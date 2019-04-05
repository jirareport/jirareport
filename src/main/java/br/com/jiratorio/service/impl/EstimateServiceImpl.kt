package br.com.jiratorio.service.impl

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateFieldReference
import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.form.EstimateForm
import br.com.jiratorio.domain.form.IssueForm
import br.com.jiratorio.exception.BadRequestException
import br.com.jiratorio.extension.log
import br.com.jiratorio.extension.time.plusDays
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.EstimateService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.service.JQLService
import br.com.jiratorio.service.PercentileService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils
import java.util.HashMap

@Service
class EstimateServiceImpl(
    private val boardService: BoardService,
    private val issueService: IssueService,
    private val holidayService: HolidayService,
    private val percentileService: PercentileService,
    private val jqlService: JQLService,
    private val messageResolver: MessageResolver
) : EstimateService {

    @Transactional(readOnly = true)
    override fun findEstimateIssues(boardId: Long, estimateForm: EstimateForm): List<EstimateIssue> {
        log.info("Method=findEstimateIssues, boardId={}, estimateForm={}", boardId, estimateForm)

        val board = boardService.findById(boardId)

        if (CollectionUtils.isEmpty(board.fluxColumn)) {
            throw BadRequestException(messageResolver.resolve("errors.flux-column-not-configured"))
        }

        val jql = jqlService.openedIssues(board)
        val issueList = issueService.findEstimateByJql(jql, board)

        val holidays = if (board.ignoreWeekend == true)
            emptyList()
        else
            holidayService.findDaysByBoard(boardId)

        val fieldPercentileMap = HashMap<String, Percentile>()

        issueList.forEach { issue ->
            val value = retrieveByFilter(issue, estimateForm.filter)!!

            var percentile: Percentile? = fieldPercentileMap[value]

            if (percentile == null) {
                percentile = calculatePercentile(board, estimateForm, value)
                fieldPercentileMap[value] = percentile
            }

            issue.estimateDateAvg = issue.startDate.plusDays(
                percentile.average.toLong(), holidays, board.ignoreWeekend
            )

            issue.estimateDatePercentile50 = issue.startDate.plusDays(
                percentile.median, holidays, board.ignoreWeekend
            )

            issue.estimateDatePercentile75 = issue.startDate.plusDays(
                percentile.percentile75, holidays, board.ignoreWeekend
            )

            issue.estimateDatePercentile90 = issue.startDate.plusDays(
                percentile.percentile90, holidays, board.ignoreWeekend
            )
        }

        return issueList
    }

    private fun calculatePercentile(board: Board, estimateForm: EstimateForm, value: String?): Percentile {
        log.info("Method=calculatePercentile, board={}, estimateForm={}, value={}", board, estimateForm, value)

        val issueForm = buildIssueFormByEstimateForm(estimateForm, value)

        val leadTimeList = issueService.findLeadTimeByExample(board.id, issueForm)

        return percentileService.calculatePercentile(leadTimeList)
    }

    private fun buildIssueFormByEstimateForm(estimateForm: EstimateForm, value: String?): IssueForm {
        log.info("Method=buildIssueFormByEstimateForm, estimateForm={}, value={}", estimateForm, value)

        val issueForm = IssueForm()
        issueForm.startDate = estimateForm.startDate
        issueForm.endDate = estimateForm.endDate
        val filter = estimateForm.filter
        if (StringUtils.isEmpty(value)) {
            return issueForm
        }

        if (filter == EstimateFieldReference.ISSUE_TYPE) {
            issueForm.issueTypes.add(value)
        } else if (filter == EstimateFieldReference.SYSTEM) {
            issueForm.systems.add(value)
        } else if (filter == EstimateFieldReference.TASK_SIZE) {
            issueForm.taskSize.add(value)
        } else if (filter == EstimateFieldReference.EPIC) {
            issueForm.epics.add(value)
        } else if (filter == EstimateFieldReference.PROJECT) {
            issueForm.projects.add(value)
        } else if (filter == EstimateFieldReference.PRIORITY) {
            issueForm.priorities.add(value)
        }
        return issueForm
    }

    private fun retrieveByFilter(issue: EstimateIssue, filter: EstimateFieldReference): String? {
        log.info("Method=retrieveByFilter, issue={}, filter={}", issue, filter)

        if (filter == EstimateFieldReference.ISSUE_TYPE) {
            return issue.issueType
        }
        if (filter == EstimateFieldReference.SYSTEM) {
            return issue.system
        }
        if (filter == EstimateFieldReference.TASK_SIZE) {
            return issue.estimated
        }
        if (filter == EstimateFieldReference.EPIC) {
            return issue.epic
        }
        if (filter == EstimateFieldReference.PROJECT) {
            return issue.project
        }
        if (filter == EstimateFieldReference.PRIORITY) {
            return issue.priority
        }

        return null
    }

}
