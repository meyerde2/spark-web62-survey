package app.evaluation.elements;

import app.evaluation.elements.helper.ScoreTableAnswerCounter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class ScoreTableEvaluation {

    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;

    @Getter @Setter ScoreTableAnswerCounter scoreTableAnswerCounter;
}
