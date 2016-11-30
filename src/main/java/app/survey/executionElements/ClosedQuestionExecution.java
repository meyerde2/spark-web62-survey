package app.survey.executionElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class ClosedQuestionExecution {

        @Getter @Setter int executionId;
        @Getter @Setter int surveyId;
        @Getter @Setter int elementId;
        @Getter @Setter int elementType;
        @Getter @Setter String sessionId;
        @Getter @Setter String ipAddres;
        @Getter @Setter int questionId;

        @Getter @Setter boolean answer1;
        @Getter @Setter boolean answer2;
        @Getter @Setter boolean answer3;
        @Getter @Setter boolean answer4;
        @Getter @Setter boolean answer5;
        @Getter @Setter boolean answer6;
        @Getter @Setter String optionalTextfield;

}
