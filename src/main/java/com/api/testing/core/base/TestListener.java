package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        LOGGER.warn("Configuration error: ", itr.getThrowable());
        itr.setStatus(ITestResult.SKIP);
    }
}