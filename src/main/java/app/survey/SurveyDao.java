package app.survey;

import app.survey.elements.ClosedQuestion;
import app.survey.elements.OpenQuestion;
import app.survey.elements.PersonalData;
import app.survey.elements.Text;

import java.util.List;

/**
 * Created by Dennis on 22.11.2016.
 */
public interface SurveyDao {

    List<Survey> getAllSurveys(int userId);
    List<SurveyElement> getAllSurveyElements(int surveyId);
    Survey getSurveyById(int surveyId);
    boolean createNewSurvey(Survey survey);
    boolean updateSurvey(Survey survey);
    boolean deleteSurvey(Survey survey);
    int createNewSurveyElement(SurveyElement surveyElement);
    boolean createNewOpenQuestion(OpenQuestion openQuestion);
    int getLatestSurveyId(int userId);

    boolean createTextElement(Text text);
    boolean createPersonalDataElement(PersonalData personalData);
    boolean createClosedQuestion(ClosedQuestion closedQuestion);
}
