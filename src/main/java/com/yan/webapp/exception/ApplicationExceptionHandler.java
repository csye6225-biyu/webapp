package com.yan.webapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleInvalidArgument(ConstraintViolationException exception,
                                                          HttpServletRequest request) {
        System.out.println("handle ConstraintViolationException");
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handel invalid input type constrained by @Valid validator, eg "quantity": -1
     * @param exception
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInvalidArgument(MethodArgumentNotValidException exception,
                                                          HttpServletRequest request) {
        System.out.println("handle Invalid argument");
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle duplicate key value violates unique constraint
     * @param e
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(DataIntegrityViolationException e,
                                                              HttpServletRequest request){
        System.out.println("handle DataIntegrityViolationException");
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getRootCause().getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle invalid user input type, like "quantity": "abcd".
     * @param e
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleException(HttpMessageNotReadableException  e,
                                                    HttpServletRequest request){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle customized bad request exception
     * @param e
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleException(BadRequestException e,
                                                    HttpServletRequest request) {
        System.out.println("handle bad request");
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle customized resource not found exception
     * @param e
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException e,
                                                    HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle customized 403 forbidden exception
     * @param e
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleException(ForbiddenException e,
                                                    HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    /**
     * Try to customized 403 forbidden, but did not work
     * @param e
     * @param request
     * @return ApiError
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiError> handleException(InsufficientAuthenticationException e,
                                                    HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    /**
     * Try to customize 401 unauthorized exception, but did not work
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleException(BadCredentialsException e,
                                                    HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Global exception handler, anything else not caught will fall into this exception
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e,
                                                    HttpServletRequest request) {

        System.out.println(e.toString());
        System.out.println(e.getCause());
        System.out.println(e.getClass());
        System.out.println(e.fillInStackTrace());
        e.printStackTrace();
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
