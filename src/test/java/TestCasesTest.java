import model.LoginRequestBody;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static model.LoginRequestBody.withLoginRequestBody;

public class TestCasesTest {

    private LoginApiClient loginApiClient;
    private TestCasesApiClient testCasesApiClient;

    @BeforeClass
    public void setup() {
        loginApiClient = new LoginApiClient();
        testCasesApiClient = new TestCasesApiClient();

        LoginRequestBody model = withLoginRequestBody();

        String jwtToken = loginApiClient.login(withLoginRequestBody()
                .setEmail("kantarofilip@gmail.com")
                .setPassword("htec123"))
                .then().extract().path("token").toString();

        testCasesApiClient.setCustomHeaders("authorization", "Bearer " + jwtToken);
    }

    @Test
    public void dummy() {


        testCasesApiClient.getTestCases();
    }

}
