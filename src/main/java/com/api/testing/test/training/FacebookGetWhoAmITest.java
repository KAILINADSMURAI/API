package training;

import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import static org.hamcrest.Matchers.equalTo;
import static com.jayway.restassured.RestAssured.when;

/**
 * Adsmurai Feeds GET Tests.
 *
 * @author Kailin Chen
 */

public class FacebookGetWhoAmITest extends BaseAdsmuraiFeedsApi {

	@Test(groups = { "FEEDS", "FEEDS-REGRESSION" }, description = "it_should_persist_if_all_ok")
	public void facebookApiGetWhoAmITest() {
		test = extent.startTest("KAILIN TEST", "Verify Get Who Am I Call Functionality");
		test.assignCategory("KAILIN REGRESSION");
		test.assignAuthor("Kailin Chen");
		when().get(baseUrl() + "105613900027906?" + "access_token=" + TOKEN).then().log().all().statusCode(200)
				.body("name", equalTo("Kailin Chen")).and().body("id", equalTo("105613900027906"));
		test.log(LogStatus.PASS, "Facebook Get Who Am I API Call Works As Expected");
	}
}