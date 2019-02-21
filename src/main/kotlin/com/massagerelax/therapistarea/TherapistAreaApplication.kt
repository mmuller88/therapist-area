package com.massagerelax.therapistarea

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.ParseException
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.locationtech.jts.io.WKTReader
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal


@SpringBootApplication
class TherapistAreaApplication{

	@Autowired
	lateinit var repository: TherapistAreaRepository

	@Bean
	fun databaseInitializer() = CommandLineRunner {
		repository.deleteAllInBatch()
		var tarea1 = TherapistAreaEntity(name = "Kenia Alves", location = wktToGeometry("POINT(-105 40)"), radius = BigDecimal(5.0))
		var tarea11 = TherapistAreaEntity(name = "Kenia Alves", location = wktToGeometry("POINT(-120 40)"), radius = BigDecimal(5.0))
		var tarea2 = TherapistAreaEntity(name = "Martin Mueller", location = wktToGeometry("POINT(-111 40)"), radius = BigDecimal(5.0))
		var tarea21 = TherapistAreaEntity(name = "Martin Mueller", location = wktToGeometry("POINT(-111 40)"), radius = BigDecimal(10.0))

		val tarea1Result = repository.save(tarea1)
		val tarea11Result = repository.save(tarea11)
		val tarea2Result = repository.save(tarea2)
		val tarea21Result = repository.save(tarea21)

		// fetch all customers
		println("Customers found with findAll():")
		println("-------------------------------")
		repository.findAll().forEach(::println)
		println()
//
//		// // fetch an individual customer by ID
//		val fetchArea1 = repository.findById(tarea1Result.id).get()
//		println("fetchArea1 found with findById(1L):")
//		println("--------------------------------")
//		println(fetchArea1)
//		println()
//
//		println("Therapists found within POLYGON((-107 39, -102 39, -102 41, -107 41, -107 39)):")
//		println("--------------------------------")
//		repository.findWithin(wktToGeometry("POLYGON((-107 39, -102 39, -102 41, -107 41, -107 39))"))
//				.forEach(::println)
//
//		println("Therapists with their radius found within POINT(-105 40):")
//		println("--------------------------------")
//		repository.findWithinTherapistRadius(wktToGeometry("POINT(-105 40)")).forEach(::println)
//
//		println("Therapists found within POINT(-105 40) and radius :")
//		println("--------------------------------")
//		repository.findWithinRadius(wktToGeometry("POINT(-105 40)"), 7.0).forEach(::println)

	}

	private fun wktToGeometry(wktPoint: String): Geometry {
		val fromText = WKTReader()
		var geom: Geometry
		try {
			geom = fromText.read(wktPoint)
		} catch (e: ParseException) {
			throw RuntimeException("Not a WKT string:$wktPoint")
		}
		geom.geometryType

		return geom
	}
}


fun main(args: Array<String>) {
	runApplication<TherapistAreaApplication>(*args)
}

