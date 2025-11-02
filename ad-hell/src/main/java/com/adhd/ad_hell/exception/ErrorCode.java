package com.adhd.ad_hell.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    // ex. 에러코드
    EXAMPLE_NOT_FOUND("10001", "예제 메세지입니다.", HttpStatus.NOT_FOUND),

    // 카테고리 관련 에러코드
    CATEGORY_NOT_FOUND("30001", "카테고리를 찾지 못했습니다.", HttpStatus.NOT_FOUND),


    //광고 관련 에러 코드
    AD_NOT_FOUND("30000", "요청한 광고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 파일 관련 에러 코드
    // 파일/이미지 스토리지 관련 오류 (20000대)
    FILE_DIR_CREATE_FAILED("20001", "업로드 디렉터리 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_EMPTY("20002", "업로드할 파일이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    FILE_NAME_NOT_PRESENT("20003", "원본 파일명이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    FILE_EXTENSION_NOT_ALLOWED("20004", "허용되지 않은 파일 확장자입니다.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_PATH_TRAVERSAL_DETECTED("20005", "허용되지 않은 파일 경로가 감지되었습니다.", HttpStatus.BAD_REQUEST),
    FILE_SAVE_IO_ERROR("20006", "파일 저장 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_IO_ERROR("20007", "파일 삭제 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND("20008", "요청한 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatusCode httpStatusCode;
}
