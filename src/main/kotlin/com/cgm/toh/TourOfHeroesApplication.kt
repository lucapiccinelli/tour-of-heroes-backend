package com.cgm.toh

import com.cgm.toh.route.dto.Hero
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class TourOfHeroesApplication

fun main(args: Array<String>) {
	runApplication<TourOfHeroesApplication>(*args)
}

@RestController
@RequestMapping("/app")
class HeroesController{

	@GetMapping(
		value = ["/heroes"],
		produces = ["application/json"])
	fun getHeroes(): ResponseEntity<List<Hero>> =
		ResponseEntity.ok().body(listOf(Hero(1, "pippo"), Hero(2, "pluto")))


	@GetMapping(
		value = ["/heroes/{id}"],
		produces = ["application/json"])
	fun getHero(@PathVariable("id") id: Int): ResponseEntity<Hero> =
		ResponseEntity.ok().body(Hero(1, "pippo"))

}