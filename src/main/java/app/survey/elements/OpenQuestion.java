package app.survey.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class OpenQuestion {

    @Getter @Setter int oId;
    @Getter @Setter int elementId;
    @Getter @Setter String elementTitle;
    @Getter @Setter String situation;
    @Getter @Setter String questiontext;
    @Getter @Setter String picture;
    @Getter @Setter int surveyId;
}
