package br.com.jiratorio.integration.board;

import br.com.jiratorio.base.Authenticator;
import br.com.jiratorio.base.resolver.SpecificationResolver;
import br.com.jiratorio.base.specification.NotFound;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.request.UpdateBoardRequest;
import br.com.jiratorio.exception.ResourceNotFound;
import br.com.jiratorio.factory.domain.request.UpdateBoardRequestFactory;
import br.com.jiratorio.factory.entity.BoardFactory;
import br.com.jiratorio.repository.BoardRepository;
import br.com.jiratorio.util.DateUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith({SpringExtension.class, SpecificationResolver.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateBoardIntegrationTest {

    private final BoardFactory boardFactory;

    private final UpdateBoardRequestFactory updateBoardRequestFactory;

    private final BoardRepository boardRepository;

    private final Authenticator authenticator;

    @Autowired
    UpdateBoardIntegrationTest(final BoardFactory boardFactory,
                                      final UpdateBoardRequestFactory updateBoardRequestFactory,
                                      final BoardRepository boardRepository,
                                      final Authenticator authenticator) {
        this.boardFactory = boardFactory;
        this.updateBoardRequestFactory = updateBoardRequestFactory;
        this.boardRepository = boardRepository;
        this.authenticator = authenticator;
    }

    @Test
    void updateBoard() throws Exception {
        authenticator.withDefaultUser(boardFactory::create);
        UpdateBoardRequest request = updateBoardRequestFactory.build();

        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .put("/boards/1")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        // @formatter:on

        Board board = boardRepository.findById(1L)
                .orElseThrow(ResourceNotFound::new);
        Assertions.assertAll("boardContent",
                () -> Assertions.assertEquals(request.getName(), board.getName()),
                () -> Assertions.assertEquals(request.getStartColumn().toUpperCase(DateUtil.LOCALE_BR), board.getStartColumn()),
                () -> Assertions.assertEquals(request.getEndColumn().toUpperCase(DateUtil.LOCALE_BR), board.getEndColumn()),
                () -> Assertions.assertEquals(request.getFluxColumn().size(), board.getFluxColumn().size()),
                () -> Assertions.assertIterableEquals(request.getFluxColumn().stream().map(String::toUpperCase).collect(Collectors.toList()),
                        board.getFluxColumn()),
                () -> Assertions.assertIterableEquals(request.getTouchingColumns().stream().map(String::toUpperCase).collect(Collectors.toList()),
                        board.getTouchingColumns()),
                () -> Assertions.assertIterableEquals(request.getWaitingColumns().stream().map(String::toUpperCase).collect(Collectors.toList()),
                        board.getWaitingColumns()),
                () -> Assertions.assertEquals(request.getIgnoreIssueType().size(), board.getIgnoreIssueType().size()),
                () -> Assertions.assertEquals(request.getEpicCF(), board.getEpicCF()),
                () -> Assertions.assertEquals(request.getEstimateCF(), board.getEstimateCF()),
                () -> Assertions.assertEquals(request.getSystemCF(), board.getSystemCF()),
                () -> Assertions.assertEquals(request.getProjectCF(), board.getProjectCF()),
                () -> Assertions.assertEquals(request.getIgnoreWeekend(), board.getIgnoreWeekend()),
                () -> Assertions.assertEquals(request.getImpedimentColumns().size(), board.getImpedimentColumns().size()),
                () -> Assertions.assertEquals(request.getDynamicFields().size(), board.getDynamicFields().size()));
    }

    @Test
    void updateNotFoundBoard(@NotFound final ResponseSpecification spec) {
        // @formatter:off
        RestAssured
                .given()
                    .log().all()
                    .header(authenticator.defaultUserHeader())
                    .contentType(ContentType.JSON)
                    .body(new UpdateBoardRequest())
                .when()
                    .put("/boards/999")
                .then()
                    .log().all()
                    .spec(spec);
        // @formatter:on
    }
}
