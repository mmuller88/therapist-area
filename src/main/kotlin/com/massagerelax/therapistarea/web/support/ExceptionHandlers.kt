package com.massagerelax.therapistarea.web.support

import com.massagerelax.therapistarea.web.support.ErrorResponseEntity.Companion.notFound
import com.massagerelax.therapistarea.domain.DataNotFoundException
import com.massagerelax.therapistarea.web.support.ErrorResponseEntity.Companion.badRequest
import com.massagerelax.therapistarea.web.support.ErrorResponseEntity.Companion.serverError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*
import javax.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import javax.validation.ConstraintViolation
import org.springframework.transaction.TransactionSystemException
import org.springframework.web.context.request.WebRequest





@ControllerAdvice
class ExceptionHandlers @Autowired constructor(var messageSource: MessageSource) {

    @ExceptionHandler(DataNotFoundException::class)
    fun resourceNotFoundException(exception: DataNotFoundException, locale: Locale) =
            notFound(messageSource.getMessage(exception, locale))

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(exception: ConstraintViolationException, locale: Locale) =
            badRequest(exception.localizedMessage)

    @ExceptionHandler(TransactionSystemException::class)
    fun handleConstraintViolation(ex: Exception, request: WebRequest): ErrorResponseEntity {
        val cause = (ex as TransactionSystemException).rootCause
        return when(cause) {
            is ConstraintViolationException -> {
                badRequest(cause.localizedMessage)
            }
            else -> {
                if(cause!=null){
                    serverError(cause.localizedMessage)
                } else {
                    serverError(ex.localizedMessage)
                }
            }
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(exception: MethodArgumentNotValidException, locale: Locale) =
            badRequest("Argument validation failed", mapBindingResult(exception.bindingResult, locale))

    @ExceptionHandler(BindException::class)
    fun bindException(exception: BindException, locale: Locale) =
            badRequest("Fatal Binding Exception", mapBindingResult(exception.bindingResult, locale))

    fun mapBindingResult(bindingResult: BindingResult, locale: Locale) =
            bindingResult.allErrors.map { messageSource.getMessage(it, locale) }

}