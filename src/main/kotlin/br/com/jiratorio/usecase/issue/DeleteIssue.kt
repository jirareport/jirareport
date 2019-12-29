package br.com.jiratorio.usecase.issue

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.repository.IssueRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteIssue(
    private val issueRepository: IssueRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(issues: Set<Issue>) {
        log.info("Method=execute, issues={}", issues)
        issueRepository.deleteAll(issues)
    }

}
