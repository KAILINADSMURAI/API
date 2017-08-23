package training;

import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.when;

/**
 * Adsmurai Products GET Tests.
 *
 * @author Kailin Chen
 */

public class FacebookGetProductsTest extends BaseApi {

    @Test(groups = { "TRAINING",
            "TRAINING-REGRESSION" }, description = "it_should_persist_if_all_ok")
    public void facebookApiGetProductsTest() {
        test = extent.startTest("FacebookGetProductsTest", "Verify Get Products Call Functionality");
        test.assignCategory("KAILIN REGRESSION");
        test.assignAuthor("Kailin Chen");
        when().get(baseUrl() + "390731147803959/products?" + PRETTY + "=0&" + FIELDS
                + "=product_type,brand&" + LIMIT + "=25&after=" + AFTER + "&access_token=" + TOKEN)
                .then().log().all().statusCode(200).body("data", exists()).and()
                .body("page", exists());
    }
}