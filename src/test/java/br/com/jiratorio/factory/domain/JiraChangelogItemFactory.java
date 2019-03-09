package br.com.jiratorio.factory.domain;

import br.com.jiratorio.domain.changelog.JiraChangelogItem;
import br.com.leonardoferreira.jbacon.JBacon;
import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JiraChangelogItemFactory extends JBacon<JiraChangelogItem> {

    @Autowired
    private Faker faker;

    @Override
    protected JiraChangelogItem getDefault() {
        JiraChangelogItem jiraChangelogItem = new JiraChangelogItem();

        jiraChangelogItem.setField("duedate");
        jiraChangelogItem.setTo(toLocalDateTime(faker.date().future(3, TimeUnit.DAYS))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        jiraChangelogItem.setCreated(toLocalDateTime(faker.date().past(3, TimeUnit.DAYS)));

        return jiraChangelogItem;
    }

    @Override
    protected JiraChangelogItem getEmpty() {
        return new JiraChangelogItem();
    }

    @Override
    protected void persist(final JiraChangelogItem jiraChangelogItem) {
        throw new UnsupportedOperationException();
    }

    private LocalDateTime toLocalDateTime(final Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
