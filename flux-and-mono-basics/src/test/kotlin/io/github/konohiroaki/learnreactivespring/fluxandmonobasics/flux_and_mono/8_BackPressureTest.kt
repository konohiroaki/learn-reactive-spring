package io.github.konohiroaki.learnreactivespring.fluxandmonobasics.flux_and_mono

import org.junit.jupiter.api.Test
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux

class BackPressureTest {

    @Test
    fun backPressure() {
        Flux.range(1, 8).log().subscribe(
                { println(it) }, // onNext
                { println(it) }, // onError
                { println("Done") }, // onComplete
                { it.request(2) }
        ) // -> 1, 2 (No onComplete!)
        // if it.request(8), it will produce onComplete
    }

    @Test
    fun backPressureCancel() {
        Flux.range(1, 8).log().subscribe(
                { println(it) },
                { println(it) },
                { println("Done") },
                { it.cancel() }
        ) // -> immediately calls cancel
    }

    @Test
    fun backPressureCustom() {
        Flux.range(1, 8).log().subscribe(object : BaseSubscriber<Int>() {
            override fun hookOnNext(value: Int) {
                request(1)
                println(value)
                if (value == 4) cancel()
            }
        })
    }
}
