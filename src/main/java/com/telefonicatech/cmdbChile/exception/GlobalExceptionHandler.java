package com.telefonicatech.cmdbChile.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    ProblemDetail handleNotFound(ChangeSetPersister.NotFoundException ex, HttpServletRequest req){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Item Not Found");
        pd.setProperty("instance", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed!!!");
        pd.setTitle("Bad Request");
        pd.setProperty("errors", ex.getBindingResult().getFieldErrors()
                .stream().map(err->err.getField() + ": " + err.getDefaultMessage()).toString()
        );
        pd.setProperty("instance", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Bad Request");
        pd.setProperty("instance", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid Input");
        pd.setTitle("Bad Request");
        pd.setProperty("violations", ex.getConstraintViolations()
                .stream().map(v-> v.getPropertyPath() + ": " + v.getMessage())
                .toList());
        pd.setProperty("instance", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleGeneric(Exception ex)
    {
        ProblemDetail pd= ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        pd.setTitle("Internal Server error");

        return pd;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ProblemDetail handleMethodValidation(HandlerMethodValidationException ex, HttpServletRequest req){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Bad Request");
        pd.setDetail("Validation failed on controller method");

        // Opcional: ex.getAllErrors() trae los detalles de las violaciones
        pd.setProperty("errors", ex.getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .toList());
        pd.setProperty("instance", req.getRequestURI());
        return pd;
    }
}
