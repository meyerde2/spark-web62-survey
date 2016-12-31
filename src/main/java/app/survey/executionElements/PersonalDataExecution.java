package app.survey.executionElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class PersonalDataExecution {

    @Getter @Setter int executionId;
    @Getter @Setter int surveyId;
    @Getter @Setter int elementId;
    @Getter @Setter int elementType;
    @Getter @Setter String sessionId;
    @Getter @Setter String ipAddres;
    @Getter @Setter int questionId;

    @Getter @Setter String firstname;
    @Getter @Setter String lastname;
    @Getter @Setter Integer age;
    @Getter @Setter Integer gender; //1 = m; 2 = w
    @Getter @Setter String location;
    @Getter @Setter int surveyCounterId;


}
