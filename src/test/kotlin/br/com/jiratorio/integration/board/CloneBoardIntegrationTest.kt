package br.com.jiratorio.integration.board

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.config.junit.testtype.IntegrationTest
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.dsl.restAssured
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.entity.BoardFactory
import br.com.jiratorio.factory.domain.entity.DynamicFieldConfigFactory
import br.com.jiratorio.factory.domain.entity.HolidayFactory
import br.com.jiratorio.factory.domain.entity.LeadTimeConfigFactory
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.apache.http.HttpStatus.SC_CREATED
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@IntegrationTest
class CloneBoardIntegrationTest(
    private val boardRepository: BoardRepository,
    private val authenticator: Authenticator,
    private val boardFactory: BoardFactory,
    private val holidayFactory: HolidayFactory,
    private val leadTimeConfigFactory: LeadTimeConfigFactory,
    private val dynamicFieldConfigFactory: DynamicFieldConfigFactory,
    private val holidayRepository: HolidayRepository,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    @Test
    fun `test clone board`() {
        val boardToClone = authenticator.withDefaultUser {
            val board = boardFactory.create(boardFactory::withCompleteConfigurationBuilder)

            holidayFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    Holiday::board to board
                )
            )
            leadTimeConfigFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    LeadTimeConfig::board to board
                )
            )
            dynamicFieldConfigFactory.create(
                quantity = 10,
                modifyingFields = mapOf(
                    DynamicFieldConfig::board to board
                )
            )

            board
        }

        restAssured {
            given {
                header(authenticator.defaultUserHeader())
                param("boardIdToClone", 1)
            }
            on {
                post("/boards")
            }
            then {
                statusCode(SC_CREATED)
                header("location", Matchers.containsString("/boards/32"))
            }
        }

        val board = boardRepository.findByIdOrNull(32L)
            ?: throw ResourceNotFound()

        board.assertThat {
            hasExternalId(boardToClone.externalId)
            hasName(boardToClone.name)
            hasStartColumn(boardToClone.startColumn)
            hasEndColumn(boardToClone.endColumn)
            hasFluxColumn(boardToClone.fluxColumn)
            hasIgnoreIssueType(boardToClone.ignoreIssueType)
            hasEpicCF(boardToClone.epicCF)
            hasEstimateCF(boardToClone.estimateCF)
            hasSystemCF(boardToClone.systemCF)
            hasProjectCF(boardToClone.projectCF)
            hasDueDateCF(boardToClone.dueDateCF)
            hasIgnoreWeekend(boardToClone.ignoreWeekend)
            hasImpedimentType(boardToClone.impedimentType)
            hasImpedimentColumns(boardToClone.impedimentColumns)
            hasTouchingColumns(boardToClone.touchingColumns)
            hasWaitingColumns(boardToClone.waitingColumns)
            hasDueDateType(boardToClone.dueDateType)
        }

        assertThat(holidayRepository.count())
            .isEqualTo(20)
        assertThat(leadTimeConfigRepository.count())
            .isEqualTo(20)
        assertThat(dynamicFieldConfigRepository.count())
            .isEqualTo(20)
    }

}
