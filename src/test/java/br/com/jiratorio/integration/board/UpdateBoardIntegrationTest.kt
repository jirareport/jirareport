package br.com.jiratorio.integration.board

import br.com.jiratorio.base.Authenticator
import br.com.jiratorio.base.resolver.SpecificationResolver
import br.com.jiratorio.base.specification.notFound
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.factory.domain.request.UpdateBoardRequestFactory
import br.com.jiratorio.factory.entity.BoardFactory
import br.com.jiratorio.repository.BoardRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("integration")
@ExtendWith(SpringExtension::class, SpecificationResolver::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UpdateBoardIntegrationTest @Autowired constructor(
        private val boardFactory: BoardFactory,
        private val updateBoardRequestFactory: UpdateBoardRequestFactory,
        private val boardRepository: BoardRepository,
        private val authenticator: Authenticator
) {

    @Test
    @Throws(Exception::class)
    fun updateBoard() {
        authenticator.withDefaultUser { boardFactory.create() }
        val request = updateBoardRequestFactory.build()

        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(request)
            .`when`()
                .put("/boards/1")
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT)
         // @formatter:on

        boardRepository.findById(1L)
                .orElseThrow(::ResourceNotFound).apply {
                    assertThat(name)
                            .isEqualTo(request.name)
                    assertThat(startColumn)
                            .isUpperCase()
                            .isEqualToIgnoringCase(request.startColumn);
                    assertThat(endColumn)
                            .isUpperCase()
                            .isEqualToIgnoringCase(request.endColumn)
                    assertThat(fluxColumn)
                            .containsAll(request.fluxColumn.map { it.toUpperCase() })
                    assertThat(touchingColumns)
                            .containsAll(request.touchingColumns.map { it.toUpperCase() })
                    assertThat(waitingColumns)
                            .containsAll(request.waitingColumns.map { it.toUpperCase() })
                    assertThat(ignoreIssueType)
                            .containsAll(request.ignoreIssueType)
                    assertThat(epicCF)
                            .isEqualTo(request.epicCF)
                    assertThat(estimateCF)
                            .isEqualTo(request.estimateCF)
                    assertThat(systemCF)
                            .isEqualTo(request.systemCF)
                    assertThat(projectCF)
                            .isEqualTo(request.projectCF)
                    assertThat(ignoreWeekend)
                            .isEqualTo(request.ignoreWeekend)
                    assertThat(impedimentColumns)
                            .containsAll(request.impedimentColumns.map { it.toUpperCase() })
                    assertThat(dynamicFields)
                            .hasSize(request.dynamicFields.size)
                }
    }

    @Test
    fun updateNotFoundBoard() {
        // @formatter:off
        RestAssured
            .given()
                .log().all()
                .header(authenticator.defaultUserHeader())
                .contentType(ContentType.JSON)
                .body(UpdateBoardRequest())
            .`when`()
                .put("/boards/999")
            .then()
                .log().all()
                .spec(notFound())
         // @formatter:on
    }
}
