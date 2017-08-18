package base;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import base.RetryAnalyzer;

/**
 * Shows partial results while running a suite
 *
 * @author kailin
 */
public class InvokedMethodListener implements IInvokedMethodListener2 {

	private Logger LOGGER = LogManager.getLogger(InvokedMethodListener.class);

	// Counters of success or failed tests.
	private int INVOKED = 0;
	private int SKIPPED = 0;
	private int FAILED = 0;
	private int SUCCESS = 0;

	public InvokedMethodListener(Logger log) {
		// this.LOGGER = log;
	}

	// Unused procedure but mandatory to implement.
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
	}

	// Unused procedure but mandatory to implement.
	public void beforeInvocation(IInvokedMethod method, ITestResult result) {
	}

	public synchronized void afterInvocation(IInvokedMethod method, ITestResult result, ITestContext test) {

		// If it's a test method then show partial results
		if (method.getTestMethod().isTest()) {
			String log = "\n\n\t" + method.getTestMethod().getRealClass() + ":" + method.getTestMethod().getMethodName()
					+ ": ";
			log += testResult(method, test, result);

			// Add parameters if any
			if (result.getParameters() != null && result.getParameters().length > 0) {
				log += "\n\tParameters : (";
				log += Arrays.toString(result.getParameters());
				log += ")";
			}

			if (log.contains("SUCCESS")) {
				SUCCESS++;
				INVOKED++;
			} else if (log.contains("FAIL")) {
				FAILED++;
				INVOKED++;
			} else if (log.contains("SKIPPED")) {
				SKIPPED++;
				INVOKED++;
			}

			log += "\n\t" + overallResults(test);
			log += "\n";
			LOGGER.info(log);

			// Get current failed tests
			LOGGER.info("Total failed tests: " + test.getFailedTests().size());
		}

		if (result.getStatus() == ITestResult.SKIP) {
			LOGGER.info("Reason for skipping: " + result.getThrowable());
		}
	}

	// Calculate partial results
	private String overallResults(ITestContext test) {
		int totalTests = test.getAllTestMethods().length;
		int successResults = SUCCESS;
		int skippedResults = SKIPPED;
		int failedResults = FAILED;
		int invokedTests = INVOKED;
		int successPercentage;
		if (invokedTests != 0) {
			successPercentage = (int) ((successResults * 100.0f) / invokedTests);
		} else {
			successPercentage = 0;
		}

		return "Tests Success/Failed/Skipped/Invoked/Total-"
				+ "(Success percentage % vs invoked / progress percentage): " + successResults + "s/" + failedResults
				+ "f/" + skippedResults + "sk/" + invokedTests + "i/" + totalTests + "t-(" + successPercentage
				+ "% success)";
	}

	private String testResult(IInvokedMethod method, ITestContext test, ITestResult result) {

		if (result.getStatus() == ITestResult.SKIP) {
			return "SKIPPED";
		}

		boolean isSuccess = result.isSuccess();

		if (isSuccess) {
			return "SUCCESS";
		} else {
			int currentCount = ((RetryAnalyzer) result.getMethod().getRetryAnalyzer()).getCount();
			if (currentCount > RetryAnalyzer.MAX_RETRY_COUNT) {
				result.setStatus(ITestResult.FAILURE);
				return "FAIL";
			} else {
				result.setStatus(ITestResult.FAILURE);
				return "UNSTABLE (" + currentCount + " runs)";
			}
		}
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult result, ITestContext test) {
		// All tests with "KNOWNBUG" group will skip, it's like deprecated but
		// these shows on able to email report
		if (Arrays.asList(method.getTestMethod().getGroups()).contains("KNOWNBUG")) {
			throw new SkipException("Method " + method.getTestMethod().getMethodName() + " skipped "
					+ "because it's a known bug, pending to be resolved");
		} else if (Arrays.asList(method.getTestMethod().getGroups()).contains("DEPRECATED")) {
			throw new SkipException("Method " + method.getTestMethod().getMethodName() + " skipped "
					+ "because it's deprecated, just here for counting reasons");
		}
	}
}