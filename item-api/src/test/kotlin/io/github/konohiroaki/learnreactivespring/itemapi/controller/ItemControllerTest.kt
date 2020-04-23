package io.github.konohiroaki.learnreactivespring.itemapi.controller

import io.github.konohiroaki.learnreactivespring.itemapi.document.Item
import io.github.konohiroaki.learnreactivespring.itemapi.repository.ItemReactiveRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    lateinit var repository: ItemReactiveRepository

    val items = listOf(
            Item(null, "Samsung TV", 400.0),
            Item(null, "LG TV", 420.0),
            Item(null, "Apple TV", 250.0),
            Item("ABC", "Kindle", 90.0)
    )

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap { repository.save(it) }
                .doOnNext { println("Inserted: $it") }
                .blockLast() // wait until all are inserted so tests will have the data
    }

    @Test
    fun getAllItems() {
        client.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item::class.java)
                .hasSize(4)
    }

    @Test
    fun getAllItems2() {
        val itemFlux = client.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item::class.java)
                .responseBody

        StepVerifier.create(itemFlux.log())
                .expectNextCount(4)
                .verifyComplete()
    }

    @Test
    fun getItemByID() {
        client.get().uri("/v1/items/{id}", "ABC")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.price").isEqualTo(90.0)
    }

    @Test
    fun getItemByID_notFound() {
        client.get().uri("/v1/items/{id}", "DEF")
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun createItem() {
        val item = Item(null, "iPhone X", 999.99)
        client.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item::class.java)
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.id").isNotEmpty
                .jsonPath("$.desc").isEqualTo("iPhone X")
                .jsonPath("$.price").isEqualTo(999.99)
    }

    @Test
    fun deleteItem() {
        client.delete().uri("/v1/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(Void::class.java)
    }

    @Test
    fun updateItem() {
        client.put().uri("/v1/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(Item(null, "Kindle", 85.0)), Item::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.price").isEqualTo(85.0)
    }

    @Test
    fun updateItem_notFound() {
        client.put().uri("/v1/items/{id}", "DEF")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(Item(null, "Kindle", 85.0)), Item::class.java)
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun error() {
        client.get().uri("/v1/error")
                .exchange()
                .expectStatus().is5xxServerError
                .expectBody<String>().isEqualTo("Exception occurred.")
    }
}
