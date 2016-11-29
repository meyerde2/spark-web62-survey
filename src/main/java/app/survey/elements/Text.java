package app.survey.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Text {

    @Getter @Setter int tId;
    @Getter @Setter int elementId;
    @Getter @Setter String elementTitle;
    @Getter @Setter String text;
    @Getter @Setter String picture;
    @Getter @Setter int surveyId;
}
