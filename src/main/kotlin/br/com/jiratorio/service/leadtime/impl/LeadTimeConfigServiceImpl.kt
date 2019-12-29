package br.com.jiratorio.service.leadtime.impl

import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.service.leadtime.LeadTimeConfigService
import br.com.jiratorio.usecase.leadtime.config.CreateLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.DeleteLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.FindAllLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.FindLeadTimeConfig
import br.com.jiratorio.usecase.leadtime.config.UpdateLeadTimeConfig
import org.springframework.stereotype.Service

@Service
class LeadTimeConfigServiceImpl(
    private val createLeadTimeConfig: CreateLeadTimeConfig,
    private val deleteLeadTimeConfig: DeleteLeadTimeConfig,
    private val findAllLeadTimeConfig: FindAllLeadTimeConfig,
    private val findLeadTimeConfig: FindLeadTimeConfig,
    private val updateLeadTimeConfig: UpdateLeadTimeConfig
) : LeadTimeConfigService {

    override fun findAll(boardId: Long): List<LeadTimeConfigResponse> =
        findAllLeadTimeConfig.execute(boardId)

    override fun create(boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest): Long =
        createLeadTimeConfig.execute(boardId, leadTimeConfigRequest)

    override fun findByBoardAndId(id: Long, boardId: Long): LeadTimeConfigResponse =
        findLeadTimeConfig.execute(id, boardId)

    override fun update(id: Long, boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest) =
        updateLeadTimeConfig.execute(id, boardId, leadTimeConfigRequest)

    override fun deleteByBoardAndId(boardId: Long, id: Long) =
        deleteLeadTimeConfig.execute(id, boardId)

}
