package training;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import java.io.File;
import java.util.Date;
import org.hamcrest.Matcher;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import base.BaseApiTest;
import base.RestAssuredRequestLogger;
import base.RestAssuredResponseLogger;

public abstract class BaseApi extends BaseApiTest {

	/**
	 * Base Test class for Adsmurai Feeds tests.
	 *
	 * @author kailin.chen
	 */

	protected static final String SERVERPORT = "";
	protected static final String QAENV = "https://graph.facebook.com/v2.9/";
	protected static final int GENERATENUM = Math.round(new Date().getTime() / 1000);
	protected static final String CONTENTTYPE = "application/json";
	protected static final String TOKEN = "EAAUaRqcSjngBAAnWLZAOfAhcHZBYoxJnOcSOOX3FRxNS3CEGUm6sksgYbNTmKCtuW5hmsiACZA0FskAZA6TwzNpPqsUp4WurwlGKRLLHp1kZAiXrc1ZCReyB56DnEJuCPSZBoRvNYFSW0vjO2Opdr8zZBvBwfxewzDTa9OV6WisfQtaHBv84qs3L";
	protected static final String AFTER = "eyJvZAmZAzZAXQiOjEyNCwidGllciI6InByb2R1Y3RzLnByb2R1Y3RzX3NlYXJjaC5hdG4ifQZDZD";
	protected static final String PRETTY = "pretty";
	protected static final String FIELDS = "fields";
	protected static final String LIMIT = "limit";
	protected ExtentReports extent;
	protected ExtentTest test;

	@BeforeMethod(alwaysRun = true)
	protected void setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/extendreports.html", true);
		extent.addSystemInfo("Host Name", "Kailin Testing Material")
				.addSystemInfo("Environment", "Kailin Automation Testing").addSystemInfo("User Name", "Kailin Chen");
		extent.loadConfig(new File(System.getProperty("user.dir") + "/extent-config.xml"));
	}

	/**
	 * Tear down method for closing all connections, stopping webdriver, etc.
	 */
	@AfterMethod(alwaysRun = true)
	protected void tearDown() {
		extent.endTest(test);
		extent.flush();
		extent.close();
	}

	protected Matcher<Object> exists() {
		return notNullValue();
	}

	protected Matcher<Object> existsvalue(String string) {
		return notNullValue();
	}

	protected String baseUrl() {
		return "https://graph.facebook.com/v2.9/";
	}

	protected RequestSpecification setupBapi() {
		return given().contentType(CONTENTTYPE).filter(new RestAssuredRequestLogger(L()))
				.filter(new RestAssuredResponseLogger(L()));
	}
}