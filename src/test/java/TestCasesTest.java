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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestCasesTest {
    /***************** IMPORTANT *********************************
     * >>>> REQUIRES AT LEAST ONE TEST CASE CREATED <<<<
     ************************************************************/
    private LoginApiClient loginApiClient;
    private TestCasesApiClient testCasesApiClient;

    @BeforeClass
    public void setup() {
        loginApiClient = new LoginApiClient();
        testCasesApiClient = new TestCasesApiClient();

        // this should not be kept on github :) better approach some ENV variable or .properties/dotenv file kept locally
        String jwtToken = loginApiClient.login(withLoginRequestBody()
                .setEmail("kantarofilip@gmail.com")
                .setPassword("htec123"))
                .then().extract().path("token").toString();

        // all subsequent calls are going to use this token
        testCasesApiClient.setCustomHeaders("authorization", "Bearer " + jwtToken);
    }

    @Test
    // example of bad test: if no test cases present the action UPDATE wont be even invoked and the test will be marked
    // as PASSED. Can cause false negative.
    public void shouldBeAbleToEditAllFields() {

        List<TestCaseResponseModel> testCases = asList(testCasesApiClient.getTestCases()
                .then()
                .extract().body().as(TestCaseResponseModel[].class));

        testCases.forEach(testCase ->
                testCasesApiClient.updateTestCase(testCase.getId(), editAllTheFields(testCase))
                        .then().assertThat().statusCode(SC_OK));

        // when test case updated it is better to check the response in this case or even perform a GET and check updated fields
    }

    @Test
    // same here, without proper setup this case can lead to false negative
    public void shouldDeleteAllTestCases() {
        List<TestCaseResponseModel> testCases = asList(testCasesApiClient.getTestCases()
                .then()
                .extract().body().as(TestCaseResponseModel[].class));

        testCases.forEach(testCase ->
                testCasesApiClient.deleteTestCase(testCase.getId())
                        .then().assertThat().statusCode(SC_OK));

        testCases = asList(testCasesApiClient.getTestCases()
                .then()
                .extract().body().as(TestCaseResponseModel[].class));

        assertThat(testCases.size(), is(0));
    }

    // prepare UPDATE request body
    private TestCaseUpdateRequestModel editAllTheFields(TestCaseResponseModel testCase) {

        String newString = "This field previously had % characters";

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
