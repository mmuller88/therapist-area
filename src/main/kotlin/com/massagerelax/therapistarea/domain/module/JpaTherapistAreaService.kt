package com.massagerelax.therapistarea.domain.module

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class JpaTherapistAreaService: TherapistAreaService{
    override fun retrieveArea(id: Long): TherapistAreaEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveAreas(): List<TherapistAreaEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addArea(therapist: TherapistAreaEntity): TherapistAreaEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateArea(id: Long, therapist: TherapistAreaEntity): TherapistAreaEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteArea(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}