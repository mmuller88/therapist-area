package com.massagerelax.therapistarea.web.controller

import com.massagerelax.therapistarea.web.support.ErrorResponse
import com.massagerelax.therapistarea.domain.module.JpaTherapistAreaService
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api")
class TherapistController(private val jpaTherapistAreaService: JpaTherapistAreaService){

    @GetMapping("/alive")
    @ApiResponses(value = [
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun alive(): ResponseEntity<String> {
        return ResponseEntity.ok("Alive")
    }

    @GetMapping("/therapists/long/{long}/lat/{lat}")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter for long or lat or radius", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun getAllTherapistsWithinTherapistRadius(@PathVariable(value = "long") long: Double, @PathVariable(value = "lat") lat: Double): ResponseEntity<List<String>> {
        return ResponseEntity.ok(jpaTherapistAreaService.retrieveTherapistsWithinTherapistRadius(long, lat))
    }

    @GetMapping("/therapists/long/{long}/lat/{lat}/n/{n}")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "Invalid parameter for long or lat or n", response = ErrorResponse::class),
        ApiResponse(code = 401, message = "Authentication failed", response = ErrorResponse::class)
    ])
    fun getNnearestTherapists(@PathVariable(value = "long") long: Double, @PathVariable(value = "lat") lat: Double, @PathVariable(value = "n") n: Int): ResponseEntity<List<String>> {
        return ResponseEntity.ok(jpaTherapistAreaService.retrieveNnearestTherapists(long, lat, n))
    }

}