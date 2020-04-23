package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class FilterTest {

    private val names = listOf("adam", "anna", "jack", "jenny")

    @Test
    fun filter() {
        val filterFlux = Flux.fromIterable(names)
                .log()
                .filter { it.startsWith("a") }
                .log()

        StepVerifier.create(filterFlux)
                .expectNext("adam", "anna")
                .verifyComplete()
    }
}
