package br.com.jiratorio.integration.leadtimeconfig

import br.com.jiratorio.Authenticator
import br.com.jiratorio.restassured.specification.notFound
import br.com.jiratorio.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.matcher.IdMatcher
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@IntegrationTest
internal class SearchLeadTimeConfigIntegrationTest(
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator
) {

    @Test
    fun `find all lead time config`() {
        authenticator.withDefaultUser {
            val defaultBoard = boardFactory.create()
            leadTimeConfigFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    LeadTimeConfigEntity::board to defaultBoard
                )
            )
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/lead-time-configs")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("$", Matchers.hasSize<Int>(10))
                body("findAll { it.boardId == 1 }", Matchers.hasSize<Int>(10))
            }
        }
    }

    @Test
    fun `find by id`() {
        val leadTimeConfig = authenticator.withDefaultUser {
            leadTimeConfigFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/lead-time-configs/1")
            }
            then {
                statusCode(HttpStatus.SC_OK)
                body("id", IdMatcher(leadTimeConfig.id))
                body("boardId", IdMatcher(leadTimeConfig.board.id))
                body("name", Matchers.equalTo(leadTimeConfig.name))
                body("startColumn", Matchers.equalTo(leadTimeConfig.startColumn))
                body("endColumn", Matchers.equalTo(leadTimeConfig.endColumn))
            }
        }
    }

    @Test
    fun `find by id not found`() {
        authenticator.withDefaultUser {
            boardFactory.create()
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
            }
            on {
                get("/boards/1/lead-time-configs/1")
            }
            then {
                spec(notFound())
            }
        }
    }
}
