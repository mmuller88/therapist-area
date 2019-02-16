package com.massagerelax.therapistarea.domain.module

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import java.math.BigDecimal

interface TherapistAreaService {
    fun retrieveArea(id: Long): TherapistAreaEntity

    fun retrieveAreas(): List<TherapistAreaEntity>

    fun retrieveAreasWithinTherapistRadius(long: Double, lat: Double): List<TherapistAreaEntity>

    fun retrieveTherapistsWithinTherapistRadius(long: Double, lat: Double): List<String>

    fun retrieveAreasWithinRadius(long: Double, lat: Double, radius: BigDecimal): List<TherapistAreaEntity>

    fun retrieveNnearestTherapists(long: Double, lat: Double, n: Int): List<String>

    fun addArea(therapist: TherapistAreaEntity): TherapistAreaEntity

    fun updateArea(id: Long, therapist: TherapistAreaEntity): TherapistAreaEntity

    fun deleteArea(id: Long)
}