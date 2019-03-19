package br.com.jiratorio.integration.board

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.matcher.IdMatcher
import org.apache.http.HttpStatus
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.isEmptyOrNullString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SearchBoardIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `find all boards of current user`() {
        authenticator.withDefaultUser { boardFactory.create(10) }
        authenticator.withUser("other_user") { boardFactory.create(5) }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("totalPages", equalTo(1))
                body("totalElements", equalTo(10))
                body("content.findAll { it.owner == 'default_user'}", hasSize<Int>(10))
            }
        }
    }

    @Test
    fun `find all owners`() {
        authenticator.withDefaultUser { boardFactory.create(5) }
        authenticator.withUser("first_user") { boardFactory.create(5) }
        authenticator.withUser("second_user") { boardFactory.create(5) }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/owners")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("$", hasSize<Int>(3))
                body("find { it == 'default_user' }", not(isEmptyOrNullString()))
                body("find { it == 'first_user' }", not(isEmptyOrNullString()))
                body("find { it == 'second_user' }", not(isEmptyOrNullString()))
            }
        }
    }

    @Test
    fun `filter board by name`() {
        authenticator.withDefaultUser {
            boardFactory.create(5) { empty -> empty.name = "Uniq Start Name" }
            boardFactory.create(5)
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("name", "start")
            }
            on {
                get("/boards")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("totalPages", equalTo(1))
                body("totalElements", equalTo(5))
                body("content.findAll { it.name == 'Uniq Start Name' }", hasSize<Any>(5))
            }
        }
    }

    @Test
    fun `find boards of all owners`() {
        authenticator.withDefaultUser { boardFactory.create(5) }
        authenticator.withUser("user2") { boardFactory.create(5) }
        authenticator.withUser("user3") { boardFactory.create(5) }
        authenticator.withUser("user4") { boardFactory.create(5) }

        restAssured {
            given {
                param("owner", "all")
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("totalElements", equalTo(20))
                body("totalPages", equalTo(1))
                body("content.findAll { it.owner == 'default_user' }", hasSize<Int>(5))
                body("content.findAll { it.owner == 'user2' }", hasSize<Int>(5))
                body("content.findAll { it.owner == 'user3' }", hasSize<Int>(5))
                body("content.findAll { it.owner == 'user4' }", hasSize<Int>(5))
            }
        }
    }

    @Test
    fun `find boards by owner`() {
        authenticator.withDefaultUser { boardFactory.create(5) }
        authenticator.withUser("user2") { boardFactory.create(5) }
        authenticator.withUser("user3") { boardFactory.create(5) }
        authenticator.withUser("user4") { boardFactory.create(5) }

        restAssured {
            given {
                param("owner", "user3")
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("totalElements", equalTo(5))
                body("totalPages", equalTo(1))
                body("content.findAll { it.owner == 'user3' }", hasSize<Any>(5))
            }
        }
    }

    @Test
    fun `find board by id`() {
        val board = authenticator.withDefaultUser { boardFactory.create("fullBoard") }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/{id}", board.id)
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("name", equalTo(board.name))
                body("externalId", IdMatcher(board.externalId!!))
                body("startColumn", equalTo(board.startColumn))
                body("endColumn", equalTo(board.endColumn))
                body("fluxColumn", contains<Any>(*board.fluxColumn!!.toTypedArray()))
                body("ignoreIssueType", contains<Any>(*board.ignoreIssueType!!.toTypedArray()))
                body("epicCF", equalTo(board.epicCF))
                body("estimateCF", equalTo(board.estimateCF))
                body("systemCF", equalTo(board.systemCF))
                body("projectCF", equalTo(board.projectCF))
                body("ignoreWeekend", equalTo(board.ignoreWeekend))
                body("impedimentType", equalTo(board.impedimentType!!.name))
                body("impedimentColumns", contains<Any>(*board.impedimentColumns!!.toTypedArray()))
                body("dynamicFields", hasSize<Any>(board.dynamicFields!!.size))
                body("dueDateCF", equalTo(board.dueDateCF))
                body("dueDateType", equalTo(board.dueDateType!!.name))
            }
        }
    }

    @Test
    fun `find by id not found`() {
        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/9999")
            }
            then {
                notFound()
            }
        }
    }
}
