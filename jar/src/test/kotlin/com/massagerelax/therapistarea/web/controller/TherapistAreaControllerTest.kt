package com.massagerelax.therapistarea.web.controller

import com.massagerelax.therapistarea.domain.IdNotFoundException
import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.domain.module.JpaTherapistAreaService
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.junit.jupiter.api.Test

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@WebMvcTest(TherapistAreaController::class)
class TherapistAreaControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var jpaTherapistAreaService: JpaTherapistAreaService

    @MockBean
    lateinit var therapistAreaRepository: TherapistAreaRepository

    val geometryFactory = GeometryFactory()

    val kenia = TherapistAreaEntity(
            id = 0,
            name ="Kenia",
            location = geometryFactory.createPoint(Coordinate(1.0, 2.0)),
            radius = BigDecimal(3))

    val martin = TherapistAreaEntity(
            id = 1,
            name ="Martin",
            location = geometryFactory.createPoint(Coordinate(7.0, 9.0)),
            radius = BigDecimal(2))

    @Test
    fun getAllTherapistAreas() {

        given(jpaTherapistAreaService.retrieveAreas()).willReturn(listOf(kenia, martin))

        mvc.perform(get("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0].id", `is`(0)))
                .andExpect(jsonPath("$[0].name", `is`("Kenia")))
                .andExpect(jsonPath("$[0].long", `is`(1.0)))
                .andExpect(jsonPath("$[0].lat", `is`(2.0)))
                .andExpect(jsonPath("$[0].radius", `is`(3)))
                .andExpect(jsonPath("$[1].id", `is`(1)))
                .andExpect(jsonPath("$[1].name", `is`("Martin")))
                .andExpect(jsonPath("$[1].long", `is`(7.0)))
                .andExpect(jsonPath("$[1].lat", `is`(9.0)))
                .andExpect(jsonPath("$[1].radius", `is`(2)))
    }

    @Test
    fun getAllAreasWithinTherapistRadius() {
        given(jpaTherapistAreaService.retrieveAreasWithinTherapistRadius(2.0, 3.0)).willReturn(listOf(kenia))
        given(jpaTherapistAreaService.retrieveAreasWithinTherapistRadius(7.0, 9.0)).willReturn(listOf(martin))

        // kenia at this area
        mvc.perform(get("/api/therapist-areas/long/2/lat/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", `is`(0)))
                .andExpect(jsonPath("$[0].name", `is`("Kenia")))
                .andExpect(jsonPath("$[0].long", `is`(1.0)))
                .andExpect(jsonPath("$[0].lat", `is`(2.0)))
                .andExpect(jsonPath("$[0].radius", `is`(3)))

        // martin at this area
        mvc.perform(get("/api/therapist-areas/long/7/lat/9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", `is`(1)))
                .andExpect(jsonPath("$[0].name", `is`("Martin")))
                .andExpect(jsonPath("$[0].long", `is`(7.0)))
                .andExpect(jsonPath("$[0].lat", `is`(9.0)))
                .andExpect(jsonPath("$[0].radius", `is`(2)))

        // No therapist at this area
        mvc.perform(get("/api/therapist-areas/long/4/lat/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"))

        // invalid arguments
        mvc.perform(get("/api/therapist-areas/long/abc/lat/a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(get("/api/therapist-areas/long/1/lat/a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(get("/api/therapist-areas/long/abc/lat/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getAllAreasWithinRadius() {

        given(jpaTherapistAreaService.retrieveAreasWithinRadius(2.0, 3.0, BigDecimal(1))).willReturn(listOf(kenia))
        given(jpaTherapistAreaService.retrieveAreasWithinRadius(7.0, 9.0, BigDecimal(1))).willReturn(listOf(martin))
        given(jpaTherapistAreaService.retrieveAreasWithinRadius(5.0, 5.0, BigDecimal(10))).willReturn(listOf(kenia, martin))

        // kenia in radius
        mvc.perform(get("/api/therapist-areas/long/2/lat/3/radius/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", `is`(0)))
                .andExpect(jsonPath("$[0].name", `is`("Kenia")))
                .andExpect(jsonPath("$[0].long", `is`(1.0)))
                .andExpect(jsonPath("$[0].lat", `is`(2.0)))
                .andExpect(jsonPath("$[0].radius", `is`(3)))

        // martin in radius
        mvc.perform(get("/api/therapist-areas/long/7/lat/9/radius/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", `is`(1)))
                .andExpect(jsonPath("$[0].name", `is`("Martin")))
                .andExpect(jsonPath("$[0].long", `is`(7.0)))
                .andExpect(jsonPath("$[0].lat", `is`(9.0)))
                .andExpect(jsonPath("$[0].radius", `is`(2)))

        // martin and kenia in radius
        mvc.perform(get("/api/therapist-areas/long/5/lat/5/radius/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0].id", `is`(0)))
                .andExpect(jsonPath("$[0].name", `is`("Kenia")))
                .andExpect(jsonPath("$[0].long", `is`(1.0)))
                .andExpect(jsonPath("$[0].lat", `is`(2.0)))
                .andExpect(jsonPath("$[0].radius", `is`(3)))
                .andExpect(jsonPath("$[1].id", `is`(1)))
                .andExpect(jsonPath("$[1].name", `is`("Martin")))
                .andExpect(jsonPath("$[1].long", `is`(7.0)))
                .andExpect(jsonPath("$[1].lat", `is`(9.0)))
                .andExpect(jsonPath("$[1].radius", `is`(2)))

        // No therapist at this area
        mvc.perform(get("/api/therapist-areas/long/4/lat/4/radius/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"))

        // invalid arguments
        mvc.perform(get("/api/therapist-areas/long/abc/lat/a/radius/a")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(get("/api/therapist-areas/long/1/lat/a/radius/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(get("/api/therapist-areas/long/abc/lat/1/radius/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(get("/api/therapist-areas/long/1/lat/1/radius/invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getTherapistAreaById() {
        given(jpaTherapistAreaService.retrieveArea(0)).willReturn(kenia)
        given(jpaTherapistAreaService.retrieveArea(1)).willReturn(martin)
        given(jpaTherapistAreaService.retrieveArea(999)).willThrow(IdNotFoundException(999))

        // kenia's id
        mvc.perform(get("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", `is`(0)))
                .andExpect(jsonPath("$.name", `is`("Kenia")))
                .andExpect(jsonPath("$.long", `is`(1.0)))
                .andExpect(jsonPath("$.lat", `is`(2.0)))
                .andExpect(jsonPath("$.radius", `is`(3)))

        // Martin's id
        mvc.perform(get("/api/therapist-areas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", `is`(1)))
                .andExpect(jsonPath("$.name", `is`("Martin")))
                .andExpect(jsonPath("$.long", `is`(7.0)))
                .andExpect(jsonPath("$.lat", `is`(9.0)))
                .andExpect(jsonPath("$.radius", `is`(2)))

        // Not existing id
        mvc.perform(get("/api/therapist-areas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)

        // invalid id
        mvc.perform(get("/api/therapist-areas/invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(get("/api/therapist-areas/1.2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun createNewTherapistArea() {

        given(jpaTherapistAreaService.addArea(kenia.copy(id=0))).willReturn(kenia)
        given(jpaTherapistAreaService.addArea(martin.copy(id=0))).willReturn(martin)

        // add kenia
        mvc.perform(post("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Kenia\",\"long\":1,\"lat\":2,\"radius\":3}"))
                .andExpect(status().isCreated)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", `is`(0)))
                .andExpect(jsonPath("$.name", `is`("Kenia")))
                .andExpect(jsonPath("$.long", `is`(1.0)))
                .andExpect(jsonPath("$.lat", `is`(2.0)))
                .andExpect(jsonPath("$.radius", `is`(3)))

        // add Martin
        mvc.perform(post("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Martin\",\"long\":7,\"lat\":9,\"radius\":2}"))
                .andExpect(status().isCreated)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", `is`(1)))
                .andExpect(jsonPath("$.name", `is`("Martin")))
                .andExpect(jsonPath("$.long", `is`(7.0)))
                .andExpect(jsonPath("$.lat", `is`(9.0)))
                .andExpect(jsonPath("$.radius", `is`(2)))

        // invalid bodies
        mvc.perform(post("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"na\":\"Martin\",\"long\":7,\"lat\":9,\"radius\":2}"))
                .andExpect(status().isBadRequest)
        mvc.perform(post("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Martin\",\"long\":\"7a\",\"lat\":9,\"radius\":2}"))
                .andExpect(status().isBadRequest)
        mvc.perform(post("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Martin\",\"long\":7,\"lat\":9}"))
                .andExpect(status().isBadRequest)

        // method not allowed
        mvc.perform(put("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Kenia\",\"long\":1,\"lat\":2,\"radius\":3}"))
                .andExpect(status().isMethodNotAllowed)
        mvc.perform(delete("/api/therapist-areas")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Kenia\",\"long\":1,\"lat\":2,\"radius\":3}"))
                .andExpect(status().isMethodNotAllowed)
    }

    @Test
    fun updateTherapistAreaById() {
        given(jpaTherapistAreaService.retrieveArea(0)).willReturn(kenia)
        given(jpaTherapistAreaService.retrieveArea(999)).willThrow(IdNotFoundException(999))

        given(jpaTherapistAreaService.updateArea(0, kenia)).willReturn(kenia)

        // update kenia
        mvc.perform(put("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Kenia\",\"long\":1,\"lat\":2,\"radius\":3}"))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", `is`(0)))
                .andExpect(jsonPath("$.name", `is`("Kenia")))
                .andExpect(jsonPath("$.long", `is`(1.0)))
                .andExpect(jsonPath("$.lat", `is`(2.0)))
                .andExpect(jsonPath("$.radius", `is`(3)))

        // id not found
        mvc.perform(put("/api/therapist-areas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Kenia\",\"long\":1,\"lat\":2,\"radius\":3}"))
                .andExpect(status().isNotFound)

        // invalid id
        mvc.perform(put("/api/therapist-areas/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Martin\",\"long\":7,\"lat\":9,\"radius\":2}"))
                .andExpect(status().isBadRequest)

        // invalid bodies
        mvc.perform(put("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"na\":\"Martin\",\"long\":7,\"lat\":9,\"radius\":2}"))
                .andExpect(status().isBadRequest)
        mvc.perform(put("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Martin\",\"long\":\"7a\",\"lat\":9,\"radius\":2}"))
                .andExpect(status().isBadRequest)
        mvc.perform(put("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Martin\",\"long\":7,\"lat\":9}"))
                .andExpect(status().isBadRequest)

        // method not allowed
        mvc.perform(post("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8")
                .content("{\"name\":\"Kenia\",\"long\":1,\"lat\":2,\"radius\":3}"))
                .andExpect(status().isMethodNotAllowed)
    }

    @Test
    fun deleteTherapistById() {
        given(jpaTherapistAreaService.deleteArea(999)).willThrow(IdNotFoundException(999))

        // delete Kenia
        mvc.perform(delete("/api/therapist-areas/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent)

        // id not found
        mvc.perform(delete("/api/therapist-areas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }
}