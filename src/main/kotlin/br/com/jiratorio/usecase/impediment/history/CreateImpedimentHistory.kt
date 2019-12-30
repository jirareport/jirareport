package br.com.jiratorio.usecase.impediment.history

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.repository.ImpedimentHistoryRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateImpedimentHistory(
    private val impedimentHistoryRepository: ImpedimentHistoryRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(issues: List<Issue>) {
        log.info("Method=execute, issues={}", issues)

        issues.forEach { issue ->
            issue.impedimentHistory.forEach { impedimentHistory ->
                impedimentHistory.issueId = issue.id
            }

            impedimentHistoryRepository.saveAll(issue.impedimentHistory)
        }
    }

}
