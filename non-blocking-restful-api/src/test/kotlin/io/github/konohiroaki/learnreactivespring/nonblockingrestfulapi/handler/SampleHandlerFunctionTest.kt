package io.github.konohiroaki.learnreactivespring.nonblockingrestfulapi.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@SpringBootTest
@AutoConfigureWebTestClient
class SampleHandlerFunctionTest {

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun functionalFlux() {
        val intFlux = client.get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_STREAM_JSON)
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
    fun functionalMono() {
        val responseBody = client.get().uri("/functional/mono")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(Int::class.java)
                .returnResult().responseBody

        assertEquals(1, responseBody)
    }
}
