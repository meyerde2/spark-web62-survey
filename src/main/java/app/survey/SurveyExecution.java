package app.survey;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class SurveyExecution {

    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;
    @Getter @Setter int elementType;
}
