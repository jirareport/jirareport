package br.com.leonardoferreira.jirareport.factory;

import br.com.leonardoferreira.jbacon.JBacon;
import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.repository.BoardRepository;
import com.github.javafaker.Faker;
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

}
