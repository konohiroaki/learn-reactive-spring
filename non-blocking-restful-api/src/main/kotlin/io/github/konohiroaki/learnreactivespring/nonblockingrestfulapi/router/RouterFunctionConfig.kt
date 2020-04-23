package io.github.konohiroaki.learnreactivespring.nonblockingrestfulapi.router

import io.github.konohiroaki.learnreactivespring.nonblockingrestfulapi.handler.SampleHandlerFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterFunctionConfig {

    @Bean
    fun routes(handler: SampleHandlerFunction) = router {
        GET("/functional/flux", handler::flux)
        GET("/functional/mono", handler::mono)
    }
}
