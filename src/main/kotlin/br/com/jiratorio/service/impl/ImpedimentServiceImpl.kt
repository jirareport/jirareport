package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.extension.log
import br.com.jiratorio.repository.ImpedimentHistoryRepository
import br.com.jiratorio.service.ImpedimentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImpedimentServiceImpl(
    private val impedimentHistoryRepository: ImpedimentHistoryRepository
) : ImpedimentService {

    @Transactional
    override fun saveImpedimentHistories(issues: List<Issue>) {
        log.info("Method=saveImpedimentHistories, issues={}, boardId={}", issues)

        issues.forEach { issue ->
            issue.impedimentHistory.forEach { impedimentHistory ->
                impedimentHistory.issueId = issue.id
            }

            impedimentHistoryRepository.saveAll(issue.impedimentHistory)
        }
    }
}
