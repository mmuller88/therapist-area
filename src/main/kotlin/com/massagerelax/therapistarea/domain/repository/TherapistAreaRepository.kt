package com.massagerelax.therapistarea.domain.repository

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import org.locationtech.jts.geom.Geometry
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal

interface TherapistAreaRepository : JpaRepository<TherapistAreaEntity, Long>{

    @Query("select a from TherapistAreaEntity a where within(a.location, ?1) = true")
    fun findWithin(location: Geometry?): List<TherapistAreaEntity>

    @Query("select a from TherapistAreaEntity a where dwithin(a.location, ?1, a.radius) = true")
    fun findWithinTherapistRadius(location: Geometry): List<TherapistAreaEntity>

    @Query("select distinct a.name from (select a.name from therapistarea a where st_dwithin(a.location, ?1, a.radius) = true) a", nativeQuery = true)
    fun findWithinTherapistRadiusTherapists(location: Geometry): List<String>

    @Query("select a from TherapistAreaEntity a where dwithin(a.location, ?1, ?2) = true")
    fun findWithinRadius(location: Geometry, radius: BigDecimal): List<TherapistAreaEntity>

    @Query("select distinct a.name from (select a.name from therapistarea a order by a.location <-> ?1) a", nativeQuery = true)
    fun findNnearestTherapists(location: Geometry, limit: Pageable): List<String>
}