package br.com.jiratorio.factory.entity;

import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.DueDateType;
import br.com.jiratorio.domain.DynamicFieldConfig;
import br.com.jiratorio.repository.BoardRepository;
import br.com.leonardoferreira.jbacon.JBacon;
import br.com.leonardoferreira.jbacon.annotation.JBaconTemplate;
import br.com.jiratorio.domain.ImpedimentType;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory extends JBacon<Board> {

    @Autowired
    private Faker faker;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    protected Board getDefault() {
        Board board = new Board();

        board.setExternalId(faker.number().randomNumber());
        board.setName(faker.lorem().word());

        return board;
    }

    @Override
    protected Board getEmpty() {
        return new Board();
    }

    @Override
    protected void persist(final Board board) {
        boardRepository.save(board);
    }

    @JBaconTemplate("fullBoard")
    protected Board fullBoard() {
        Board board = getDefault();
        board.setStartColumn("TODO");
        board.setEndColumn("DONE");
        board.setFluxColumn(Arrays.asList("TODO", "WIP", "DONE"));
        board.setIgnoreIssueType(Collections.singletonList("IT_1"));
        board.setEpicCF("customfield_123");
        board.setEstimateCF("customfield_124");
        board.setSystemCF("customfield_125");
        board.setProjectCF("customfield_126");
        board.setIgnoreWeekend(false);
        board.setImpedimentType(ImpedimentType.COLUMN);
        board.setImpedimentColumns(Arrays.asList("IMP_COLUMN1", "IMP_COLUMN2", "IMP_COLUMN3"));
        board.setDynamicFields(Arrays.asList(new DynamicFieldConfig("dn_field1", "customfield_127"),
                new DynamicFieldConfig("dn_field2", "customfield_128")));
        board.setTouchingColumns(faker.lorem().words());
        board.setWaitingColumns(faker.lorem().words());
        board.setDueDateCF("customfield_321");
        board.setDueDateType(DueDateType.FIRST_DUE_DATE_AND_END_DATE);

        return board;
    }

}
