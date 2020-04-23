package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class FactoryTest {

    private val names = listOf("adam", "anna", "jack", "jenny")

    @Test
    fun fluxFromIterable() {
        val namesFlux = Flux.fromIterable(names)
        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete()
    }

    @Test
    fun fluxFromArray() {
        val namesFlux = Flux.fromArray(names.toTypedArray())
        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete()
    }

    @Test
    fun fluxFromRange() {
        StepVerifier.create(Flux.range(1, 5).log())
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete()
    }

    @Test
    fun monoFromJustOrEmpty() {
        val nullMono = Mono.justOrEmpty<Any>(null)
        StepVerifier.create(nullMono)
                .verifyComplete()
    }

    @Test
    fun monoFromSupplier() {
        val supplier = { "adam" }
        StepVerifier.create(Mono.fromSupplier(supplier).log())
                .expectNext("adam")
                .verifyComplete()
    }

}
