package com.massagerelax.therapistarea.domain

import org.springframework.context.MessageSourceResolvable

abstract class DataNotFoundException : RuntimeException, MessageSourceResolvable {

    constructor(message: String) : super(message) { }
    constructor(message: String, cause: Throwable) : super(message, cause) { }

    override fun getArguments(): Array<out Any>? = arrayOf()
    override fun getDefaultMessage(): String?  = message
}

class TherapistNameNotFoundException(private val therapistName: String) : DataNotFoundException("Therapist with name $therapistName doesn't exist") {

    override fun getCodes(): Array<out String> = arrayOf("error.therapistNameNotFound")
    override fun getArguments(): Array<out Any> = arrayOf(therapistName)
}

class IdNotFoundException(private val id: Long) : DataNotFoundException("Therapist area with id $id doesn't exist") {

    override fun getCodes(): Array<out String> = arrayOf("error.idNotFound")
    override fun getArguments(): Array<out Any> = arrayOf(id)
}