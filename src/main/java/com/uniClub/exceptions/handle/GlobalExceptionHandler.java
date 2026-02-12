package com.uniClub.exceptions.handle;

import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.FileStorageException;
import com.uniClub.exceptions.exception.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError<String>> handleBaseException(BaseException ex, WebRequest request) {
        MessageType messageType = ex.getErrorMessage().getMessageType();
        HttpStatus status = messageType.getHttpStatus();

        // Sadece mesaj metnini dönmek daha temiz bir API çıktısı sağlar
        String fullMessage = ex.getErrorMessage().prepareErrorMessage();

        return ResponseEntity.status(status)
                .body(createApiError(fullMessage, request, status));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError<Map<String, List<String>>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, List<String>> map = new HashMap<>();
        for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) objectError).getField();
            map.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(objectError.getDefaultMessage());
        }
        // HttpStatus'u açıkça belirterek gönderiyoruz
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createApiError(map, request, HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiError<String>> handleFileStorageException(FileStorageException ex, WebRequest request) {
        // Mesaj tipi olarak sistemdeki FILE_NOT_FOUND veya yeni bir FILE_UPLOAD_ERROR kullanabilirsin
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createApiError(ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR));
    }
    // Genel tüm beklenmedik hatalar için (NullPointer vb.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError<String>> handleAllExceptions(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createApiError(ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public <E> ApiError<E> createApiError(E message, WebRequest request, HttpStatus status) {
        ApiError<E> apiError = new ApiError<>();
        apiError.setStatus(status.value());

        ErrorDetail<E> errorDetail = new ErrorDetail<>();
        errorDetail.setPath(request.getDescription(false).replace("uri=", ""));
        errorDetail.setMessage(message);
        errorDetail.setHost(getHostName());
        errorDetail.setCreateTime(LocalDateTime.now());

        apiError.setErrorDetail(errorDetail);
        return apiError;
    }

    private String getHostName() {
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown-host";
        }
    }
}