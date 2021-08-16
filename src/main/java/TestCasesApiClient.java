import client.BaseApiClient;
import client.GlobalApiProperties;
import io.restassured.response.Response;
import model.updateTestCaseRequest.TestCaseUpdateRequestModel;

public class TestCasesApiClient extends BaseApiClient {

    private static final String API_PATH = "/api/candidate";
    private static GlobalApiProperties globalProperties = GlobalApiProperties.getInstance();

    public TestCasesApiClient() {
        super(globalProperties.get("app.test.htec.base.uri"));
        setInitialPath(API_PATH);
    }

    public Response getTestCases() {
        return requestSpecification()
                  .get("/testcases");
    }

    public Response updateTestCase(Long testCaseId, TestCaseUpdateRequestModel requestBody) {
        return requestSpecification()
                .pathParam("id", testCaseId)
                .body(requestBody)
                .put("/testcases/{id}");
    }
}
