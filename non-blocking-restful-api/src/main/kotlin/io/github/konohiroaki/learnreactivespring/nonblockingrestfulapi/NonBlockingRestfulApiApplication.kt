package io.github.konohiroaki.learnreactivespring.nonblockingrestfulapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NonBlockingRestfulApiApplication

fun main(args: Array<String>) {
    runApplication<NonBlockingRestfulApiApplication>(*args)
}
