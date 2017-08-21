package base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    // Change this value to enable retries on failure
    public static final int MAX_RETRY_COUNT = 2;
    private int count = 0;

    public boolean retry(ITestResult result) {
        count++;
        if (count <= MAX_RETRY_COUNT) {
            return true;
        } else {
            return false;
        }
    }

    public int getCount() {
        return count;
    }
}