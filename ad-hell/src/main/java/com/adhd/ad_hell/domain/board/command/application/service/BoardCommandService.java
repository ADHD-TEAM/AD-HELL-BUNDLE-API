package com.adhd.ad_hell.domain.board.command.application.service;

import com.adhd.ad_hell.domain.board.command.application.dto.request.BoardCreateRequest;
import com.adhd.ad_hell.domain.board.command.application.dto.request.BoardUpdateRequest;
import com.adhd.ad_hell.domain.board.command.domain.aggregate.Board;
import com.adhd.ad_hell.domain.board.command.domain.repository.BoardRepository;
import com.adhd.ad_hell.domain.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.domain.category.command.domain.repository.CategoryRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardRepository boardRepository;
//    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorage fileStorage;
    private final ModelMapper modelMapper;
//    private final ImageProperties imageProperties;

    /* 게시글 등록 */
    @Transactional


    // 파일 업로드 요청이 들어옴
    public Long createBoard(BoardCreateRequest boardCreateRequest, MultipartFile boardImg) {

        // 실제 파일을 파일시스템에 저장하고, 내부 파일명 반환
        final String newFileName = fileStorage.store(boardImg);

        // DTO to Entity
        Board newBoard = modelMapper.map(boardCreateRequest, Board.class);

        //writer, category 연관관계 생성 필요

        // DB에 메타데이터(URL)만 저장 (baseUrl  + 파일명)
        // 실제 파일은 파일시스템에 있고 DB에는 어디서 불러올지 경로만 기록 하는 것
//        newBoard.changeBoardImageUrl(imageProperties.getBaseUrl() + newFileName);

        // 저장
        Board saved = boardRepository.save(newBoard);


        // DB 트랜잭션이 실패(rollback) 하면, 파일은 저장되어 있으니까 고아 파일이 됨.
        // 그래서 트랜잭션 종료 이벤트(afterCompletion)을 후킹해서, DB가 실패하면 저장한 파일도 자동 삭제처리
        // DB와 파일 저장 간 일관성을 맞춰주는 보상 트랜잭션임
        // 후킹 : 특정 동작(함수, 이벤트, 트랜잭션 등)이 실행될 때,
        // 그 사이에 내 코드를 끼워 넣어 특정 시점에 내가 원하는 코드가 함께 실행되게 만드는 것


        // 로직 롤백 될 경우 새 파일 제거 -> 롤백 보상
        // TransactionSynchronizationManager : 스프링 트랜잭션이 라이프사이클 이벤트에
        // 외부 로직(파일 삭제, 로그 기록) 등을 안전하게 연결할 수 있는 훅을 제공
        TransactionSynchronizationManager.registerSynchronization(
                // registerSynchronization 메소드를 통해 TransactionSynchronization 구현체
                // 를 등록하면 트랜잭션의 커밋 또는 롤백 직후 로직을 정의할 수 있음
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if(status != STATUS_COMMITTED) {
                            fileStorage.deleteQuietly(newFileName);
                        }
                    }
                }
        );
        return saved.getId();
    }

    // 게시글 수정(이미지 교체 포함)
    @Transactional
    public void updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest, MultipartFile ImageUrl) {

        // 기존 게시글 조회(없으면 비즈니스 예외)
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        // 기존 이미지의 URL/파일명을 보관(커밋 시 기존 파일 삭제에 사용)
        final String oldUrl = board.getImageUrl();
        final String oldFileName = extractFileName(oldUrl);


        String newFileName = null;  // 새 파일명을 저장할 변수
        // 새 이미지가 넘어 왔다면, 먼저 파일부터 저장하고 엔티티의 이미지URL 교체
        if(ImageUrl != null && !ImageUrl.isEmpty()) {
            newFileName = fileStorage.store(ImageUrl);  // 새 파일 먼저 저장
//            board.changeBoardImageUrl(imageProperties.getBaseUrl() + newFileName); // 엔티티의 URL 갱신
        }



        // 나머지 필드(제목/내용/상태/카테고리 등) 수정
        // 여기서는 엔티티의 도메인 메서드로 한 번에 업데이트
        Category category = categoryRepository.findById(boardUpdateRequest.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        board.updateBoard(
                boardUpdateRequest.getTitle(),
                boardUpdateRequest.getContent(),
                category,  // 엔티티 전달
                boardUpdateRequest.getStatus()
        );

        // 5) 트랜잭션 완료시 파일 정리
        final String finalNew = newFileName;     // 람다/익명클래스에서 사용 위해 effectively final로 보관
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if(status == STATUS_COMMITTED) {               // 커밋 성공
                            // 커밋 성공 시 기존 파일 삭제 (새 파일 반영 완료)
                            if(finalNew != null && oldFileName != null) fileStorage.deleteQuietly(oldFileName);
                        } else {                                       // 롤백
                            // 롤백이면 새 파일 삭제(디스크 정리)
                            if(finalNew != null) fileStorage.deleteQuietly(finalNew);
                        }
                    }
                }
        );
    }

    // URL에서 파일명만 뽑아내는 유틸리티(마지막 '/' 이후 문자열을 반환)
    private String extractFileName(String url) {
        if (url == null) return null;
        int idx = url.lastIndexOf('/');
        return (idx >= 0 && idx < url.length() - 1) ? url.substring(idx + 1) : url;
    }


     // 게시글 삭제 (soft delete)

    @Transactional
    public void deleteBoard(Long boardId) {
        // 단순 삭제. 실서비스에서는 @SQLDelete 또는 status="N"으로 소프트 삭제 권장
        boardRepository.deleteById(boardId);
    }
}

