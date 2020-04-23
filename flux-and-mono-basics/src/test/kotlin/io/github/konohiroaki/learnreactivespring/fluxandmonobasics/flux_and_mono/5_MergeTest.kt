package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration
import java.util.function.BiFunction

class MergeTest {

    @Test
    fun merge() {
        val f1 = Flux.just("A", "B", "C")
        val f2 = Flux.just("D", "E", "F")

        StepVerifier.create(Flux.merge(f1, f2).log()) // order is not guaranteed
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete()
    }

    @Test
    fun mergeWithDelay() {
        val f1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
        val f2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))

        StepVerifier.create(Flux.merge(f1, f2).log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete()
    }

    @Test
    fun concat() {
        val f1 = Flux.just("A", "B", "C")
        val f2 = Flux.just("D", "E", "F")

        StepVerifier.create(Flux.concat(f1, f2).log()) // order is guaranteed
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete()
    }

    @Test
    fun concatWithDelay() {
        val f1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
        val f2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))

        StepVerifier.create(Flux.concat(f1, f2).log()) // order is guaranteed
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete()
    }

    @Test
    fun zip() {
        val f1 = Flux.just("A", "B", "C")
        val f2 = Flux.just("D", "E", "F")

        val biFun = BiFunction<String, String, String> { t1, t2 -> "$t1+$t2" }
        val zip = Flux.zip(f1, f2, biFun)

        StepVerifier.create(zip.log())
                .expectNext("A+D", "B+E", "C+F")
                .verifyComplete()
    }
}
