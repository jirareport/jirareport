package br.com.jiratorio.config

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {

    @ExceptionHandler(MissingKotlinParameterException::class)
    fun handleMissingKotlinParameterException(
        e: MissingKotlinParameterException
    ): ResponseEntity<Map<String, List<Map<String, Any>>>> {
        return ResponseEntity(
            mapOf(
                "errors" to listOf(
                    mapOf(
                        "field" to e.parameter.name!!,
                        "messages" to listOf("must not be null")
                    )
                )
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, List<Map<String, Any>>>> {
        val errors = e.bindingResult.allErrors.groupBy {
            it as FieldError
            it.field
        }.map {
            mapOf(
                "field" to it.key,
                "messages" to it.value.mapNotNull { it.defaultMessage }
            )
        }

        return ResponseEntity(mapOf("errors" to errors), HttpStatus.BAD_REQUEST)
    }

}
