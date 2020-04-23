package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers.parallel
import reactor.test.StepVerifier

class MapTest {

    private val names = listOf("adam", "anna", "jack", "jenny")

    @Test
    fun map() {
        val namesFlux = Flux.fromIterable(names)
                .log()
                .map { it.toUpperCase() }
                .log()
        StepVerifier.create(namesFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete()
    }

    @Test
    fun repeat() {
        val lengthRepeatedFlux = Flux.fromIterable(names)
                .map { it.length }
                .repeat(1)
        StepVerifier.create(lengthRepeatedFlux)
                .expectNext(4, 4, 4, 5, 4, 4, 4, 5)
                .verifyComplete()
    }

    @Test
    fun flatMap() {
        val namesFlux = Flux.fromIterable(names)
                .flatMap { Flux.fromIterable(fetchValues(it)) }
                .log()
        StepVerifier.create(namesFlux)
                .expectNextCount(8)
                .verifyComplete()
    }

    private fun fetchValues(s: String): List<String> {
        Thread.sleep(1000)
        return listOf("A$s", "B$s")
    }

    @Test
    fun flatMapWithParallel() {
        val namesFlux = Flux.fromIterable(names)
                .window(2)
                .flatMap { s -> s.map { fetchValues(it) }.subscribeOn(parallel()) }
                .flatMap { Flux.fromIterable(it) }
                .log()
        StepVerifier.create(namesFlux)
                .expectNextCount(8)
                .verifyComplete()
    }

    @Test
    fun flatMapWithParallelKeepOrder() {
        val namesFlux = Flux.fromIterable(names)
                .window(2)
                .flatMapSequential { s -> s.map { fetchValues(it) }.subscribeOn(parallel()) }
                .flatMap { Flux.fromIterable(it) }
                .log()
        StepVerifier.create(namesFlux)
                .expectNextCount(8)
                .verifyComplete()
    }
}
