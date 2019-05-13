package br.com.jiratorio.config

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.exception.UniquenessFieldException
import br.com.jiratorio.extension.log
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.UUID
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ErrorHandler(
    private val messageResolver: MessageResolver
) {

    @ExceptionHandler(MissingKotlinParameterException::class)
    fun handleMissingKotlinParameterException(e: MissingKotlinParameterException): ResponseEntity<Map<String, List<String>>> {
        return ResponseEntity(
            mapOf(e.parameter.name!! to listOf(messageResolver.resolve("javax.validation.constraints.NotNull.message"))),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<Map<String, List<String>>> {
        val bindingResult = e.bindingResult
        return buildBindResultResponseErrors(bindingResult)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<Map<String, List<String>>> {
        val bindingResult = e.bindingResult
        return buildBindResultResponseErrors(bindingResult)
    }

    private fun buildBindResultResponseErrors(bindingResult: BindingResult): ResponseEntity<Map<String, List<String>>> {
        val errors = bindingResult.allErrors.groupBy {
            it as FieldError
            it.field
        }.map {
            it.key to it.value.mapNotNull { it.defaultMessage }
        }.toMap()

        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UniquenessFieldException::class)
    fun handleUniquenessException(e: UniquenessFieldException): ResponseEntity<Map<String, List<String>>> {
        return ResponseEntity(
            mapOf(e.field to listOf(messageResolver.resolve("validations.uniqueness"))),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun defaultHandler(e: Exception, request: HttpServletResponse) {
        val uuid = UUID.randomUUID().toString()
        log.error("Method=defaultHandler, MSG=trace uuid, uuid={}", uuid, e)
        request.sendError(500, uuid)
    }

}
