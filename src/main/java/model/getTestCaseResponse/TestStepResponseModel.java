package model.getTestCaseResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TestStepResponseModel {

    private Long id;
    private Long testcase_id;
    private String value;
}
