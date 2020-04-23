package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration

class ColdHotPublisherTest {

    @Test
    fun coldPublisher() {
        val strFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1))
        strFlux.subscribe { println("Subscriber 1: $it") } // A B C D E F
        Thread.sleep(2000)
        strFlux.subscribe { println("Subscriber 2: $it") } // A B C D
        Thread.sleep(4000)
        // for each subscriber, flux will emit values from beginning. this is called cold publisher.
        // all publisher we used until now is cold publisher.
    }

    @Test
    fun hotPublisher() {
        val strFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1))
        val connectableFlux = strFlux.publish()
        connectableFlux.connect()
        connectableFlux.subscribe { println("Subscriber 1: $it") } // A B C D E F
        Thread.sleep(2000)
        connectableFlux.subscribe { println("Subscriber 2: $it") } // C D E F
        Thread.sleep(4000)
    }
}
