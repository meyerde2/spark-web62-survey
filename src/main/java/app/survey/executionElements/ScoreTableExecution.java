package app.survey.executionElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class ScoreTableExecution {

    @Getter @Setter int executionId;
    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;
    @Getter @Setter int elementType;
    @Getter @Setter String sessionId;
    @Getter @Setter String ipAddres;
    @Getter @Setter int questionId;

    @Getter @Setter int criterion1;
    @Getter @Setter int criterion2;
    @Getter @Setter int criterion3;
    @Getter @Setter int criterion4;
    @Getter @Setter int criterion5;
    @Getter @Setter int criterion6;
}