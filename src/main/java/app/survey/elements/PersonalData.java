package app.survey.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class PersonalData {

    @Getter @Setter int pId;
    @Getter @Setter int elementId;
    @Getter @Setter String elementTitle;
    @Getter @Setter boolean isFirstname;
    @Getter @Setter boolean isLastname;
    @Getter @Setter boolean isAge;
    @Getter @Setter boolean isGender;
    @Getter @Setter boolean isLocation;
    @Getter @Setter int surveyId;

}
