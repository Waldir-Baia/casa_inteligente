package com.casainteligente.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
