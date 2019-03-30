package br.com.jiratorio.service.impl

import br.com.jiratorio.assert.LeadTimeAssert
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.domain.entity.embedded.Changelog
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.toLocalDate
import br.com.jiratorio.extension.toLocalDateTime
import br.com.jiratorio.repository.LeadTimeRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.LeadTimeConfigService
import io.mockk.called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@Tag("unit")
@ExtendWith(MockKExtension::class)
internal class LeadTimeServiceImplTest {

    private val leadTimeConfigService = mockk<LeadTimeConfigService>()

    private val holidayService = mockk<HolidayService>()

    private val leadTimeRepository = mockk<LeadTimeRepository>()

    private val boardService = mockk<BoardService>()

    private val leadTimeServiceImpl = LeadTimeServiceImpl(
        leadTimeConfigService,
        holidayService,
        leadTimeRepository,
        boardService
    )

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test create lead times without lead times config`() {
        every {
            leadTimeConfigService.findAllByBoardId(1L)
        } returns emptyList()

        leadTimeServiceImpl.createLeadTimes(emptyList(), 1L)

        verifyAll {
            leadTimeConfigService.findAllByBoardId(1L)
            holidayService wasNot called
            leadTimeRepository wasNot called
            boardService wasNot called
        }
    }

    @Test
    fun `test create lead times with empty issue list`() {
        val board = defaultBoard()

        every {
            boardService.findById(1L)
        } returns board

        every {
            leadTimeConfigService.findAllByBoardId(1L)
        } returns defaultLeadTimes(board)

        every {
            holidayService.findDaysByBoard(1L)
        } returns commonHolidays()

        leadTimeServiceImpl.createLeadTimes(emptyList(), 1L)

        verifyAll {
            boardService.findById(1L)
            leadTimeConfigService.findAllByBoardId(1L)
            holidayService.findDaysByBoard(1L)
        }

        verifyAll(inverse = true) {
            leadTimeRepository.deleteByIssueId(any())
            leadTimeRepository.save(any<LeadTime>())
        }
    }

    @Test
    fun `test create lead times`() {
        val board = defaultBoard()

        every {
            boardService.findById(1L)
        } returns board

        every {
            leadTimeConfigService.findAllByBoardId(1L)
        } returns defaultLeadTimes(board)

        every {
            holidayService.findDaysByBoard(1L)
        } returns commonHolidays()

        every {
            leadTimeRepository.deleteByIssueId(any())
        } just runs

        every {
            leadTimeRepository.save(any<LeadTime>())
        } answers { firstArg() }

        val issue = Issue(
            id = 1L,
            key = "JIRAT-1",
            board = board,
            changelog = listOf(
                Changelog(from = null, to = "BACKLOG", created = "01/01/2019 12:00".toLocalDateTime()),
                Changelog(from = "BACKLOG", to = "ANALYSIS", created = "10/01/2019 12:00".toLocalDateTime()),
                Changelog(from = "ANALYSIS", to = "DEV WIP", created = "12/01/2019 12:00".toLocalDateTime()),
                Changelog(from = "DEV WIP", to = "DEV DONE", created = "21/01/2019 12:00".toLocalDateTime()),
                Changelog(from = "DEV DONE", to = "TEST WIP", created = "23/01/2019 12:00".toLocalDateTime()),
                Changelog(from = "TEST WIP", to = "TEST DONE", created = "31/01/2019 12:00".toLocalDateTime()),
                Changelog(from = "TEST DONE", to = "REVIEW", created = "01/02/2019 12:00".toLocalDateTime()),
                Changelog(from = "REVIEW", to = "DELIVERY LINE", created = "02/02/2019 12:00".toLocalDateTime()),
                Changelog(from = "DELIVERY LINE", to = "ACCOMPANIMENT", created = "06/02/2019 12:00".toLocalDateTime()),
                Changelog(from = "ACCOMPANIMENT", to = "DONE", created = "12/02/2019 12:00".toLocalDateTime())
            ),
            created = "01/01/2019 12:00".toLocalDateTime(),
            startDate = "12/01/2019 12:00".toLocalDateTime(),
            endDate = "12/02/2019 12:00".toLocalDateTime(),
            leadTime = 7L,
            summary = "JIRAT-1 summary"
        )

        leadTimeServiceImpl.createLeadTimes(listOf(issue), 1L)

        verifyAll {
            boardService.findById(1L)
            leadTimeConfigService.findAllByBoardId(1L)
            holidayService.findDaysByBoard(1L)

            leadTimeRepository.deleteByIssueId(1L)
            leadTimeRepository.save(any<LeadTime>())
        }

        verify(exactly = 3) {
            leadTimeRepository.save(any<LeadTime>())
        }

        val leadTimes = issue.leadTimes ?: throw ResourceNotFound()

        LeadTimeAssert(leadTimes.find { it.leadTimeConfig.name == "Development Lead Time" }!!).assertThat {
            hasLeadTime(10)
            hasStartDate("10/01/2019 12:00".toLocalDateTime())
            hasEndDate("23/01/2019 12:00".toLocalDateTime())
        }

        LeadTimeAssert(leadTimes.find { it.leadTimeConfig.name == "Test Lead Time" }!!).assertThat {
            hasLeadTime(8)
            hasStartDate("23/01/2019 12:00".toLocalDateTime())
            hasEndDate("02/02/2019 12:00".toLocalDateTime())
        }

        LeadTimeAssert(leadTimes.find { it.leadTimeConfig.name == "Delivery Lead Time" }!!).assertThat {
            hasLeadTime(7)
            hasStartDate("02/02/2019 12:00".toLocalDateTime())
            hasEndDate("12/02/2019 12:00".toLocalDateTime())
        }
    }

    private fun defaultLeadTimes(board: Board): List<LeadTimeConfig> {
        return listOf(
            LeadTimeConfig(
                board = board,
                name = "Development Lead Time",
                startColumn = "ANALYSIS",
                endColumn = "TEST WIP"
            ),
            LeadTimeConfig(
                board = board,
                name = "Test Lead Time",
                startColumn = "TEST WIP",
                endColumn = "DELIVERY LINE"
            ),
            LeadTimeConfig(
                board = board,
                name = "Delivery Lead Time",
                startColumn = "DELIVERY LINE",
                endColumn = "DONE"
            )
        )
    }

    private fun commonHolidays(): List<LocalDate> {
        return listOf(
            "01/01/2019".toLocalDate(),
            "01/05/2019".toLocalDate(),
            "25/12/2019".toLocalDate()
        )
    }

    private fun defaultBoard(): Board {
        return Board(
            id = 1L,
            externalId = 123L,
            name = "My Board",
            startColumn = "ANALYSIS",
            endColumn = "ACCOMPANIMENT",
            fluxColumn = mutableListOf(
                "BACKLOG",
                "ANALYSIS",
                "DEV WIP",
                "DEV DONE",
                "TEST WIP",
                "TEST DONE",
                "REVIEW",
                "DELIVERY LINE",
                "ACCOMPANIMENT",
                "DONE"
            )
        )
    }

}
