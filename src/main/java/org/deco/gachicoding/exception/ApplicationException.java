package org.deco.gachicoding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    // 익셉션 코드 추가 리팩토링 필요
//    private final String errorCode;
//    private final HttpStatus httpStatus;
    private final StatusEnum statusEnum;
}
