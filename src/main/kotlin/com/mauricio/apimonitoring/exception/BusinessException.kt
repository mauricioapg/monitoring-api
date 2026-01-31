package com.mauricio.apimonitoring.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class BusinessException(message: String): RuntimeException(message) {}