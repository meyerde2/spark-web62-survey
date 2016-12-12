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

    @Getter @Setter Integer answer1;
    @Getter @Setter Integer answer2;
    @Getter @Setter Integer answer3;
    @Getter @Setter Integer answer4;
    @Getter @Setter Integer answer5;
    @Getter @Setter Integer answer6;
    @Getter @Setter Integer surveyCounterId;

}
