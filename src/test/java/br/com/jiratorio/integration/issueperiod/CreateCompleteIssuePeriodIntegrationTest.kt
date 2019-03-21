package br.com.jiratorio.integration.issueperiod

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.annotation.LoadStubs
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.repository.IssuePeriodRepository
import br.com.jiratorio.repository.IssueRepository
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CreateCompleteIssuePeriodIntegrationTest @Autowired constructor(
    private val boardFactory: BoardFactory,
    private val authenticator: Authenticator,
    private val issuePeriodRepository: IssuePeriodRepository,
    private val issueRepository: IssueRepository
) {

    @Test
    @LoadStubs(["issues/complex-issues"])
    fun `create complete issue period`() {
        authenticator.withDefaultUser {
            boardFactory.create("withCompleteConfiguration")
        }
    }

}
