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
  CATEGORY_NOT_FOUND("30001", "카테고리를 찾지 못했습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatusCode httpStatusCode;
}
