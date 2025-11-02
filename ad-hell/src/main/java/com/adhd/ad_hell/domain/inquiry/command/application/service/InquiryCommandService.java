package com.adhd.ad_hell.domain.inquiry.command.application.service;


import com.adhd.ad_hell.domain.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.domain.category.command.domain.repository.CategoryRepository;
import com.adhd.ad_hell.domain.inquiry.command.application.dto.request.InquiryAnswerRequest;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.repository.UserCommandRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import com.adhd.ad_hell.domain.inquiry.command.application.dto.request.InquiryCreateRequest;
import com.adhd.ad_hell.domain.inquiry.command.domain.aggregate.Inquiry;
import com.adhd.ad_hell.domain.inquiry.command.domain.repository.InquiryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryCommandService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserCommandRepository userCommandRepository;


    // 문의 등록

    @Transactional
    public void createInquiry(InquiryCreateRequest req) {

        // FK 조회 (없으면 비즈니스 예외)

        User user = userCommandRepository.findById(req.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // 단순 필드 매핑 (title, content)
        Inquiry inquiry = modelMapper.map(req, Inquiry.class);

        // 연관관계 주입 (엔티티에 아래 두 메서드가 있어야 함)
        // public void linkUser(User user) { this.user = user; }
        // public void linkCategory(Category category) { this.category = category; }
        inquiry.linkUser(user);
        inquiry.linkCategory(category);

        // 저장
        inquiryRepository.save(inquiry);

    }
    // 관리자 : 문의 답뱐(신규 들록, 수정 모두 가능)
    // 관리자는 : 수정도 가능해야한다고 생각함

    // 수정 원리 : findById()로 가져온 순간 영속 상태가 되고, 트랜잭션 안에서 필드 값만
    // 바꾸면 JPA가 커밋 전에 바뀐 부분을 감지해 자동으로 UPDATE 쿼리를 날림 -> 변경감지(Dirty Checking)이라고함.
    // 그래서 Setter 없이도 도메인 메서드(answer)로 수정이 가능함

    @Transactional
    public void answerInquiry(Long inquiryId, InquiryAnswerRequest req) {
        // 엔티티 로드 : 영속 상태가 된다.
        // 못 찾으면 404error 발생
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INQUIRY_NOT_FOUND));

        // 도메인 메서드 상태 변경
        //    - Inquiry#answer() 내부에서 response, answeredAt 변경
        //    - Setter를 노출하지 않고, 의도된 변경만 허용
        inquiry.answer(req.getResponse());
        // JPA 변경감지로 자동 업데이트
    }
}
