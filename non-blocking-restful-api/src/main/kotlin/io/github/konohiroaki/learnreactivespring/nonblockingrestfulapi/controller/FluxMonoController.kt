package io.github.konohiroaki.learnreactivespring.nonblockingrestfulapi.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

// traditional way. check router/handler package for reactive way.
@RestController
class FluxMonoController {

    // browser waits for 4 seconds
    @GetMapping("/flux")
    fun flux(): Flux<Int> {
        return Flux.just(1, 2, 3, 4).delayElements(Duration.ofSeconds(1)).log()
    }

    // browser will show the result one by one
    @GetMapping("/fluxstream", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun fluxStream(): Flux<Long> {
        return Flux.interval(Duration.ofSeconds(1)).log()
    }

    @GetMapping("/mono")
    fun mono(): Mono<Int> {
        return Mono.just(1).log()
    }
}
