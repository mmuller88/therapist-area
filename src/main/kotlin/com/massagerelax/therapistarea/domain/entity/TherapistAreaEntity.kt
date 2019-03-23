package com.massagerelax.therapistarea.domain.entity

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.massagerelax.therapistarea.web.dto.CreateTherapistAreaDTO
import com.massagerelax.therapistarea.web.dto.TherapistAreaDTO
import com.massagerelax.therapistarea.web.dto.UpdateTherapistAreaDTO
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "therapistarea")
data class TherapistAreaEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotNull
        @get: NotBlank
        @field:Size(max = 100)
        val name: String = "",

        @JsonSerialize(using = GeometrySerializer::class)
        @JsonDeserialize(using = GeometryDeserializer::class)
        @Column(name = "location", columnDefinition = "Geometry")
        val location: Geometry,

        @field:NotNull
        @field:DecimalMin(value = "0.5", inclusive = true)
        @field:DecimalMax(value = "10.0", inclusive = true)
        val radius: BigDecimal = BigDecimal(0.0)){

        fun toDto() = TherapistAreaDTO(
                id = this.id,
                name = this.name,
                long = this.location.coordinate.x,
                lat = this.location.coordinate.y,
                radius = this.radius
        )

        companion object {
                fun fromDto(dto: CreateTherapistAreaDTO): TherapistAreaEntity {
                        val gf = GeometryFactory()
                        val point = gf.createPoint(Coordinate(dto.long, dto.lat))

                        return TherapistAreaEntity(name = dto.name, location = point, radius = dto.radius)
                }

                fun fromDto(dto: UpdateTherapistAreaDTO): TherapistAreaEntity {
                        val gf = GeometryFactory()
                        val point = gf.createPoint(Coordinate(dto.long, dto.lat))

                        return TherapistAreaEntity(name = dto.name, location = point, radius = dto.radius)
                }

        }
}