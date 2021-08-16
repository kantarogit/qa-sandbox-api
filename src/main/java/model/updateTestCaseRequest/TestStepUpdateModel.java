package model.updateTestCaseRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TestStepUpdateModel {

    private Long id;
    private String value;
}
