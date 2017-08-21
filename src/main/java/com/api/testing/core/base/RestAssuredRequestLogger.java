package base;

import org.apache.logging.log4j.Logger;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;

public class RestAssuredRequestLogger implements Filter {

    private Logger logger;

    public RestAssuredRequestLogger(Logger logger) {
        this.logger = logger;
    }

    public Response filter(FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec, FilterContext ctx) {
        String requestParams = requestSpec.getRequestParams().toString();
        String requestQueryParams = requestSpec.getQueryParams().toString();
        String requestFormParams = requestSpec.getFormParams().toString();
        String requestPathParams = requestSpec.getPathParams().toString();
        String requestMultipartParams = requestSpec.getMultiPartParams().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("REQUEST_PARAMS=" + requestParams).append("\n");
        sb.append("REQUEST_QUERY_PARAMS=" + requestQueryParams).append("\n");
        sb.append("REQUEST_FORM_PARAMS=" + requestFormParams).append("\n");
        sb.append("REQUEST_PATH_PARAMS=" + requestPathParams).append("\n");
        sb.append("REQUEST_MULTIPART_PARAMS=" + requestMultipartParams).append("]");
        logger.info("Request data: " + sb.toString());
        return ctx.next(requestSpec, responseSpec);
    }
}