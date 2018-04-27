package io.agilehandy.springbootkotlin

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.*
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
class SpringBootKotlinApplication

fun main(args: Array<String>) {
    runApplication<SpringBootKotlinApplication>(*args)
}

@Configuration
class RunnerConfiguration(val repository: PersonRepository) {

    @Bean
    fun clr(): CommandLineRunner {
        val persons = listOf<Person>(
                 Person(null, "Haytham", "Mohamed", 30)
                ,Person(null, "Eyman", "Ahmad", 29)
                ,Person(null, "Ahmed", "Mohamed", 16)
        )

        return CommandLineRunner {
            persons.forEach { repository.save(it) }
            repository.findAll().forEach { println(it) }
        }
    }
}

@javax.persistence.Entity
data class Person(@Id @GeneratedValue var Id: Long? = null
                  ,val firstName: String = ""
                  ,val lastName: String = ""
                  ,val age: Int? = null)

interface PersonRepository : CrudRepository<Person, Long>

@RestController
class PersonController(val repository: PersonRepository) {

    @GetMapping("/")
     fun hello(): String {
        return "Hello Haytham!"
    }

    @GetMapping("/persons")
    fun persons() : Iterable<Person> {
        return repository.findAll();
    }

    @GetMapping("/persons/{id}")
    fun personById(@PathVariable id: Long) : Person {
        return repository.findById(id).orElseThrow( { RuntimeException("No prerson found!")} )
    }

    @PostMapping("/persons")
    fun addPerson(@RequestBody person: Person): Long? {
        val saved = repository.save(person)
        return saved.Id;
    }

}