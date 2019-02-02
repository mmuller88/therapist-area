package com.massagerelax.therapistarea.web.dto

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.Point
import java.math.BigDecimal

data class CreateTherapistAreaDTO(val name: String, val long: Double, val lat: Double, val radius: BigDecimal)