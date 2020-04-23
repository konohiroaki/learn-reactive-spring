package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.expectError
import reactor.test.StepVerifier

class BasicTest {

    @Test
    fun flux() {
        val strFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        strFlux.subscribe { println(it) }
    }

    @Test
    fun fluxErr() {
        val strFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(RuntimeException("Error Occurred")))
                .concatWith(Flux.just("After error")) // you don't receive after error.
                .log() //shows event log
        strFlux.subscribe(
                { println(it) },
                { System.err.println(it) }, // error consumer. only executed on onError()
                { println("Completed") } // complete consumer. only executed on onComplete()
        )
    }

    @Test
    fun fluxTestElements() {
        val strFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log()
        StepVerifier.create(strFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete()
    }

    @Test
    fun fluxErrTestElements() {
        val strFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(RuntimeException("Error Occurred")))
                .log()
        StepVerifier.create(strFlux)
                .expectNext("Spring", "Spring Boot", "Reactive Spring")
                .expectErrorMessage("Error Occurred")
                .verify()
    }

    @Test
    fun fluxElementCount() {
        val strFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(RuntimeException("Error Occurred")))
                .log()
        StepVerifier.create(strFlux)
                .expectNextCount(3)
                .expectError(RuntimeException::class)
                .verify()
    }

    @Test
    fun mono() {
        val strMono = Mono.just("Spring")
        StepVerifier.create(strMono.log())
                .expectNext("Spring")
                .verifyComplete()
    }

    @Test
    fun monoErr() {
        StepVerifier.create(Mono.error<RuntimeException>(RuntimeException("Exception Occurred")).log())
                .expectError(RuntimeException::class)
                .verify()
    }
}
