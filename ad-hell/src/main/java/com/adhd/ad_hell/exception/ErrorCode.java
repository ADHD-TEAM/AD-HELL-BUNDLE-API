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
  NICKNAME_ALREADY_EXISTS("10002","이미 사용중인 닉네임입니다." , HttpStatus.ALREADY_REPORTED ),
  INVALID_USERNAME_OR_PASSWORD("10003", "올바르지 않은 아이디 혹은 비밀번호입니다.", HttpStatus.UNAUTHORIZED ),
  USER_NOT_FOUND("10004", "사용자를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
  LOGIN_ID_ALREADY_EXISTS("10005","이미 사용중인 아이디입니다." ,  HttpStatus.ALREADY_REPORTED),
  // 카테고리 관련 에러코드
  CATEGORY_NOT_FOUND("30001", "카테고리를 찾지 못했습니다.", HttpStatus.NOT_FOUND),

  // 게시판 관련 에러코드
  BOARD_NOT_FOUND("40001", "게시판을 찾지 못했습니다.", HttpStatus.NOT_FOUND),

  //파일 관련 에러코드
  FILE_EMPTY("50001", "업로드된 파일이 비어 있습니다.", HttpStatus.NOT_FOUND),
  FILE_STORE_FAILED("50002", "파일 저장에 실패했습니다.", HttpStatus.NOT_FOUND),

  // 경품 관련 에러코드
  REWARD_NOT_FOUND("60001", "경품을 찾지 못했습니다.", HttpStatus.NOT_FOUND),
  REWARD_STOCK_INVALID_STATUS("60011", "이미 사용되었거나 만료된 경품입니다.", HttpStatus.BAD_REQUEST),

  // 신고 관련 에러코드
  REPORT_NOT_FOUND("70001", "신고사항을 찾지 못했습니다.", HttpStatus.NOT_FOUND),

  ;
  private final String code;
  private final String message;
  private final HttpStatusCode httpStatusCode;
}
