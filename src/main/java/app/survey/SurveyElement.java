package app.survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class SurveyElement {

    @Getter @Setter int elementId;
    @Getter @Setter int surveyId;
    @Getter @Setter int elementType;
    //Not part of database relation:
    @Getter @Setter String elementTitle;

    public SurveyElement(int elementId, int surveyId, int elementType){
        this.elementId = elementId;
        this.surveyId = surveyId;
        this.elementType = elementType;
    }
}
