package com.example.embeddedtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@EntityScan(basePackages = ["com.example"])
@SpringBootApplication
class EmbeddedTestApplication

fun main(args: Array<String>) {
    runApplication<EmbeddedTestApplication>(*args)
}