package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * Перехватывает определённые исключения и возвращает соответствующие HTTP-ответы
 * с сообщением об ошибке.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link UnauthorizedException}.
     * Возвращает HTTP-статус 401 (Unauthorized) с сообщением об ошибке.
     *
     * @param ex Исключение {@link UnauthorizedException}.
     * @return {@link ResponseEntity} с сообщением об ошибке и статусом 401.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Обрабатывает исключение {@link ForbiddenException}.
     * Возвращает HTTP-статус 403 (Forbidden) с сообщением об ошибке.
     *
     * @param ex Исключение {@link ForbiddenException}.
     * @return {@link ResponseEntity} с сообщением об ошибке и статусом 403.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
