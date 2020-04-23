package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class TimeTest {

    @Test
    fun interval() {
        val infinite = Flux.interval(Duration.ofMillis(500)).log()
        infinite.subscribe { println(it) }

        Thread.sleep(3000) // need to keep main thread to see above execution
    }

    @Test
    fun intervalTake() {
        val finiteFlux = Flux.interval(Duration.ofMillis(500)).take(3).log()
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2) // receive onNext each 500ms
                .verifyComplete()
    }

    @Test
    fun intervalDelay() {
        val finiteFlux = Flux.interval(Duration.ofMillis(500))
                .delayElements(Duration.ofSeconds(1))
                .take(3)
                .log()
        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2) // receive onNext each 1 second (NOT 1.5s)
                .verifyComplete()
    }
}
