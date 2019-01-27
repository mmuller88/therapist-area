package com.massagerelax.therapistarea

import com.massagerelax.therapistarea.domain.entity.TherapistAreaEntity
import com.massagerelax.therapistarea.domain.repository.TherapistAreaRepository
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.ParseException
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.locationtech.jts.io.WKTReader
import org.springframework.beans.factory.annotation.Autowired


@SpringBootApplication
class TherapistAreaApplication{

	@Autowired
	lateinit var repository: TherapistAreaRepository

	@Bean
	fun databaseInitializer() = CommandLineRunner {
		repository.deleteAllInBatch()
		val tarea1 = TherapistAreaEntity(name = "Kenia Alves", geom = wktToGeometry("POINT(-105 40)"))
		val tarea2 = TherapistAreaEntity(name = "Martin Mueller", geom = wktToGeometry("POINT(-108 40)"))

		val tarea1Result = repository.save(tarea1)

		// fetch all customers
		println("Customers found with findAll():")
		println("-------------------------------")
		repository.findAll().forEach(::println)
		println()

		// // fetch an individual customer by ID
		val fetchArea1 = repository.findById(tarea1Result.id).get()
		println("fetchArea1 found with findById(1L):")
		println("--------------------------------")
		println(fetchArea1)
		println()

		println("Customers found within POLYGON((-107 39, -102 39, -102 41, -107 41, -107 39)):")
		println("--------------------------------")
		repository.findWithin(wktToGeometry("POLYGON((-107 39, -102 39, -102 41, -107 41, -107 39))"))
				.forEach(::println)

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



fun main(args: Array<String>) {
	runApplication<TherapistAreaApplication>(*args)
}

