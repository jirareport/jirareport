package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByBoardResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.service.IssuePeriodService
import br.com.jiratorio.usecase.issue.period.CreateIssuePeriod
import br.com.jiratorio.usecase.issue.period.DeleteIssuePeriod
import br.com.jiratorio.usecase.issue.period.FindIssuePeriodByBoard
import br.com.jiratorio.usecase.issue.period.FindIssuePeriod
import br.com.jiratorio.usecase.issue.period.UpdateIssuePeriod
import org.springframework.stereotype.Service

@Service
class IssuePeriodServiceImpl(
    private val createIssuePeriod: CreateIssuePeriod,
    private val deleteIssuePeriod: DeleteIssuePeriod,
    private val findIssuePeriodByBoard: FindIssuePeriodByBoard,
    private val findIssuePeriod: FindIssuePeriod,
    private val updateIssuePeriod: UpdateIssuePeriod
) : IssuePeriodService {

    override fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long =
        createIssuePeriod.execute(createIssuePeriodRequest, boardId)

    override fun findById(boardId: Long, id: Long): IssuePeriodByIdResponse =
        findIssuePeriod.execute(id, boardId)

    override fun removeByBoardAndId(boardId: Long, id: Long) =
        deleteIssuePeriod.execute(id, boardId)

    override fun update(boardId: Long, id: Long) =
        updateIssuePeriod.execute(id, boardId)

    override fun findIssuePeriodByBoard(boardId: Long): IssuePeriodByBoardResponse =
        findIssuePeriodByBoard.execute(boardId)

}
