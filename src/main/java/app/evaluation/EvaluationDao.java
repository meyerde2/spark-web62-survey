package app.evaluation;


import app.survey.SurveyElement;

import java.util.List;

public interface EvaluationDao {

    Evaluation getSurveyEvaluationById(int surveyId);

    int getPlayCounterForSurvey(int surveyId);

    List<SurveyElement> getTerminationQuestions();

    int getCountAnswer1ClosedQuestion(int surveyId);
    int getCountAnswer2ClosedQuestion(int surveyId);
    int getCountAnswer3ClosedQuestion(int surveyId);
    int getCountAnswer4ClosedQuestion(int surveyId);
    int getCountAnswer5ClosedQuestion(int surveyId);
    int getCountAnswer6ClosedQuestion(int surveyId);
    List<String> getOptionalTextfieldAnswersClosedQuestion(int surveyId);

}
