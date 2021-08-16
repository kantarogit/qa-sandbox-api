package model.getTestCaseResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TestCaseResponseModel {

    private Long id;
    private Long candidate_id;
    private String title;
    private String expected_result;
    private String description;
    private boolean automated;
    private Long candidate_scenario_id;
    private List<TestStepResponseModel> test_steps;
}
