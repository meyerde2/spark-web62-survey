package app.evaluation;

import app.Application;
import app.evaluation.elements.ClosedQuestionEvaluation;
import app.evaluation.elements.OpenQuestionEvaluation;
import app.evaluation.elements.PersonalDataEvaluation;
import app.evaluation.elements.ScoreTableEvaluation;
import app.login.LoginController;
import app.survey.SurveyElement;
import app.util.Path;
import app.util.ViewUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.Application.*;
import static app.util.JsonUtil.dataToJson;
import static app.util.RequestUtil.*;


public class EvaluationController {

    public static Route serveEvaluationPage = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("EvaluationController serveEvaluationPage");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "evaluation");

            int surveyId = Integer.parseInt(getParamId(request));

            System.out.println("SurveyId=  " + surveyId);
            int elementId = 1;

            List<SurveyElement> surveyElementList = surveyDao.getAllSurveyElements(surveyId);

            // TextElemente entfernen
            surveyElementList.removeIf((SurveyElement element) -> element.getElementType() == 1);


            List<PersonalDataEvaluation> personalDataEvaluationList = new ArrayList<>();
            List<ClosedQuestionEvaluation> closedQuestionEvaluationList = new ArrayList<>();

            List<OpenQuestionEvaluation> openQuestionEvaluationList = new ArrayList<>();
            List<ScoreTableEvaluation> scoreTableEvaluationList = new ArrayList<>();

            if (evaluationDao.getExecutionCounterForSurvey(surveyId) != 0){

                for (SurveyElement element :surveyElementList) {
                    elementId = element.getElementId();

                    switch (element.getElementType()){
                        case 1:
                            // nicht nötig die Text-Klicks hier zu holen?!?
                            break;
                        case 2:

                            List<Integer> allAges = evaluationDao.getAllAges(surveyId, elementId);
                            PersonalDataEvaluation personalDataEvaluation;

                            if(allAges != null && allAges.size() > 0){
                                //Median
                                double median = 0;

                                if(allAges.size() > 2){
                                    if (allAges.size() % 2 == 0){
                                        int i1 = (int)((allAges.size() / 2) - 0.5);
                                        int i2 = i1 -1;
                                        median = allAges.get(i1) + allAges.get(i2) / 2;
                                    }else{
                                        median = allAges.get(allAges.size()/2);

                                    }
                                }

                                //Arithmetische Mittel
                                double ageAverage = 0;
                                for (int i: allAges) {
                                    ageAverage += i;
                                }

                                ageAverage = (ageAverage / allAges.size());

                                //Standabweichung | standardDeviation
                                double standardDeviation = 0.0;

                                for(int i = 0; i < allAges.size(); i++){
                                    standardDeviation += (allAges.get(i) - ageAverage) * (allAges.get(i)  - ageAverage);
                                }
                                standardDeviation = Math.sqrt(standardDeviation / (allAges.size() - 1.0));

                                personalDataEvaluation = new PersonalDataEvaluation(surveyId, elementId, allAges.get(0), allAges.get(allAges.size()-1), median, ageAverage, standardDeviation, allAges, evaluationDao.getMaleCount(surveyId, elementId), evaluationDao.getFemaleCount(surveyId, elementId), evaluationDao.getCountOfDifferentLocations(surveyId, elementId));

                            }else{

                                personalDataEvaluation = new PersonalDataEvaluation(surveyId, elementId, null, null, 0.0, 0.0, 0.0, allAges, evaluationDao.getMaleCount(surveyId, elementId), evaluationDao.getFemaleCount(surveyId, elementId), evaluationDao.getCountOfDifferentLocations(surveyId, elementId));

                            }

                            personalDataEvaluationList.add(personalDataEvaluation);
                            break;
                        case 3:
                            ClosedQuestionEvaluation closedQuestionEvaluation = new ClosedQuestionEvaluation(surveyId, elementId,
                                    evaluationDao.getCountOfClosedQuestionAnswers(surveyId, elementId), evaluationDao.getOptionalTextfieldAnswersClosedQuestion(surveyId, elementId));
                            closedQuestionEvaluationList.add(closedQuestionEvaluation);
                            break;
                        case 4:
                            OpenQuestionEvaluation openQuestionEvaluation = new OpenQuestionEvaluation(surveyId, elementId, evaluationDao.getOpenQuestionEvaluation(surveyId, elementId));
                            openQuestionEvaluationList.add(openQuestionEvaluation);
                            break;
                        case 5:
                            ScoreTableEvaluation scoreTableEvaluation = new ScoreTableEvaluation(surveyId, elementId, evaluationDao.getCountOfScoreTableAnswers(surveyId, elementId));
                            scoreTableEvaluationList.add(scoreTableEvaluation);
                            break;

                    }

                }
            }

            System.out.println("closedQuestionEvaluationList " +closedQuestionEvaluationList.toString());
            attributes.put("surveyElementList", surveyElementList);

            attributes.put("personalDataEvaluationList", personalDataEvaluationList);
            attributes.put("closedQuestionEvaluationList", closedQuestionEvaluationList);
            attributes.put("openQuestionEvaluationList", openQuestionEvaluationList);
            attributes.put("scoreTableEvaluationList", scoreTableEvaluationList);


            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.EVALUATION));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
