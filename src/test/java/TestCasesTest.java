import model.getTestCaseResponse.TestCaseResponseModel;
import model.updateTestCaseRequest.TestCaseUpdateRequestModel;
import model.updateTestCaseRequest.TestStepUpdateModel;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static model.LoginRequestBody.withLoginRequestBody;
import static model.updateTestCaseRequest.TestCaseUpdateRequestModel.updateRequestModel;
import static org.apache.http.HttpStatus.SC_OK;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class TestCasesTest {

    private LoginApiClient loginApiClient;
    private TestCasesApiClient testCasesApiClient;

    @BeforeClass
    public void setup() {
        loginApiClient = new LoginApiClient();
        testCasesApiClient = new TestCasesApiClient();

        String jwtToken = loginApiClient.login(withLoginRequestBody()
                .setEmail("kantarofilip@gmail.com")
                .setPassword("htec123"))
                .then().extract().path("token").toString();

        testCasesApiClient.setCustomHeaders("authorization", "Bearer " + jwtToken);
    }

    @Test
    public void shouldBeAbleToEditAllFields() {

        List<TestCaseResponseModel> testCases = asList(testCasesApiClient.getTestCases()
                .then()
                .extract().body().as(TestCaseResponseModel[].class));

        testCases.forEach(testCase ->
                testCasesApiClient.updateTestCase(testCase.getId(), editAllTheFields(testCase))
                        .then().assertThat().statusCode(SC_OK));
    }

    private TestCaseUpdateRequestModel editAllTheFields(TestCaseResponseModel testCase) {

        String newString = "This field previously  had % characters";

        return updateRequestModel()
                .setTitle(StringUtils.replace(newString, "%", String.valueOf(testCase.getTitle().length())))
                .setDescription(StringUtils.replace(newString, "%", String.valueOf(testCase.getDescription().length())))
                .setExpected_result(StringUtils.replace(newString, "%", String.valueOf(testCase.getExpected_result().length())))
                .setAutomated(testCase.isAutomated())
                .setCandidate_scenario_id(testCase.getCandidate_scenario_id())
                .setTest_steps(testCase.getTest_steps()
                        .stream()
                        .map(step -> new TestStepUpdateModel()
                                .setId(step.getId())
                                .setValue(StringUtils.replace(newString, "%", String.valueOf(step.getValue().length()))))
                        .collect(Collectors.toList()));
    }
}
