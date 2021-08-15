import client.BaseApiClient;
import client.GlobalApiProperties;
import io.restassured.response.Response;
import model.LoginRequestBody;

public class LoginApiClient extends BaseApiClient {

    private static final String API_PATH = "/api/candidate";
    private static GlobalApiProperties globalProperties = GlobalApiProperties.getInstance();

    public LoginApiClient() {
        super(globalProperties.get("app.test.htec.base.uri"));
        setInitialPath(API_PATH);
    }

    public Response login(LoginRequestBody requestBody) {
        return requestSpecification()
                .body(requestBody)
                .post("/login");
    }
}
