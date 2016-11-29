package app.survey.elements;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ScoreTable {
    @Getter @Setter int sId;
    @Getter @Setter int elementId;
    @Getter @Setter String elementTitle;
    @Getter @Setter String situation;
    @Getter @Setter String questiontext;
    @Getter @Setter String criterion1;
    @Getter @Setter String criterion2;
    @Getter @Setter String criterion3;
    @Getter @Setter String criterion4;
    @Getter @Setter String criterion5;
    @Getter @Setter String criterion6;
    @Getter @Setter String picture;
    @Getter @Setter int surveyId;
}
