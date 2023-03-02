package com.example.embeddedtest

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [EmbeddedTestApplication::class],
    properties = [
        "spring.datasource.url=jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create",
        "spring.flyway.enabled=false",
    ]
)
class EmbeddedTestApplicationTests {

    @Autowired
    private lateinit var repository: WithEmbeddedRepository

    @Test
    fun contextLoads() {
        val embeddable = AnEmbeddable("a", "b")
        repository.saveAndFlush(WithEmbedded(0, embeddable))

        assertThat(repository.findByE(embeddable).map { it.id }).contains(0L)
    }
}
