package com.massagerelax.therapistarea.web.controller

import com.massagerelax.therapistarea.domain.module.JpaTherapistAreaService
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test

import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(TherapistController::class)
class TherapistControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var jpaTherapistAreaService: JpaTherapistAreaService

    @MockBean
    lateinit var therapistAreaRepository: TherapistAreaRepository

    @Test
    fun getAllTherapistsWithinTherapistRadius() {
        given(jpaTherapistAreaService.retrieveTherapistsWithinTherapistRadius(2.0, 2.0)).willReturn(listOf("Kenia", "Martin"))

        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/2/lat/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0]", `is`("Kenia")))
                .andExpect(jsonPath("$[1]", `is`("Martin")))

        // No therapist at this radius
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/3/lat/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().string("[]"))

        // invalid arguments
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/inva/lat/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/2/lat/inva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/inva/lat/inva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getNnearestTherapists() {
        given(jpaTherapistAreaService.retrieveNnearestTherapists(2.0, 2.0, 2)).willReturn(listOf("Kenia", "Martin"))

        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/2/lat/2/n/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0]", `is`("Kenia")))
                .andExpect(jsonPath("$[1]", `is`("Martin")))

        // No therapist at this radius
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/3/lat/3/n/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().string("[]"))

        // invalid arguments
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/inva/lat/2/n/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/2/lat/inva/n/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/inva/lat/inva/n/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
        mvc.perform(MockMvcRequestBuilders.get("/api/therapists/long/2/lat/2/n/inva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }
}