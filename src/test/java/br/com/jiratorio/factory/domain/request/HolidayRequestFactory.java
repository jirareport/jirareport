package br.com.jiratorio.factory.domain.request;

import br.com.leonardoferreira.jbacon.JBacon;
import br.com.jiratorio.domain.request.HolidayRequest;
import com.github.javafaker.Faker;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HolidayRequestFactory extends JBacon<HolidayRequest> {

    @Autowired
    private Faker faker;

    @Override
    protected HolidayRequest getDefault() {
        HolidayRequest holidayRequest = new HolidayRequest();

        holidayRequest.setDate(new SimpleDateFormat("dd/MM/yyyy")
                .format(faker.date().future(5, TimeUnit.DAYS)));
        holidayRequest.setDescription(faker.lorem().word());

        return holidayRequest;
    }

    @Override
    protected HolidayRequest getEmpty() {
        return new HolidayRequest();
    }

    @Override
    protected void persist(final HolidayRequest holidayRequest) {
        throw new UnsupportedOperationException();
    }
}
