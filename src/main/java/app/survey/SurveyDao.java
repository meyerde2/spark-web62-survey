package app.survey;

import app.survey.elements.*;

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
    int getLatestSurveyId(int userId);

    boolean createTextElement(Text text);
    boolean createPersonalDataElement(PersonalData personalData);
    boolean createClosedQuestion(ClosedQuestion closedQuestion);
    boolean createOpenQuestion(OpenQuestion openQuestion);
    boolean createScoreTableQuestion(ScoreTable scoreTable);

    boolean updateTextElement(Text text);
    boolean updatePersonalDataElement(PersonalData personalData);
    boolean updateClosedQuestion(ClosedQuestion closedQuestion);
    boolean updateOpenQuestion(OpenQuestion openQuestion);
    boolean updateScoreTableQuestion(ScoreTable scoreTable);

    SurveyElement getSurveyElementById(int surveyId, int elementId);

    boolean deleteSurveyElementById(int surveyId, int elementId);

    Text getTextElement(int elementId);
    PersonalData getPersonalData(int elementId);
    ClosedQuestion getClosedQuestion(int elementId);
    OpenQuestion getOpenedQuestion(int elementId);
    ScoreTable getScoreTable(int elementId);

}
