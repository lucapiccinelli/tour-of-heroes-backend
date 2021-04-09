package com.cgm.toh

import com.cgm.toh.route.dto.Hero
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	classes = [TourOfHeroesApplication::class])
class TourOfHeroesApplicationTests(
	@Autowired private val restTemplate: TestRestTemplate) {

	@Test
	fun `should be possible to reach the endpoint`() {
		val heroesResult = restTemplate.exchange("/app/heroes", HttpMethod.GET, null, typeReference<List<Hero>>())
		assertThat(heroesResult.statusCode).isEqualTo(HttpStatus.OK)

		val expected = listOf(Hero(1, "pippo"), Hero(2, "pluto"))
		assertThat(heroesResult.body).isEqualTo(expected)
	}

	@Test
	fun `should be possible to get only one hero`() {
		val heroesResult = restTemplate.getForEntity<Hero>("/app/heroes/1")
		assertThat(heroesResult.statusCode).isEqualTo(HttpStatus.OK)

		val expected = Hero(1, "pippo")
		assertThat(heroesResult.body).isEqualTo(expected)
	}

}

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}