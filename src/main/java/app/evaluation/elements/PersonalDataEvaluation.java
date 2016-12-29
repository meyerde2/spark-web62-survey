package app.evaluation.elements;

import app.evaluation.elements.helper.LocationCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@AllArgsConstructor
public class PersonalDataEvaluation {

    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;

    @Getter @Setter Integer ageMin;
    @Getter @Setter Integer ageMax;
    @Getter @Setter double ageMedian;
    @Getter @Setter double ageAverage;
    @Getter @Setter double standardDeviation;


    @Getter @Setter List<Integer> ages;
    @Getter @Setter Integer maleCounter;
    @Getter @Setter Integer femaleCounter;
    @Getter @Setter List<LocationCount> locationCount;


}
