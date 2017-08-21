package base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public abstract class BaseApiTest extends BaseTest {

    private enum ApiPostMode {
        JSON, FORM
    };

    private enum ApiHttpMethod {
        POST, GET, DELETE, PUT
    };

    private final static ApiPostMode DEFAULT_API_POST_MODE = ApiPostMode.FORM;
    private final static ApiHttpMethod DEFAULT_API_HTTP_METHOD = ApiHttpMethod.POST;

    protected InheritableThreadLocal<ApiPostMode> apiPostMode = new InheritableThreadLocal<ApiPostMode>();
    protected InheritableThreadLocal<ApiHttpMethod> apiHttpMethod = new InheritableThreadLocal<ApiHttpMethod>();
    private InheritableThreadLocal<WebResource> service = new InheritableThreadLocal<WebResource>();
    protected InheritableThreadLocal<HashMap<String, Object>> params = new InheritableThreadLocal<HashMap<String, Object>>();

    @Override
    protected void specificSetup(Method m) {
        super.specificSetup(m);
        Client client = Client.create();
        client.setConnectTimeout(20000);
        WebResource service = client.resource(baseUrl());
        this.service.set(service);
        this.params.set(new HashMap<String, Object>());
        this.apiPostMode.set(DEFAULT_API_POST_MODE);
        this.apiHttpMethod.set(DEFAULT_API_HTTP_METHOD);
    }

    protected JSONObject getJson(String method, Map<String, String> params)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        ClientResponse response = doApiGetCall(method, params);
        return clientResponseToJSONObject(response);
    }

    protected JSONObject postJson(String method)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        ClientResponse response = doApiPostCall(method);
        return clientResponseToJSONObject(response);
    }

    protected JSONObject getJson(String method)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        return getJson(method, null);
    }

    protected JSONArray getArray(String method, Map<String, String> params)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        ClientResponse response = doApiGetCall(method, params);
        return clientResponseToJSONArray(response);
    }

    protected JSONArray postArray(String method)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        ClientResponse response = doApiPostCall(method);
        return clientResponseToJSONArray(response);
    }

    protected JSONArray getArray(String method)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        return getArray(method, null);
    }

    private ClientResponse doApiPostCall(String method) {
        logComment("Endpoint: " + service.get().getURI());
        logComment("Calling POST API with method \"" + method + "\", params = \"" + params.get()
                + "\".");
        Builder builder = service.get().path(method).getRequestBuilder();

        for (Cookie cookie : getCookies()) {
            builder.cookie(cookie);
        }

        ClientResponse response;
        if (apiPostMode.get().equals(ApiPostMode.FORM)) {
            response = doFormPost(builder);
        } else if (apiPostMode.get().equals(ApiPostMode.JSON)) {
            response = doJsonPost(builder);
        } else {
            throw new RuntimeException("Bad API post mode. This error should not happen never.");
        }

        return response;
    }

    private ClientResponse doFormPost(Builder builder) {
        Form form = new Form();
        if (!params.get().isEmpty()) {
            for (Map.Entry<String, Object> param : params.get().entrySet()) {
                form.add(param.getKey(), param.getValue());
            }
        }

        ClientResponse response = builder.type(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, form);

        return response;
    }

    private ClientResponse doJsonPost(Builder builder) {
        try {
            JSONObject json = new JSONObject();
            if (!params.get().isEmpty()) {
                for (Map.Entry<String, Object> param : params.get().entrySet()) {
                    json.put(param.getKey(), param.getValue());
                }
            }

            ClientResponse response = builder.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, json.toString());
            return response;

        } catch (JSONException ex) {
            throw new RuntimeException("Exception when creating post parameters json.", ex);
        }
    }

    private ClientResponse doApiGetCall(String method, Map<String, String> params) {
        logComment("Endpoint: " + service.get().getURI());
        logComment("Calling GET API with method \"" + method + "\", params = \"" + params + "\".");

        // Add parameters to the query if necessary (GET parameters)
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        HashMap<String, Object> customParams = this.params.get();
        for (Map.Entry<String, Object> currentParam : customParams.entrySet()) {
            queryParams.add(currentParam.getKey(), currentParam.getValue().toString());
        }
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                queryParams.add(param.getKey(), param.getValue());
            }
        }

        this.service.set(service.get().queryParams(queryParams));
        Builder builder = service.get().path(method).getRequestBuilder();

        for (Cookie cookie : getCookies()) {
            builder.cookie(cookie);
        }

        ClientResponse response = builder.get(ClientResponse.class);
        return response;
    }

    private JSONObject clientResponseToJSONObject(ClientResponse response)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        String responseString = response.getEntity(String.class);
        JSONObject json = new JSONObject(responseString);
        logComment("Answer JSON: " + json.toString(2));

        return json;
    }

    private JSONArray clientResponseToJSONArray(ClientResponse response)
            throws ClientHandlerException, UniformInterfaceException, JSONException {
        String resp = response.getEntity(String.class);
        JSONArray json = new JSONArray(resp);
        logComment("Answer JSON: " + json.toString(2));

        return json;
    }

    protected void addParameter(String key, Object value) {
        if (value != null) {
            params.get().put(key, value);
        }
    }

    protected void clearParameters() {
        params.get().clear();
    }

    protected ArrayList<Cookie> getCookies() {
        return new ArrayList<Cookie>();
    }

    protected abstract String baseUrl();

    protected void useJsonPostMode() {
        this.apiPostMode.set(ApiPostMode.JSON);
    }

    protected void useFormPostMode() {
        this.apiPostMode.set(ApiPostMode.FORM);
    }
}