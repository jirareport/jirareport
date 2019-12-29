package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.service.ImpedimentService
import br.com.jiratorio.usecase.impediment.history.CreateImpedimentHistory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImpedimentServiceImpl(
    private val createImpedimentHistory: CreateImpedimentHistory
) : ImpedimentService {

    @Transactional
    override fun saveImpedimentHistories(issues: List<Issue>) =
        createImpedimentHistory.execute(issues)

}
