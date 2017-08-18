package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.BaseApiTest;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Test(singleThreaded = true)
public class Wiremock extends BaseApiTest {

	public Wiremock() {
		configureFor("Pending to define", 9999);
	}

	@Override
	protected String baseUrl() {
		return "Pending to define";
	}

	@BeforeMethod
	public void cleanWireMockBefore() {
		resetAllRequests();
	}

	@Test
	public void sampleTest() {
		when().get(baseUrl() + "/test.html").then().assertThat().body(containsString("WireMock is Alive!"));
		verify(getRequestedFor(urlEqualTo("/test.html")));
	}

	@AfterMethod
	public void cleanWireMockAfter() {
		resetAllRequests();
	}
}