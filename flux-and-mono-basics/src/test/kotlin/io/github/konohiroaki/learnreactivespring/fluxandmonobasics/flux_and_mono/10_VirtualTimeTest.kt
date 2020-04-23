package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.test.scheduler.VirtualTimeScheduler
import java.time.Duration

class VirtualTimeTest {

    @Test
    fun testWithoutVirtualTime() {
        val longFlux = Flux.interval(Duration.ofSeconds(1)).take(3).log()

        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete()
        // it takes 3 seconds!
    }

    @Test
    fun testWithVirtualTime() {
        VirtualTimeScheduler.getOrSet()

        val longFlux = Flux.interval(Duration.ofSeconds(1)).take(3).log()

        StepVerifier.withVirtualTime { longFlux }
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0, 1, 2)
                .verifyComplete()
        // it finishes immediately
    }

    @Test // same test from MergeTest
    fun concatWithDelay() {
        VirtualTimeScheduler.getOrSet()

        val f1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
        val f2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))

        StepVerifier.withVirtualTime { Flux.concat(f1, f2).log() }
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete()
    }
}
