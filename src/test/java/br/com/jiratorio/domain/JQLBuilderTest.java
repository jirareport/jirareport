package br.com.jiratorio.domain;

import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class JQLBuilderTest {

    private JQLBuilder jql;

    @BeforeEach
    void setUp() {
        jql = new JQLBuilder();
    }

    @Test
    void buildJqlWithParameter() {
        jql.append("project = {project} ", "JIRAT");
        jql.append("and issueTypes in ({issueTypes})", Arrays.asList("IT1", "IT2", "IT3"));

        Assertions.assertThat(jql.build())
                .isEqualTo("project = 'JIRAT' and issueTypes in ('IT1','IT2','IT3')");
    }

    @Test
    void buildJqlWithoutParameter() {
        jql.append("project = JIRAT");

        Assertions.assertThat(jql.build())
                .isEqualTo("project = JIRAT");
    }

}
