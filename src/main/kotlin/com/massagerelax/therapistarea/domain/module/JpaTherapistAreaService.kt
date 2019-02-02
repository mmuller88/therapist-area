package com.massagerelax.therapistarea.domain.module

import com.massagerelax.therapistarea.domain.IdNotFoundException
import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class JpaTherapistAreaService(val therapistAreaRepository: TherapistAreaRepository): TherapistAreaService{
    override fun retrieveArea(id: Long): TherapistAreaEntity {
        return therapistAreaRepository.findById(id).orElseThrow { IdNotFoundException(id) }
    }

    override fun retrieveAreas(): List<TherapistAreaEntity> {
        return therapistAreaRepository.findAll()
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