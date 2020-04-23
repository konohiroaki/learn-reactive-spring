package io.github.konohiroaki.learnreactivespring.itemapi.router

import io.github.konohiroaki.learnreactivespring.itemapi.handler.ItemHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class ItemRouter(val handler: ItemHandler) {

    @Bean
    fun route() = router {
        GET("/v2/items").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::getAllItems)
        GET("/v2/items/{id}").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::getItemByID)
        POST("/v2/items").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::createItem)
        DELETE("/v2/items/{id}").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::deleteItem)
        PUT("/v2/items/{id}").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::updateItem)
    }

    @Bean
    fun errorRoute() = router {
        GET("/v2/error").and(accept(MediaType.APPLICATION_JSON)).invoke(handler::error)
    }
}
