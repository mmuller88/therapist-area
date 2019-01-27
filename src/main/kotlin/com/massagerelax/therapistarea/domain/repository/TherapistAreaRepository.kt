package com.massagerelax.therapistarea.domain.repository

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import org.locationtech.jts.geom.Geometry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TherapistAreaRepository : JpaRepository<TherapistAreaEntity, Long>{

    @Query("select a from TherapistAreaEntity a where within(a.geom, ?1) = true")
    fun findWithin(filter: Geometry?): List<TherapistAreaEntity>

    @Query("select a from TherapistAreaEntity a where dwithin(a.geom, ?1, a.radius) = true")
    fun findWithinTherapistRadius(filter: Geometry): List<TherapistAreaEntity>

    @Query("select a from TherapistAreaEntity a where dwithin(a.geom, ?1, ?2) = true")
    fun findWithinRadius(filter: Geometry, radius: Double): List<TherapistAreaEntity>
}