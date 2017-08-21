package base;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.fest.assertions.api.Assertions;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

/**
 * Base test class for common behavior across tests
 * 
 * @author kailin
 */
abstract public class BaseTest {

    private Logger log;
    protected TestProperties env;
    private final InheritableThreadLocal<Method> testMethod = new InheritableThreadLocal<Method>();
    private final InheritableThreadLocal<XmlTest> xmlTest = new InheritableThreadLocal<XmlTest>();
    private final InheritableThreadLocal<ITestContext> testContext = new InheritableThreadLocal<ITestContext>();

    @BeforeMethod(alwaysRun = true)
    protected void setUp(Method m, ITestContext context, XmlTest test) throws Exception {

        // Load system properties
        env = new TestProperties(System.getProperties());
        ThreadContainer.setTestProperties(env);

        // Setup log4j
        setupLog4J(m);

        try {
            testMethod.set(m);
            xmlTest.set(test);
            testContext.set(context);
            // Specific setup for child classes
            specificSetup(m);

            if (m.getAnnotation(Test.class) != null) {
                Test annotation = m.getAnnotation(Test.class);
                String description = annotation.description();
                if (description != null && !description.isEmpty()) {
                    L().debug("Test description: " + description);
                }
            }
            L().debug("setUp completed!");
        } catch (Exception ex) {
            L().error("Error while executing setUp", ex);
            throw ex;
        }
    }

    private void setupLog4J(Method m) {
        String logfile = m.getDeclaringClass().getSimpleName() + "-" + m.getName();
        ThreadContext.put("logfile", logfile);
        ThreadContext.put("reportsfolder", env.reportsFolder);
        log = LogManager.getLogger(logfile);
    }

    /**
     * Tear down method for closing all connections, stopping web driver, etc.
     */
    @AfterMethod(alwaysRun = true)
    protected void tearDown(ITestResult tr, ITestContext context, java.lang.reflect.Method m) {
        try {
            specificTearDown(tr, context, m);

            if (!tr.isSuccess()) {
                L().error("Failed/Skipped test", tr.getThrowable());
            }

            ITestNGMethod method = tr.getMethod();
            L().info("Finished " + method.getRealClass() + "." + method.getMethodName()
                    + " with parameters: " + tr.getParameters());
            L().debug("tearDown completed!");
            ThreadContext.clearAll();

        } catch (Exception ex) {
            L().error("Error while executing tearDown", ex);
        }
    }

    /**
     * Override this method when you want some specific behavior before a specific test. This method
     * is intended to be used by subclasses like WebdriverBaseTest.
     */
    protected void specificSetup(Method m) {

    }

    /**
     * Override this method when you want some specific behavior after a specific test. This method
     * is intended to be used by subclasses like WebdriverBaseTest.
     * 
     * @param m
     * @param context
     * @param tr
     */
    protected void specificTearDown(ITestResult tr, ITestContext context, Method m) {

    }

    /**
     * Method for logging across all the tests
     * 
     * @return Logger for logging
     */
    protected Logger L() {
        return log;
    }

    protected void fail(String message) {
        Assertions.fail(message);
    }

    protected Method testMethod() {
        return testMethod.get();
    }

    protected XmlTest xmlTest() {
        return xmlTest.get();
    }

    protected ITestContext testContext() {
        return testContext.get();
    }

    public void logComment(String message) {
        L().info(message);
    }
}