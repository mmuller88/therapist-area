package com.massagerelax.therapistarea.domain.module

import com.massagerelax.therapistarea.domain.IdNotFoundException
import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional
import org.springframework.data.domain.PageRequest



@Service
@Transactional
class JpaTherapistAreaService(val therapistAreaRepository: TherapistAreaRepository): TherapistAreaService{

    override fun retrieveArea(id: Long): TherapistAreaEntity {
        return therapistAreaRepository.findById(id).orElseThrow { IdNotFoundException(id) }
    }

    override fun retrieveAreas(): List<TherapistAreaEntity> {
        return therapistAreaRepository.findAll()
    }

    override fun retrieveAreasWithinTherapistRadius(long: Double, lat: Double): List<TherapistAreaEntity> {
        val gf = GeometryFactory()
        val point = gf.createPoint(Coordinate(long, lat))
        return therapistAreaRepository.findWithinTherapistRadius(point)
    }

    override fun retrieveTherapistsWithinTherapistRadius(long: Double, lat: Double): List<String> {
        val gf = GeometryFactory()
        val point = gf.createPoint(Coordinate(long, lat))
        return therapistAreaRepository.findWithinTherapistRadiusTherapists(point)
    }

    override fun retrieveAreasWithinRadius(long: Double, lat: Double, radius: BigDecimal): List<TherapistAreaEntity> {
        val gf = GeometryFactory()
        val point = gf.createPoint(Coordinate(long, lat))
        return therapistAreaRepository.findWithinRadius(point, radius)
    }

    override fun retrieveNnearestTherapists(long: Double, lat: Double, n: Int): List<String> {
        val gf = GeometryFactory()
        val point = gf.createPoint(Coordinate(long, lat))
        val limit = PageRequest.of(0, n)
        return therapistAreaRepository.findNnearestTherapists(point, limit)
    }

    override fun addArea(therapistArea: TherapistAreaEntity): TherapistAreaEntity {
        return therapistAreaRepository.save(therapistArea)
    }

    override fun updateArea(id: Long, updateTherapistArea: TherapistAreaEntity): TherapistAreaEntity {
        return therapistAreaRepository.findById(id).map {
            existingTherapistArea -> val updatedTherapistArea = existingTherapistArea.copy(
                name = updateTherapistArea.name,
                location = updateTherapistArea.location,
                radius = updateTherapistArea.radius)
            therapistAreaRepository.save(updatedTherapistArea)
        }.orElseThrow { IdNotFoundException(id) }
    }

    override fun deleteArea(id: Long) {
        therapistAreaRepository.findById(id).map { existingArea ->
            therapistAreaRepository.delete(existingArea)
        }.orElseThrow{ IdNotFoundException(id)}
    }

}