package com.cgm.toh

import com.cgm.toh.route.dto.Hero
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@SpringBootApplication
class TourOfHeroesApplication

fun main(args: Array<String>) {
	runApplication<TourOfHeroesApplication>(*args)
}

@RestController
@RequestMapping("/app")
class HeroesController{

	private val heroesRepo: MutableMap<Int, Hero> = HashMap()

	@GetMapping(
		value = ["/heroes"],
		produces = ["application/json"])
	fun getHeroes(): ResponseEntity<List<Hero>> =
		ResponseEntity.ok().body(heroesRepo.values.toList())


	@GetMapping(
		value = ["/heroes/{id}"],
		produces = ["application/json"])
	fun getHero(@PathVariable id: Int): ResponseEntity<Hero> =
		heroesRepo[id]
			?.let { ResponseEntity.ok().body(it) }
			?: ResponseEntity.notFound().build()


	@PostMapping(
		value = ["/heroes"],
		produces = ["application/json"],
		consumes = ["application/json"]
	)
	fun newHero(@RequestBody hero: Hero): ResponseEntity<Hero> {
		val currentId = heroesRepo.keys
			.maxOrNull()
			?: 0
		val idToAssign = currentId + 1

		val newHero = hero.copy(id = idToAssign)
		heroesRepo[idToAssign] = newHero

		return ResponseEntity
			.created(URI("/heroes/${newHero.id}"))
			.body(newHero)
	}

	@DeleteMapping(value = ["/heroes/{id}"])
	fun deleteHero(@PathVariable("id") id: Int): ResponseEntity<Unit> =
		heroesRepo.remove(id)
			?.let { ResponseEntity.ok(Unit) }
			?: ResponseEntity.notFound().build()
}