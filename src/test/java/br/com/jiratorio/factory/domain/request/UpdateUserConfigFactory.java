package br.com.jiratorio.factory.domain.request;

import br.com.jiratorio.domain.ChartType;
import br.com.jiratorio.domain.request.UpdateUserConfigRequest;
import br.com.leonardoferreira.jbacon.JBacon;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserConfigFactory extends JBacon<UpdateUserConfigRequest> {

    private final Faker faker;

    public UpdateUserConfigFactory(final Faker faker) {
        this.faker = faker;
    }

    @Override
    protected UpdateUserConfigRequest getDefault() {
        UpdateUserConfigRequest updateUserConfigRequest = new UpdateUserConfigRequest();

        updateUserConfigRequest.setState(faker.address().state());
        updateUserConfigRequest.setCity(faker.address().city());
        updateUserConfigRequest.setHolidayToken(faker.crypto().md5());
        updateUserConfigRequest.setLeadTimeChartType(faker.options().option(ChartType.class));
        updateUserConfigRequest.setThroughputChartType(faker.options().option(ChartType.class));

        return updateUserConfigRequest;
    }

    @Override
    protected UpdateUserConfigRequest getEmpty() {
        return new UpdateUserConfigRequest();
    }

    @Override
    protected void persist(final UpdateUserConfigRequest updateUserConfigRequest) {
        throw new UnsupportedOperationException();
    }

}
