package br.com.jiratorio.factory.entity;

import br.com.jiratorio.repository.HolidayRepository;
import br.com.leonardoferreira.jbacon.JBacon;
import br.com.jiratorio.domain.Holiday;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HolidayFactory extends JBacon<Holiday> {

    @Autowired
    private Faker faker;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private BoardFactory boardFactory;

    @Override
    protected Holiday getDefault() {
        Holiday holiday = new Holiday();

        holiday.setDate(LocalDate.from(faker.date().future(5, TimeUnit.DAYS).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()));
        holiday.setDescription(faker.lorem().word());
        holiday.setBoard(boardFactory.create());

        return holiday;
    }

    @Override
    protected Holiday getEmpty() {
        return new Holiday();
    }

    @Override
    protected void persist(final Holiday holiday) {
        holidayRepository.save(holiday);
    }

}
