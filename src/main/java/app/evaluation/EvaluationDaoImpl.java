package app.evaluation;


import app.evaluation.elements.helper.ClosedAnswerCounter;
import app.evaluation.elements.helper.LocationCount;
import app.evaluation.elements.helper.ScoreTableAnswerCounter;
import app.survey.SurveyElement;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class EvaluationDaoImpl implements EvaluationDao {

    private Sql2o sql2o;

    public EvaluationDaoImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public int getPlayCounterForSurvey(int surveyId) {
        int count;

        String sql = "SELECT COUNT(surveyExecutionId) FROM execution_multipleexecutions;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public int getExecutionCounterForSurvey(int surveyId) {
        int count;
        String sql = "SELECT `surveyCounterId` FROM `execution_counter` WHERE  `surveyId` = " + surveyId + " AND `end`= 1";

        try (Connection con = sql2o.open()) {
            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());
        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public List<SurveyElement> getTerminationQuestions() {

        List<SurveyElement> terminatedQuestions = null;
        String sql = "SELECT execution_survey.elementId, execution_survey.surveyId, execution_survey.elementType, surveyelementtitle.elementTitle " +
                "FROM execution_survey LEFT JOIN surveyelementtitle ON execution_survey.elementId = surveyelementtitle.elementId " +
                "WHERE surveyExecutionId = 0";

        try (Connection con = sql2o.open()) {

            terminatedQuestions = con.createQuery(sql).executeAndFetch(SurveyElement.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return terminatedQuestions;
    }

    @Override
    public ClosedAnswerCounter getCountOfClosedQuestionAnswers(int surveyId, int elementId) {
        List<ClosedAnswerCounter> closedAnswerCounters=  null;

        String sql = "SELECT " +
                "SUM(CASE WHEN execution_closedquestion.answer1 = 1 THEN 1 ELSE 0 END) AS answer1c, " +
                "SUM(CASE WHEN execution_closedquestion.answer2 = 1 THEN 1 ELSE 0 END) AS answer2c, " +
                "SUM(CASE WHEN execution_closedquestion.answer3 = 1 THEN 1 ELSE 0 END) AS answer3c, " +
                "SUM(CASE WHEN execution_closedquestion.answer4 = 1 THEN 1 ELSE 0 END) AS answer4c, " +
                "SUM(CASE WHEN execution_closedquestion.answer5 = 1 THEN 1 ELSE 0 END) AS answer5c, " +
                "SUM(CASE WHEN execution_closedquestion.answer6 = 1 THEN 1 ELSE 0 END) AS answer6c, " +
                "SUM(CASE WHEN execution_closedquestion.optionalTextfield <> 'NULL' THEN 1 ELSE 0 END) AS answerOtherc, " +
                "closedquestion.answer1, " +
                "closedquestion.answer2, " +
                "closedquestion.answer3, " +
                "closedquestion.answer4, " +
                "closedquestion.answer5, " +
                "closedquestion.answer6 " +
                "FROM execution_closedquestion LEFT JOIN closedquestion ON execution_closedquestion.elementId = closedquestion.elementId " +
                "WHERE execution_closedquestion.surveyId = " + surveyId + " AND execution_closedquestion.elementId= " + elementId +" ;";

        try (Connection con = sql2o.open()) {

            closedAnswerCounters = con.createQuery(sql).executeAndFetch(ClosedAnswerCounter.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }

        return closedAnswerCounters.get(0);
    }

    @Override
    public ScoreTableAnswerCounter getCountOfScoreTableAnswers(int surveyId, int elementId) {
        List<ScoreTableAnswerCounter> scoreTableAnswerCounterList =  null;

        List<ScoreTableAnswerCounter> scoreTableAnswerCounters =  null;
        try (Connection con = sql2o.open()) {

        String sql = "SELECT " +
                "SUM(CASE WHEN execution_scoretable.answer1 = 1 THEN 1 ELSE 0 END) AS answer1c1, " +
                "SUM(CASE WHEN execution_scoretable.answer1 = 2 THEN 1 ELSE 0 END) AS answer1c2, " +
                "SUM(CASE WHEN execution_scoretable.answer1 = 3 THEN 1 ELSE 0 END) AS answer1c3, " +
                "SUM(CASE WHEN execution_scoretable.answer1 = 4 THEN 1 ELSE 0 END) AS answer1c4, " +
                "SUM(CASE WHEN execution_scoretable.answer1 = 5 THEN 1 ELSE 0 END) AS answer1c5, " +
                "SUM(CASE WHEN execution_scoretable.answer1 = 0 THEN 1 ELSE 0 END) AS answer1c0, " +

                "SUM(CASE WHEN execution_scoretable.answer2 = 1 THEN 1 ELSE 0 END) AS answer2c1, " +
                "SUM(CASE WHEN execution_scoretable.answer2 = 2 THEN 1 ELSE 0 END) AS answer2c2, " +
                "SUM(CASE WHEN execution_scoretable.answer2 = 3 THEN 1 ELSE 0 END) AS answer2c3, " +
                "SUM(CASE WHEN execution_scoretable.answer2 = 4 THEN 1 ELSE 0 END) AS answer2c4, " +
                "SUM(CASE WHEN execution_scoretable.answer2 = 5 THEN 1 ELSE 0 END) AS answer2c5, " +
                "SUM(CASE WHEN execution_scoretable.answer2 = 0 THEN 1 ELSE 0 END) AS answer2c0, " +

                "SUM(CASE WHEN execution_scoretable.answer3 = 1 THEN 1 ELSE 0 END) AS answer3c1, " +
                "SUM(CASE WHEN execution_scoretable.answer3 = 2 THEN 1 ELSE 0 END) AS answer3c2, " +
                "SUM(CASE WHEN execution_scoretable.answer3 = 3 THEN 1 ELSE 0 END) AS answer3c3, " +
                "SUM(CASE WHEN execution_scoretable.answer3 = 4 THEN 1 ELSE 0 END) AS answer3c4, " +
                "SUM(CASE WHEN execution_scoretable.answer3 = 5 THEN 1 ELSE 0 END) AS answer3c5, " +
                "SUM(CASE WHEN execution_scoretable.answer3 = 0 THEN 1 ELSE 0 END) AS answer3c0, " +

                "SUM(CASE WHEN execution_scoretable.answer4 = 1 THEN 1 ELSE 0 END) AS answer4c1, " +
                "SUM(CASE WHEN execution_scoretable.answer4 = 2 THEN 1 ELSE 0 END) AS answer4c2, " +
                "SUM(CASE WHEN execution_scoretable.answer4 = 3 THEN 1 ELSE 0 END) AS answer4c3, " +
                "SUM(CASE WHEN execution_scoretable.answer4 = 4 THEN 1 ELSE 0 END) AS answer4c4, " +
                "SUM(CASE WHEN execution_scoretable.answer4 = 5 THEN 1 ELSE 0 END) AS answer4c5, " +
                "SUM(CASE WHEN execution_scoretable.answer4 = 0 THEN 1 ELSE 0 END) AS answer4c0, " +

                "SUM(CASE WHEN execution_scoretable.answer5 = 1 THEN 1 ELSE 0 END) AS answer5c1, " +
                "SUM(CASE WHEN execution_scoretable.answer5 = 2 THEN 1 ELSE 0 END) AS answer5c2, " +
                "SUM(CASE WHEN execution_scoretable.answer5 = 3 THEN 1 ELSE 0 END) AS answer5c3, " +
                "SUM(CASE WHEN execution_scoretable.answer5 = 4 THEN 1 ELSE 0 END) AS answer5c4, " +
                "SUM(CASE WHEN execution_scoretable.answer5 = 5 THEN 1 ELSE 0 END) AS answer5c5, " +
                "SUM(CASE WHEN execution_scoretable.answer5 = 0 THEN 1 ELSE 0 END) AS answer5c0, " +

                "SUM(CASE WHEN execution_scoretable.answer6 = 1 THEN 1 ELSE 0 END) AS answer6c1, " +
                "SUM(CASE WHEN execution_scoretable.answer6 = 2 THEN 1 ELSE 0 END) AS answer6c2, " +
                "SUM(CASE WHEN execution_scoretable.answer6 = 3 THEN 1 ELSE 0 END) AS answer6c3, " +
                "SUM(CASE WHEN execution_scoretable.answer6 = 4 THEN 1 ELSE 0 END) AS answer6c4, " +
                "SUM(CASE WHEN execution_scoretable.answer6 = 5 THEN 1 ELSE 0 END) AS answer6c5, " +
                "SUM(CASE WHEN execution_scoretable.answer6 = 0 THEN 1 ELSE 0 END) AS answer6c0, " +

                "scoretable.criterion1, " +
                "scoretable.criterion2, " +
                "scoretable.criterion3, " +
                "scoretable.criterion4, " +
                "scoretable.criterion5, " +
                "scoretable.criterion6 " +
                "FROM execution_scoretable LEFT JOIN scoretable ON execution_scoretable.elementId = scoretable.elementId " +
                "WHERE execution_scoretable.surveyId = " + surveyId + " AND execution_scoretable.elementId= " + elementId +" ;";


            scoreTableAnswerCounters = con.createQuery(sql).executeAndFetch(ScoreTableAnswerCounter.class);

            scoreTableAnswerCounterList.add(scoreTableAnswerCounters.get(0));

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return scoreTableAnswerCounters.get(0);
    }


    @Override
    public List<String> getOptionalTextfieldAnswersClosedQuestion(int surveyId, int questionId) {
        List<String> optionalTestfieldList=  null;

        String sql = "SELECT optionalTextfield FROM `execution_closedquestion` WHERE surveyId= " + surveyId + " AND elementID = " + questionId +
                " AND optionalTextfield is not null;";

        try (Connection con = sql2o.open()) {

            optionalTestfieldList = con.createQuery(sql).executeScalarList(String.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }

        return optionalTestfieldList;
    }

    @Override
    public List<LocationCount> getCountOfDifferentLocations(int surveyId, int elementId) {

        List<LocationCount> countOfDifferentLocations=  null;

        String sql = "SELECT location, COUNT(*) AS `count` FROM execution_personaldata WHERE surveyId = " + surveyId + " AND elementId = " + elementId + " GROUP BY location ;";

        try (Connection con = sql2o.open()) {

            countOfDifferentLocations = con.createQuery(sql).executeAndFetch(LocationCount.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return countOfDifferentLocations;

    }

    @Override
    public List<Integer> getAllAges(int surveyId, int elementId) {


        List<Integer> allAges=  null;

        String sql = "SELECT age FROM execution_personaldata WHERE surveyId = " + surveyId + " AND elementId = " + elementId + "  AND age is not null  ORDER BY age ASC;";

        try (Connection con = sql2o.open()) {

            allAges = con.createQuery(sql).executeScalarList(Integer.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return allAges;

    }

    @Override
    public Integer getMaleCount(int surveyId, int elementId) {
        Integer maleCount = 0;

        String sql = "SELECT " +
                "SUM(CASE WHEN execution_personaldata.gender = 1 THEN 1 ELSE 0 END) AS male " +
                "FROM execution_personaldata WHERE surveyId = " + surveyId + " AND elementId = " + elementId + " AND execution_personaldata.gender is not null;";

        System.out.println(sql);
        try (Connection con = sql2o.open()) {

            maleCount = con.createQuery(sql).executeScalar(Integer.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return maleCount;
    }

    @Override
    public Integer getFemaleCount(int surveyId, int elementId) {
        Integer femaleCount = 0;

        String sql = "SELECT " +
                "SUM(CASE WHEN execution_personaldata.gender = 2 THEN 1 ELSE 0 END) AS female " +
                "FROM execution_personaldata WHERE surveyId = " + surveyId + " AND elementId = " + elementId + " AND execution_personaldata.gender is not null;";

        System.out.println(sql);
        try (Connection con = sql2o.open()) {

            femaleCount = con.createQuery(sql).executeScalar(Integer.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return femaleCount;
    }

    @Override
    public List<String> getOpenQuestionEvaluation(int surveyId, int elementId) {
        List<String> openQuestionEvaluations = null;

        String sql = "SELECT text FROM execution_openquestion WHERE surveyId = " + surveyId + " AND elementId = " + elementId + " ;";

        try (Connection con = sql2o.open()) {

            openQuestionEvaluations = con.createQuery(sql).executeScalarList(String.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return openQuestionEvaluations;
    }

}
