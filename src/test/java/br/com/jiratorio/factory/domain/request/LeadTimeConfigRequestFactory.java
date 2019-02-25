package br.com.jiratorio.factory.domain.request;

import br.com.leonardoferreira.jbacon.JBacon;
import br.com.jiratorio.domain.request.LeadTimeConfigRequest;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeadTimeConfigRequestFactory extends JBacon<LeadTimeConfigRequest> {

    @Autowired
    private Faker faker;

    @Override
    protected LeadTimeConfigRequest getDefault() {
        LeadTimeConfigRequest leadTimeConfigRequest = new LeadTimeConfigRequest();

        leadTimeConfigRequest.setName(faker.lorem().word());
        leadTimeConfigRequest.setStartColumn(faker.lorem().word());
        leadTimeConfigRequest.setEndColumn(faker.lorem().word());

        return leadTimeConfigRequest;
    }

    @Override
    protected LeadTimeConfigRequest getEmpty() {
        return new LeadTimeConfigRequest();
    }

    @Override
    protected void persist(final LeadTimeConfigRequest leadTimeConfigRequest) {
        throw new UnsupportedOperationException();
    }
}
