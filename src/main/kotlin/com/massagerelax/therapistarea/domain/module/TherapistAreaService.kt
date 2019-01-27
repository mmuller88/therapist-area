package com.massagerelax.therapistarea.domain.module

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity

interface TherapistAreaService {
    fun retrieveArea(id: Long): TherapistAreaEntity

    fun retrieveAreas(): List<TherapistAreaEntity>

    fun addArea(therapist: TherapistAreaEntity): TherapistAreaEntity

    fun updateArea(id: Long, therapist: TherapistAreaEntity): TherapistAreaEntity

    fun deleteArea(id: Long)
}