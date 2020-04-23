package io.github.konohiroaki.learnreactivespring.nonblockingrestfulapi.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@WebFluxTest
internal class FluxMonoControllerTest {

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun flux_approach1() {
        val intFlux = client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .returnResult(Int::class.java)
                .responseBody

        StepVerifier.create(intFlux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete()

    }

    @Test
    fun flux_approach2() {
        client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Int::class.java)
                .hasSize(4)
    }

    @Test
    fun flux_approach3() {
        val expected = listOf(1, 2, 3, 4)

        val result = client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Int::class.java)
                .returnResult()

        assertEquals(expected, result.responseBody)
    }

    @Test
    fun flux_approach4() {
        client.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList(Int::class.java)
                .consumeWith<WebTestClient.ListBodySpec<Int>> {
                    assertEquals(listOf(1, 2, 3, 4), it.responseBody)
                }
    }

    @Test
    fun fluxStream() {
        val responseBody = client.get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .returnResult(Long::class.java)
                .responseBody

        StepVerifier.create(responseBody)
                .expectNext(0, 1, 2)
                .thenCancel()
                .verify()
    }

    @Test
    fun mono() {
        val responseBody = client.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(Int::class.java)
                .returnResult().responseBody

        assertEquals(1, responseBody)
    }
}
