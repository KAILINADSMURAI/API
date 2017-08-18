package base;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.when;

public class TestUtils {
	public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String query = url.getQuery();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	public static boolean isValidLink(String link) {
		return isValidLink(link, 200);
	}

	public static boolean isValidLink(String link, Integer expectedStatusCode) {
		try {
			when().get(link).then().statusCode(expectedStatusCode);
		} catch (AssertionError ex) {
			return false;
		}
		return true;
	}

	public static void threadPause(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException("Error while thread sleeping", e);
		}
	}
}