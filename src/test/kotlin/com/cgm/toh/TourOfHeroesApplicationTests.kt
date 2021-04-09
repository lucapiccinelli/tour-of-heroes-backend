package com.cgm.toh

import com.cgm.toh.route.dto.Hero
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	classes = [TourOfHeroesApplication::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TourOfHeroesApplicationTests(
	@Autowired private val restTemplate: TestRestTemplate) {

	val initialHeroes = listOf(Hero(1, "pippo"), Hero(2, "pluto"))

	@BeforeAll
	fun setup(){
		initialHeroes.forEach(::createHero)
	}

	private fun createHero(hero: Hero) {
		restTemplate.postForEntity<Hero>("/app/heroes", HttpEntity(hero))
	}

	@Test
	fun `should be possible to reach the endpoint`() {
		val heroesResult = restTemplate.exchange("/app/heroes", HttpMethod.GET, null, typeReference<List<Hero>>())
		assertThat(heroesResult.statusCode).isEqualTo(HttpStatus.OK)

		val expected = initialHeroes
		assertThat(heroesResult.body).isEqualTo(expected)
	}

	@Test
	fun `should be possible to get only one hero`() {
		val heroesResult = restTemplate.getForEntity<Hero>("/app/heroes/1")
		assertThat(heroesResult.statusCode).isEqualTo(HttpStatus.OK)

		val expected = Hero(1, "pippo")
		assertThat(heroesResult.body).isEqualTo(expected)
	}

	@Test
	fun `should be possible to save a new hero`() {
		val newHero = Hero(0, "bla")
		val heroesResult = restTemplate.postForEntity<Hero>("/app/heroes", HttpEntity(newHero))
		assertThat(heroesResult.statusCode).isEqualTo(HttpStatus.CREATED)

		val newHeroResult = heroesResult.body!!

		assertThat(newHeroResult.name).isEqualTo(newHero.name)

		val readResult = restTemplate.getForEntity<Hero>("/app/heroes/${newHeroResult.id}")
		assertThat(readResult.statusCode).isEqualTo(HttpStatus.OK)
	}

	@Test
	fun `should be possible to delete a hero`() {
		val newHero = Hero(0, "bla")
		val heroesResult = restTemplate.postForEntity<Hero>("/app/heroes", HttpEntity(newHero))
		assertThat(heroesResult.statusCode).isEqualTo(HttpStatus.CREATED)
		val newHeroResult = heroesResult.body!!

		restTemplate.delete("/app/heroes/${newHeroResult.id}")

		val readResult = restTemplate.getForEntity<Hero>("/app/heroes/${newHeroResult.id}")
		assertThat(readResult.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
	}

}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}