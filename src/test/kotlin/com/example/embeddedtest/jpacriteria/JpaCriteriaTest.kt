package com.example.embeddedtest.jpacriteria

import com.example.embeddedtest.EmbeddedTestApplication
import jakarta.persistence.*
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest


@Embeddable
internal data class AnEmbeddableJpaCriteria(@Column(nullable = false) val foo: String, @Column(nullable = false) val bar: String)

@Entity
internal class WithEmbeddedJpaCriteria(@Id val id: Long, @Embedded val e: AnEmbeddableJpaCriteria)

@SpringBootTest(
    classes = [EmbeddedTestApplication::class],
    properties = [
        "spring.datasource.url=jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create",
    ]
)
class JpaCriteriaTest {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Test
    fun `jpa criteria`() {
        val cb = em.criteriaBuilder

        val query = cb.createQuery(WithEmbeddedJpaCriteria::class.java)
        val root = query.from(WithEmbeddedJpaCriteria::class.java)
        val parameter = cb.parameter(Any::class.java)
        query.select(root).where(cb.equal(root.get<Any>("e"), parameter))

        val typedQuery = em.createQuery(query)
        typedQuery.setParameter(parameter, AnEmbeddableJpaCriteria("a", "b") as Any)
        assertThatCode { typedQuery.resultList } .doesNotThrowAnyException()
    }
}
