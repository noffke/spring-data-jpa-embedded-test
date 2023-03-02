package com.example.embeddedtest.springdatajpa

import com.example.embeddedtest.EmbeddedTestApplication
import jakarta.persistence.*
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@Embeddable
internal data class AnEmbeddableSpringDataJpa(@Column(nullable = false) val foo: String, @Column(nullable = false) val bar: String)

internal interface HasEmbeddableSpringDataJpa{
    val e: AnEmbeddableSpringDataJpa
}

@Entity
internal class WithEmbeddedSpringDataJpa(@Id val id: Long, @Embedded override val e: AnEmbeddableSpringDataJpa): HasEmbeddableSpringDataJpa

@NoRepositoryBean
internal interface HasEmbeddableRepository<T: HasEmbeddableSpringDataJpa, ID, in AnEmbeddable> : JpaRepository<T, ID> {
    fun findByE(e: AnEmbeddable): List<WithEmbeddedSpringDataJpa>
}

internal interface WithEmbeddedRepository: HasEmbeddableRepository<WithEmbeddedSpringDataJpa, Long, AnEmbeddableSpringDataJpa>

@SpringBootTest(
    classes = [EmbeddedTestApplication::class],
    properties = [
        "spring.datasource.url=jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create",
    ]
)
class SpringDataJpaTest {
    @Autowired
    private lateinit var repository: WithEmbeddedRepository

    @Test
    fun `spring data jpa`() {
        assertThatCode { (repository.findByE(AnEmbeddableSpringDataJpa("a", "b"))) }.doesNotThrowAnyException()
    }
}
