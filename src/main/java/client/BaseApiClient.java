package client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.internal.ResponseParserRegistrar;
import io.restassured.internal.ResponseSpecificationImpl;
import io.restassured.internal.TestSpecificationImpl;
import io.restassured.internal.log.LogRepository;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.config.HttpClientConfig.httpClientConfig;

public class BaseApiClient {

    private static final int CONNECTION_MANAGER_TIMEOUT_VALUE = 10000;
    private static final Map<String, String> customHeaders = new HashMap<>();
    private final URI baseURI;
    private final RestAssuredConfig restAssuredConfig;
    private final LogRepository logRepository;
    private final ResponseParserRegistrar responseParserRegistrar;
    private String initialPath = StringUtils.EMPTY;

    public BaseApiClient(String baseUri) {
        try {
            this.baseURI = new URI(baseUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException("An error occurred while creating RestClient, 'baseUri' is incorrect", e);
        }

        logRepository = new LogRepository();
        responseParserRegistrar = new ResponseParserRegistrar();
        restAssuredConfig = getDefaultRestAssuredConfig();
    }

    public RequestSpecification requestSpecification() {

        RequestSpecification requestSpec = new TestSpecificationImpl(
                new RequestSpecificationImpl(this.baseURI.toString(),
                        this.baseURI.getPort(), this.initialPath, RestAssured.DEFAULT_AUTH, new ArrayList<>(), null,
                        RestAssured.DEFAULT_URL_ENCODING_ENABLED, restAssuredConfig, logRepository,
                        null),
                new ResponseSpecificationImpl(RestAssured.DEFAULT_BODY_ROOT_PATH, null, this.responseParserRegistrar,
                        restAssuredConfig, logRepository)).
                getRequestSpecification();

        //default content type, to be overwritten by the client's request method implementation
        requestSpec.contentType(ContentType.JSON);

        requestSpec
                .filter(new ResponseLoggingFilter())
                .filter(new RequestLoggingFilter());

        if (!BaseApiClient.customHeaders.isEmpty()) {
            BaseApiClient.customHeaders.forEach(
                    requestSpec::header
            );
        }

        return requestSpec;
    }

    private RestAssuredConfig getDefaultRestAssuredConfig() {
        return RestAssuredConfig.config()
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        (aClass, s) -> new ObjectMapper()
                                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                .registerModule(new JavaTimeModule())
                ))
                .sslConfig(new SSLConfig().relaxedHTTPSValidation())
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
                .httpClient(
                        httpClientConfig()
                                .setParam("CONNECTION_MANAGER_TIMEOUT_NAME", CONNECTION_MANAGER_TIMEOUT_VALUE));
    }

    public BaseApiClient setInitialPath(String initialPath) {
        this.initialPath = initialPath;
        return this;
    }

    public BaseApiClient setCustomHeaders(String name, String value) {
        BaseApiClient.customHeaders.put(name, value);
        return this;
    }
}