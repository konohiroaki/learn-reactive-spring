package io.github.konohiroaki.learnreactivespring.itemapi.handler

import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Component
class FunctionalErrorWebExceptionHandler(
        errorAttributes: ErrorAttributes?,
        applicationContext: ApplicationContext,
        serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(
        errorAttributes,
        ResourceProperties(),
        applicationContext
) {

    init {
        this.setMessageWriters(serverCodecConfigurer.writers)
        this.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?) = router {
        RequestPredicates.all().invoke(::renderErrorResponse)
    }

    private fun renderErrorResponse(serverRequest: ServerRequest): Mono<ServerResponse> {
        val errorAttrMap = getErrorAttributes(serverRequest, false)
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(errorAttrMap["message"]!!)
    }
}
