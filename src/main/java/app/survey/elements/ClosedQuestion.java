package app.survey.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ClosedQuestion {

    @Getter@Setter int cId;
    @Getter @Setter int elementId;
    @Getter @Setter String elementTitle;
    @Getter @Setter String situation;
    @Getter @Setter String questiontext;
    @Getter @Setter String answer1;
    @Getter @Setter String answer2;
    @Getter @Setter String answer3;
    @Getter @Setter String answer4;
    @Getter @Setter String answer5;
    @Getter @Setter String answer6;
    @Getter @Setter boolean optionalTextfield;
    @Getter @Setter boolean multipleSelection;
    @Getter @Setter String picture;
    @Getter @Setter int surveyId;

}