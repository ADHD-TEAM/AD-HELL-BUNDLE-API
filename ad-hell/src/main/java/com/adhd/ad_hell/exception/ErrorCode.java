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
  // 회원정보 관련 에러코드
  NICKNAME_ALREADY_EXISTS("10002","이미 사용중인 아이디입니다." , HttpStatus.ALREADY_REPORTED ),
  INVALID_USERNAME_OR_PASSWORD("1003", "올바르지 않은 아이디 혹은 비밀번호입니다.", HttpStatus.UNAUTHORIZED ),
  USER_NOT_FOUND("1004", "사용자를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),

  // 카테고리 관련 에러코드
  CATEGORY_NOT_FOUND("30001", "카테고리를 찾지 못했습니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatusCode httpStatusCode;
}
