package app.evaluation.elements;

import app.evaluation.elements.helper.ClosedAnswerCounter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class ClosedQuestionEvaluation {
    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;

    @Getter @Setter ClosedAnswerCounter closedAnswerCounter;
    @Getter @Setter List<String> optionalTextfield;
}
