package app.survey.executionElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class TextExecution {

    @Getter @Setter int executionId;
    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;
    @Getter @Setter int elementType;
    @Getter @Setter String sessionId;
    @Getter @Setter String ipAddres;
    @Getter @Setter int questionId;


}
