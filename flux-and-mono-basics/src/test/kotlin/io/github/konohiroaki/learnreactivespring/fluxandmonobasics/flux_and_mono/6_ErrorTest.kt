package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.kotlin.test.expectError
import reactor.test.StepVerifier
import reactor.util.retry.Retry
import java.time.Duration

class ErrorTest {

    @Test
    fun errorResume() {
        val strFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume {
                    println("Exception is $it")
                    Flux.just("ERROR")
                }

        StepVerifier.create(strFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "ERROR")
                .verifyComplete()
    }

    @Test
    fun errorReturn() {
        val strFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("ERROR")

        StepVerifier.create(strFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "ERROR")
                .verifyComplete()
    }

    @Test
    fun errorMap() {
        val strFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap { IllegalStateException(it) }

        StepVerifier.create(strFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException::class)
                .verify()
    }

    @Test
    fun errorMapRetry() {
        val strFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap { IllegalStateException(it) }
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2)))

        StepVerifier.create(strFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                // in real situation, retry may resolve error
                .expectError(IllegalStateException::class)
                .verify()
    }
}
