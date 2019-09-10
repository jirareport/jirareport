package br.com.jiratorio.integration.board

import br.com.jiratorio.assert.assertThat
import br.com.jiratorio.base.Authenticator
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
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloneBoardIntegrationTest @Autowired constructor(
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

            holidayFactory.create(10) {
                it.board = board
            }
            leadTimeConfigFactory.create(10) {
                it.board = board
            }
            dynamicFieldConfigFactory.create(10) {
                it.board = board
            }

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
