package com.adhd.ad_hell.domain.board.command.application.controller;

import com.adhd.ad_hell.domain.board.command.application.dto.request.BoardCreateRequest;
import com.adhd.ad_hell.domain.board.command.application.dto.request.BoardUpdateRequest;
import com.adhd.ad_hell.domain.board.command.application.dto.response.BoardCommandResponse;
import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.board.command.application.service.BoardCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardCommandController {

    private final BoardCommandService boardCommandService;

    /** 1) JSON 전용: 파일 없이 생성 (Postman: Body -> raw -> JSON) */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<BoardCommandResponse>> createBoardJson(
            @Valid @RequestBody BoardCreateRequest request
    ) {
        Long boardId = boardCommandService.createBoard(request, null);
        BoardCommandResponse response = BoardCommandResponse.builder().id(boardId).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    /** 2) 멀티파트: JSON + 이미지로 생성 (Postman: Body -> form-data) */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<BoardCommandResponse>> createBoardMultipart(
            @RequestPart("request") @Valid BoardCreateRequest request,          // JSON 파트
            @RequestPart(value = "boardImg", required = false) MultipartFile boardImg // 파일 파트(선택)
    ) {
        Long boardId = boardCommandService.createBoard(request, boardImg);
        BoardCommandResponse response = BoardCommandResponse.builder().id(boardId).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    /** 3) 업데이트 - JSON 전용 */
    @PutMapping(value = "/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateBoardJson(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardUpdateRequest request
    ) {
        boardCommandService.updateBoard(boardId, request, null);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 4) 업데이트 - 멀티파트(JSON + 이미지) */
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateBoardMultipart(
            @PathVariable Long boardId,
            @RequestPart("request") @Valid BoardUpdateRequest request,
            @RequestPart(value = "boardImg", required = false) MultipartFile boardImg
    ) {
        boardCommandService.updateBoard(boardId, request, boardImg);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /** 5) 삭제 */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable Long boardId) {
        boardCommandService.deleteBoard(boardId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}