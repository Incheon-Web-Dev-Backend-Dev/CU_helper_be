package com.example.cu_helper_be.exception;

import com.example.cu_helper_be.dto.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

/*** 전역 예외 처리 핸들러 - 모든 컨트롤러에서 발생하는 예외 일괄 처리 ***/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*** Supabase 409 Conflict 처리 - 중복 데이터 삽입 시 발생 ***/
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ApiErrorResponse> handleSupabaseConflict(HttpClientErrorException.Conflict e) {
        String body = e.getResponseBodyAsString();
        log.warn("handleSupabaseConflict() - body: {}", body);

        /*** PostgreSQL 에러 코드 23505: unique_violation ***/
        if (body.contains("23505")) {
            if (body.contains("phone")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiErrorResponse(409, "이미 예약 내역이 존재하는 전화번호입니다."));
            }
            if (body.contains("orderNumber")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiErrorResponse(409, "주문 번호가 중복되었습니다. 다시 시도해 주세요."));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiErrorResponse(409, "중복된 데이터가 존재합니다."));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(409, "요청 처리 중 충돌이 발생했습니다."));
    }

    /*** Supabase 기타 4xx 에러 처리 (400, 401, 403, 404 등) ***/
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiErrorResponse> handleSupabaseClientError(HttpClientErrorException e) {
        log.warn("handleSupabaseClientError() - status: {}, body: {}",
                e.getStatusCode(), e.getResponseBodyAsString());
        return ResponseEntity.status(e.getStatusCode())
                .body(new ApiErrorResponse(e.getStatusCode().value(),
                        "데이터베이스 요청 처리 중 오류가 발생했습니다."));
    }

    /*** 주문 조회 결과 없음 (전화번호 미일치) ***/
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NoSuchElementException e) {
        log.warn("handleNotFound() - message: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    /*** 비즈니스 로직 검증 실패 (가격 불일치, 빈 주문, 본인 인증 실패 등) ***/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument() - message: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    /*** 처리되지 않은 서버 오류 ***/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception e) {
        log.error("handleGeneral() - unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."));
    }
}
