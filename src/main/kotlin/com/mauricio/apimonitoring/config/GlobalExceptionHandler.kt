package com.mauricio.apimonitoring.config

import com.mauricio.apimonitoring.dto.ApiError
import com.mauricio.apimonitoring.exception.BusinessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(
                ApiError(
                    message = ex.message ?: "Erro de regra de negócio"
                )
            )
    }
}
