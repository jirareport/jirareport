package br.com.jiratorio.base.executionlistener;

import br.com.jiratorio.base.CleanDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

@Slf4j
public class CleanDatabaseTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        try {
            CleanDatabase cleanDatabase = testContext.getApplicationContext()
                    .getBean(CleanDatabase.class);
            cleanDatabase.clean();
        } catch (NoSuchBeanDefinitionException e) {
            log.info("Method=beforeTestMethod, Info=Not a integration test");
        }
    }

}
