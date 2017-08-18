package base;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ThreadContainer {
	private static InheritableThreadLocal<CatchAllExceptionSolver> exceptionSolver = new InheritableThreadLocal<CatchAllExceptionSolver>();
	private static InheritableThreadLocal<Integer> retryCount = new InheritableThreadLocal<Integer>();
	private static InheritableThreadLocal<TestProperties> testProperties = new InheritableThreadLocal<TestProperties>();
	private static InheritableThreadLocal<RemoteWebDriver> driver = new InheritableThreadLocal<RemoteWebDriver>();
	private static InheritableThreadLocal<Map<String, Object>> genericStorage = new InheritableThreadLocal<Map<String, Object>>();

	public static CatchAllExceptionSolver getExceptionSolver() {
		return exceptionSolver.get();
	}

	public static void setExceptionSolver(CatchAllExceptionSolver es) {
		exceptionSolver.set(es);
	}

	public static Integer getRetryCount() {
		return retryCount.get();
	}

	public static void setRetryCount(Integer count) {
		retryCount.set(count);
	}

	public static TestProperties getTestProperties() {
		return testProperties.get();
	}

	public static void setTestProperties(TestProperties props) {
		testProperties.set(props);
	}

	public static RemoteWebDriver getDriver() {
		return driver.get();
	}

	public static void setDriver(RemoteWebDriver webdriver) {
		driver.set(webdriver);
	}

	public static void set(String key, Object value) {
		if (genericStorage.get() == null) {
			genericStorage.set(new HashMap<String, Object>());
		}
		Map<String, Object> storage = genericStorage.get();
		storage.put(key, value);
	}

	public static Object get(String key) {
		Map<String, Object> storage = genericStorage.get();
		return storage.get(key);
	}
}