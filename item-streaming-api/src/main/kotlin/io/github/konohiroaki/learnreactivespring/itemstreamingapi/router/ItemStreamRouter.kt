package io.github.konohiroaki.learnreactivespring.itemstreamingapi.router

import io.github.konohiroaki.learnreactivespring.itemstreamingapi.handler.ItemStreamHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class ItemStreamRouter(val handler: ItemStreamHandler) {
    @Bean
    fun itemStreamRoute() = router {
        GET("/v2/items").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::getItemsStream)
    }
}
