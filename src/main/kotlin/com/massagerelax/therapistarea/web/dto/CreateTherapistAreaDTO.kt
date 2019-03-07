package com.massagerelax.therapistarea.web.dto

import java.math.BigDecimal

data class CreateTherapistAreaDTO(val name: String, val long: Double, val lat: Double, val radius: BigDecimal)