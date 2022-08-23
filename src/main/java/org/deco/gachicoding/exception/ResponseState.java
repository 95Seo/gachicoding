package org.deco.gachicoding.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseState {
    private final LocalDateTime Date = LocalDateTime.now();
    private final int statusCode;
    private final String statusMessage;
    private final String code;
    private final String message;

    public static ResponseEntity<ResponseState> toResponseEntity(StatusEnum statusEnum) {
        return ResponseEntity
                .status(statusEnum.getHttpStatus())
                .body(ResponseState.builder()
                        .statusCode(statusEnum.getHttpStatus().value())
                        .statusMessage(statusEnum.getHttpStatus().name())
                        .code(statusEnum.name())
                        .message(statusEnum.getDetail())
                        .build());
    }
}
