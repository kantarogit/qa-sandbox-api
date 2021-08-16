package model.updateTestCaseRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor(staticName = "updateRequestModel")
@Accessors(chain = true)
public class TestCaseUpdateRequestModel {

    private String title;
    private String description;
    private String expected_result;
    private List<TestStepUpdateModel> test_steps;
    private boolean automated;
    private Long candidate_scenario_id;
}
