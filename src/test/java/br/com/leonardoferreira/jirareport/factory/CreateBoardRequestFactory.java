package br.com.leonardoferreira.jirareport.factory;

import br.com.leonardoferreira.jbacon.JBacon;
import br.com.leonardoferreira.jirareport.domain.request.CreateBoardRequest;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateBoardRequestFactory extends JBacon<CreateBoardRequest>  {

    @Autowired
    private Faker faker;

    @Override
    protected CreateBoardRequest getDefault() {
        CreateBoardRequest createBoardRequest = new CreateBoardRequest();

        createBoardRequest.setName(faker.lorem().word());
        createBoardRequest.setExternalId(faker.number().randomNumber());

        return createBoardRequest;
    }

    @Override
    protected CreateBoardRequest getEmpty() {
        return new CreateBoardRequest();
    }

    @Override
    protected void persist(final CreateBoardRequest createBoardRequest) {
        throw new UnsupportedOperationException();
    }
}
