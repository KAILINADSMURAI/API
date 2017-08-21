package base;

import com.jayway.restassured.specification.RequestSpecification;

public class RequestSpecificationBuilder {

    private RequestSpecification spec;

    public RequestSpecificationBuilder(RequestSpecification spec) {
        this.spec = spec;
    }

    public RequestSpecificationBuilder multiPart(String name, String value) {
        if (value != null)
            spec.multiPart(name, value);
        return this;
    }

    public RequestSpecification build() {
        return spec;
    }

    public RequestSpecificationBuilder multiPart(String name, Integer value) {
        if (value != null)
            spec.multiPart(name, value);
        return this;
    }
}