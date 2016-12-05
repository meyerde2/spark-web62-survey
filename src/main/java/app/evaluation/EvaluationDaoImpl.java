package app.evaluation;


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
    public Evaluation getSurveyEvaluationById(int surveyId) {
        return null;
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
    public int getCountAnswer1ClosedQuestion(int surveyId) {
        int count;

        String sql = "SELECT COUNT(answer1) FROM `execution_closedquestion` WHERE answer1 = 1 AND surveyId= " + surveyId + " ;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public int getCountAnswer2ClosedQuestion(int surveyId) {
        int count;

        String sql = "SELECT COUNT(answer2) FROM `execution_closedquestion` WHERE answer2 = 1 AND surveyId= " + surveyId + " ;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public int getCountAnswer3ClosedQuestion(int surveyId) {
        int count;

        String sql = "SELECT COUNT(answer3) FROM `execution_closedquestion` WHERE answer3 = 1 AND surveyId= " + surveyId + " ;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public int getCountAnswer4ClosedQuestion(int surveyId) {
        int count;

        String sql = "SELECT COUNT(answer4) FROM `execution_closedquestion` WHERE answer4 = 1 AND surveyId= " + surveyId + " ;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public int getCountAnswer5ClosedQuestion(int surveyId) {
        int count;

        String sql = "SELECT COUNT(answer5) FROM `execution_closedquestion` WHERE answer5 = 1 AND surveyId= " + surveyId + " ;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public int getCountAnswer6ClosedQuestion(int surveyId) {
        int count;

        String sql = "SELECT COUNT(answer6) FROM `execution_closedquestion` WHERE answer6 = 1 AND surveyId= " + surveyId + " ;";

        try (Connection con = sql2o.open()) {

            count = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            count = 0;
        }
        return count;
    }

    @Override
    public List<String> getOptionalTextfieldAnswersClosedQuestion(int surveyId) {
        List<String> optionalTestfieldList=  null;

        String sql = "SELECT optionalTextfield FROM `execution_closedquestion` WHERE surveyId= " + surveyId + " " +
                "AND optionalTextfield is not null;";

        try (Connection con = sql2o.open()) {

            optionalTestfieldList = con.createQuery(sql).executeScalarList(String.class);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return optionalTestfieldList;
    }

}
