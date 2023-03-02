package com.example.embeddedtest

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean

//import javax.persistence.Column
//import javax.persistence.Embeddable
//import javax.persistence.Embedded
//import javax.persistence.Entity
//import javax.persistence.Id

@SpringBootApplication
class EmbeddedTestApplication

fun main(args: Array<String>) {
    runApplication<EmbeddedTestApplication>(*args)
}

@Embeddable
data class AnEmbeddable(@Column(nullable = false) val foo: String, @Column(nullable = false) val bar: String)

interface HasEmbeddable{
    val e: AnEmbeddable
}

@Entity
class WithEmbedded(@Id val id: Long, @Embedded override val e: AnEmbeddable): HasEmbeddable

@NoRepositoryBean
interface HasEmbeddableRepository<T: HasEmbeddable, ID, in AnEmbeddable> : JpaRepository<T, ID>{
    fun findByE(e: AnEmbeddable): List<WithEmbedded>
}

interface WithEmbeddedRepository: HasEmbeddableRepository<WithEmbedded, Long, AnEmbeddable>