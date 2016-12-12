package app.evaluation;


import app.evaluation.elements.helper.ClosedAnswerCounter;
import app.evaluation.elements.helper.LocationCount;
import app.evaluation.elements.helper.ScoreTableAnswerCounter;
import app.survey.SurveyElement;

import java.util.List;

public interface EvaluationDao {

    int getPlayCounterForSurvey(int surveyId);

    List<SurveyElement> getTerminationQuestions();

    //Text?

    //Persönliche Daten
    List<LocationCount> getCountOfDifferentLocations(int surveyId, int elementId);
    List<Integer> getAllAges(int surveyId, int elementId);


    //Auswertung: geschlossene Frage
    ClosedAnswerCounter getCountOfClosedQuestionAnswers(int surveyId, int elementId);
    List<String> getOptionalTextfieldAnswersClosedQuestion(int surveyId, int elementId);

    //Auswertung: offene Frage
    List<String> getOpenQuestionEvaluation(int surveyId, int elementId);

    //Auswertung: Bewertungstabelle
    ScoreTableAnswerCounter getCountOfScoreTableAnswers(int surveyId, int elementId);




}
