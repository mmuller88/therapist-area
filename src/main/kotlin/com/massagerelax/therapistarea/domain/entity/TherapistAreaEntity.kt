package com.massagerelax.therapistarea.domain.entity

import org.locationtech.jts.geom.Geometry
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "TherapistArea")
data class TherapistAreaEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @NotNull
        @get: NotBlank
        @Size(max = 100)
        val name: String = "",

//        @Column(columnDefinition = "geometry(Point,4326)")
        val geom: Geometry,

        @NotNull
        @Max(10)
        val radius: Double = 0.0
)