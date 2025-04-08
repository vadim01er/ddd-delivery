package ru.ershov.ddd.delivery.api.adapters.http

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.ershov.ddd.delivery.api.adapters.generated.models.Error

@RestControllerAdvice
class WebExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun onJsonMappingException(e: Exception): Error {
        return Error(
            code = 500,
            message = e.message!!,
        )
    }

}