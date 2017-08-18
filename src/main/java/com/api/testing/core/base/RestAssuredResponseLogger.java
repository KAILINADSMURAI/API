package base;

import org.apache.logging.log4j.Logger;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.internal.support.Prettifier;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;

public class RestAssuredResponseLogger implements Filter {

	private Logger logger;

	public RestAssuredResponseLogger(Logger logger) {
		this.logger = logger;
	}

	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext ctx) {
		Response response = ctx.next(requestSpec, responseSpec);
		StringBuilder builder = new StringBuilder();
		builder.append(response.statusLine()).append("\n");
		builder.append(response.headers()).append("\n");
		builder.append(response.detailedCookies()).append("\n");
		String prettyBody = new Prettifier().getPrettifiedBodyIfPossible(response, response);
		builder.append(prettyBody).append("\n");
		logger.info("Response data: " + builder.toString());
		return response;
	}
}