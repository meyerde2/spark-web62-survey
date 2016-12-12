package app.evaluation.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class OpenQuestionEvaluation {

    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;

    @Getter @Setter List<String> text;
}
