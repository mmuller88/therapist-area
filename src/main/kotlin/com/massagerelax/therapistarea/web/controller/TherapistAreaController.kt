package com.massagerelax.therapistarea.web.controller

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.web.support.ErrorResponse
import com.massagerelax.therapistarea.domain.module.JpaTherapistAreaService
import com.massagerelax.therapistarea.web.dto.CreateTherapistAreaDTO
import com.massagerelax.therapistarea.web.dto.TherapistAreaDTO
import com.massagerelax.therapistarea.web.dto.UpdateTherapistAreaDTO
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import javax.validation.Valid

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api")
class TherapistAreaController(private val jpaTherapistAreaService: JpaTherapistAreaService){

    @GetMapping("/therapist-areas")
    @ApiResponses(value = [
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun getAllTherapistAreas(): ResponseEntity<List<TherapistAreaDTO>> {
        return ResponseEntity.ok(jpaTherapistAreaService.retrieveAreas().map { therapistEntity -> therapistEntity.toDto()})
    }

    @GetMapping("/therapist-areas/long/{long}/lat/{lat}")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter for long or lat", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun getAllAreasWithinTherapistRadius(@PathVariable(value = "long") long: Double, @PathVariable(value = "lat") lat: Double): ResponseEntity<List<TherapistAreaDTO>> {
        return ResponseEntity.ok(jpaTherapistAreaService.retrieveAreasWithinTherapistRadius(long, lat).map { therapistEntity -> therapistEntity.toDto()})
    }

    @GetMapping("/therapist-areas/long/{long}/lat/{lat}/radius/{radius}")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter for long or lat or radius", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun getAllAreasWithinRadius(@PathVariable(value = "long") long: Double, @PathVariable(value = "lat") lat: Double, @PathVariable(value = "radius") radius: BigDecimal): ResponseEntity<List<TherapistAreaDTO>> {
        return ResponseEntity.ok(jpaTherapistAreaService.retrieveAreasWithinRadius(long, lat, radius).map { therapistEntity -> therapistEntity.toDto()})
    }

    @GetMapping("/therapist-areas/{areaId}")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter for long or lat or radius", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class),
        ApiResponse(code = 404, message = "AreaId don't exists", response = ErrorResponse::class)
    ])
    fun getTherapistAreaById(@PathVariable(value = "areaId") areaId: Long): ResponseEntity<TherapistAreaDTO> {
        return ResponseEntity.ok(jpaTherapistAreaService.retrieveArea(areaId).toDto())
    }

    @PostMapping("/therapist-areas")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter: therapistAreaBodyCreate is invalid", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun createNewTherapistArea(@Valid @RequestBody therapistAreaBodyCreate: CreateTherapistAreaDTO): ResponseEntity<TherapistAreaDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(jpaTherapistAreaService.addArea(TherapistAreaEntity.fromDto(therapistAreaBodyCreate)).toDto())
    }

    @PutMapping("/therapist-areas/{areaId}")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter: therapistAreaBodyUpdate is invalid", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class),
        ApiResponse(code = 404, message = "areaId don't exists", response = ErrorResponse::class)
    ])
    fun updateTherapistAreaById(@PathVariable(value = "areaId") areaId: Long,
                            @Valid @RequestBody therapistAreaBodyUpdate: UpdateTherapistAreaDTO): ResponseEntity<TherapistAreaDTO> {
        val updateTherapistAreaEntity = TherapistAreaEntity.fromDto(therapistAreaBodyUpdate)
        val updatedArea = jpaTherapistAreaService.retrieveArea(areaId).copy(
                name = updateTherapistAreaEntity.name,
                location = updateTherapistAreaEntity.location,
                radius = updateTherapistAreaEntity.radius
        )
        return ResponseEntity.ok(jpaTherapistAreaService.updateArea(areaId, updatedArea).toDto())
    }

    @DeleteMapping("/therapist-areas/{areaId}")
    @ApiResponses(value = [
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class),
        ApiResponse(code = 404, message = "areaId don't exists", response = ErrorResponse::class)
    ])
    fun deleteTherapistById(@PathVariable(value = "areaId") areaId: Long): ResponseEntity<Void> {
        jpaTherapistAreaService.deleteArea(areaId)
        return ResponseEntity.noContent().build()
    }

}