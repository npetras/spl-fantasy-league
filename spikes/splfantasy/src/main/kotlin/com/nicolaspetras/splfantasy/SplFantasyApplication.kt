package com.nicolaspetras.splfantasy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class SplFantasyApplication

fun main(args: Array<String>) {
    runApplication<SplFantasyApplication>(*args)
}

@Bean
fun corsConfigurer(): WebMvcConfigurer? {
    return object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/weekMatchScores").allowedOrigins("*")
        }
    }
}