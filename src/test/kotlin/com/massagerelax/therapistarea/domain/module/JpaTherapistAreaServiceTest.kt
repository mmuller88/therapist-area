package com.massagerelax.therapistarea.domain.module

import com.massagerelax.therapistarea.domain.IdNotFoundException
import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.hamcrest.CoreMatchers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKTReader
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Assertions
import org.mockito.BDDMockito
import org.mockito.Mockito.doNothing


@RunWith(MockitoJUnitRunner::class)
class JpaTherapistAreaServiceTest {

    @Mock
    lateinit var therapistAreaRepository: TherapistAreaRepository

    lateinit var therapistAreaService: JpaTherapistAreaService

    var tarea1 = TherapistAreaEntity(id=1, name = "Kenia Alves", geom = wktToGeometry("POINT(-105 40)"), radius = 5.0)
    var tarea11 = TherapistAreaEntity(id=2,name = "Kenia Alves", geom = wktToGeometry("POINT(-120 40)"), radius = 5.0)
    var tarea2 = TherapistAreaEntity(id=3,name = "Martin Mueller", geom = wktToGeometry("POINT(-111 40)"), radius = 5.0)
    var tarea21 = TherapistAreaEntity(id=4,name = "Martin Mueller", geom = wktToGeometry("POINT(-111 40)"), radius = 10.0)

    @Before
    fun setup() {
        therapistAreaService = JpaTherapistAreaService(therapistAreaRepository)
    }

    @Test
    fun retrieveArea() {
        given(therapistAreaRepository.findById(tarea1.id)).willReturn(Optional.of(tarea1))
        val retrieveArea = therapistAreaService.retrieveArea(tarea1.id)
        assertThat(retrieveArea, `is`(tarea1))

        Assertions.assertThrows(IdNotFoundException::class.java){
            therapistAreaService.retrieveArea(tarea11.id)
        }
    }

    @Test
    fun retrieveAreas() {
        val areas = listOf(tarea1, tarea11)
        given(therapistAreaRepository.findAll()).willReturn(areas)
        val retrieveTherapists = therapistAreaService.retrieveAreas()
        assertThat(retrieveTherapists[0], `is`(tarea1))
        assertThat(retrieveTherapists[1], `is`(tarea11))
    }

    @Test
    fun addArea() {
        given(therapistAreaRepository.save(tarea1)).willReturn(tarea1)
        val addTherapist = therapistAreaService.addArea(tarea1)
        assertThat(addTherapist, `is`(tarea1))
    }

    @Test
    fun updateArea() {
        val updatedtArea1 = tarea1.copy(name = "Blub", radius = 4.0)
        given(therapistAreaRepository.findById(tarea1.id)).willReturn(Optional.of(tarea1))
        given(therapistAreaRepository.save(updatedtArea1)).willReturn(updatedtArea1)
        val retrieveUpdateArea = therapistAreaService.updateArea(updatedtArea1.id, updatedtArea1)
        assertThat(retrieveUpdateArea, `is`(updatedtArea1))

        Assertions.assertThrows(IdNotFoundException::class.java) {
            therapistAreaService.updateArea(tarea11.id, updatedtArea1)
        }
    }

    @Test
    fun deleteArea() {
        given(therapistAreaRepository.findById(tarea1.id)).willReturn(Optional.of(tarea1))
        doNothing().`when`(therapistAreaRepository).delete(tarea1)
        therapistAreaService.deleteArea(tarea1.id)

        Assertions.assertThrows(IdNotFoundException::class.java) {
            therapistAreaService.deleteArea(tarea11.id)
        }
    }

    private fun wktToGeometry(wktPoint: String): Geometry {
        val fromText = WKTReader()
        var geom: Geometry
        try {
            geom = fromText.read(wktPoint)
        } catch (e: ParseException) {
            throw RuntimeException("Not a WKT string:$wktPoint")
        }

        return geom
    }
}