package training;

import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static com.jayway.restassured.RestAssured.when;

/**
 * Adsmurai Who Am I GET Tests.
 *
 * @author Kailin Chen
 */

public class FacebookGetWhoAmITest extends BaseApi {

    @Test(groups = { "TRAINING",
            "TRAINING-REGRESSION" }, description = "it_should_persist_if_all_ok")
    public void facebookApiGetWhoAmITest() {
        test = extent.startTest("KAILIN TEST", "Verify Get Who Am I Call Functionality");
        test.assignCategory("KAILIN REGRESSION");
        test.assignAuthor("Kailin Chen");
        when().get(baseUrl() + "105613900027906?" + "access_token=" + TOKEN).then().log().all()
                .statusCode(200).body("name", equalTo("Kailin Chen")).and()
                .body("id", equalTo("105613900027906"));
    }
}