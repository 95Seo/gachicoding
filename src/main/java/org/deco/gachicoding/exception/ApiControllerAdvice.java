package org.deco.gachicoding.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    // 익셉션 메세지를 response에 넣을때 좀더 디테일 하게 넣을 수 있도록 공부해 봅시다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * UncheckedException
     * Amazon S3에서 요청된 객체가 버킷에 없는 경우 오류를 반환합니다.
     * @return {@link ResponseEntity}를 반환
     */
    @ExceptionHandler(AmazonS3Exception.class)
    protected ResponseEntity<ResponseState> handleAmazonS3Exception() {
        log.error("handleAmazonS3Exception throw Exception : {}", AMAZONS_S3_EXCEPTION);
        return ResponseState.toResponseEntity(AMAZONS_S3_EXCEPTION);
    }

//    @ExceptionHandler(IOException.class)
//    protected ResponseEntity<ResponseState> handleIOException() {
//        log.error("handleIOException throw Exception : {}", INPUT_OUTPUT_EXCEPTION);
//        return ResponseState.toResponseEntity(INPUT_OUTPUT_EXCEPTION);
//    }

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ResponseState> handleDataException() {
        log.error("handleDataException throw Exception : {}", DATA_VIOLATION_EXCEPTION);
        return ResponseState.toResponseEntity(DATA_VIOLATION_EXCEPTION);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ResponseState> handleCustomException(ApplicationException e) {
        log.error("handleCustomException throw CustomException : {}", e.getStatusEnum());
        return ResponseState.toResponseEntity(e.getStatusEnum());
    }
}
