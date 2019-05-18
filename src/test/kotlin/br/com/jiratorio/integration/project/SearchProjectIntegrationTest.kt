package br.com.jiratorio.integration.project

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SearchProjectIntegrationTest @Autowired constructor(
    private val authenticator: Authenticator
) {

    @Test
    @LoadStubs(["projects/find-all"])
    fun `test find all`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/projects")
            }
            then {
                statusCode(SC_OK)
                body("$", hasSize<Int>(5))
                body(
                    "collect { it.name }", containsInAnyOrder(
                        "Spring Data JPA", "Spring Data JDBC", "Spring Batch", "Spring Social", "Spring Tool Suite"
                    )
                )
                body(
                    "collect { it.id }", containsInAnyOrder(
                        10550, 10552, 10090, 10481, 11700
                    )
                )
            }
        }
    }

    @Test
    @LoadStubs(["projects/find-by-id"])
    fun `test find by id`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/projects/10552")
            }
            then {
                statusCode(SC_OK)
                body("id", equalTo(10552))
                body("name", equalTo("Spring Data JPA"))
                body("issueTypes", hasSize<Int>(5))
                body(
                    "issueTypes.collect { it.id }", containsInAnyOrder(
                        1, 2, 3, 4, 5
                    )
                )
                body(
                    "issueTypes.collect { it.name }", containsInAnyOrder(
                        "Bug", "Improvement", "New Feature", "Task", "Sub-task"
                    )
                )
            }
        }
    }

    @Test
    @LoadStubs(["projects/find-by-id-not-found"])
    fun `test find by id not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/projects/1")
            }
            then {
                spec(notFound())
            }
        }
    }

}
