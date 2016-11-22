package app.survey;

import app.survey.elements.OpenQuestion;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 22.11.2016.
 */
public class SurveyDaoImpl implements SurveyDao {

    private Sql2o sql2o;

    public SurveyDaoImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Survey> getAllSurveys(int userId) {
        List<Survey> surveyList;
        try (Connection conn = sql2o.open()) {
            surveyList= conn.createQuery("SELECT * FROM survey")
                    .executeAndFetch(Survey.class);
            return surveyList;
        }
    }

    @Override
    public List<SurveyElement> getAllSurveyElements(int surveyId) {

        List<SurveyElement> list = new ArrayList<>();

        list.add(new SurveyElement(1, 1, SurveyElementType.PERSONALDATA.getValue(), "Hello World"));

        return list;
    }

    @Override
    public Survey getSurveyById(int surveyId) {
        List<Survey> survey;
        try (Connection conn = sql2o.open()) {
            survey= conn.createQuery("SELECT * FROM survey WHERE surveyId= " + surveyId +";")
                    .executeAndFetch(Survey.class);
            return survey.get(0);
        }
    }

    @Override
    public boolean createNewSurvey(Survey survey) {

        String sql =
                "INSERT INTO survey(surveyTitle, userId, ipAddress, sessionId, isPublished) " +
                        "VALUES (:surveyTitle, :userId, :ipAddress, :sessionId, :isPublished)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("surveyTitle", survey.getSurveyTitle())
                    .addParameter("userId", survey.getUserId())
                    .addParameter("ipAddress", survey.isIpAddress())
                    .addParameter("sessionId", survey.isSessionId())
                    .addParameter("isPublished", survey.isPublished())
                    .executeUpdate();
            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean updateSurvey(Survey survey) {

        String updateSql = "UPDATE survey SET " +
                "surveyTitle = :surveyTitle, " +
                "ipAddress= :ipAddress, " +
                "sessionId = :sessionId, " +
                "isPublished = :isPublished " +
                "WHERE surveyId = :surveyId";

        try (Connection con = sql2o.open()) {
            con.createQuery(updateSql)
                    .addParameter("surveyId", survey.getSurveyId())
                    .addParameter("surveyTitle", survey.getSurveyTitle())
                    .addParameter("ipAddress", survey.isIpAddress())
                    .addParameter("sessionId", survey.isSessionId())
                    .addParameter("isPublished", survey.isPublished())
                    .executeUpdate();
            return true;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteSurvey(Survey survey) {

        String updateSql = "DELETE FROM `survey` WHERE`surveyId` = "+ survey.getSurveyId() +";";

        try (Connection con = sql2o.open()) {
            con.createQuery(updateSql).executeUpdate();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean createNewSurveyElement(SurveyElement surveyElement) {
        return false;
    }

    @Override
    public boolean createNewOpenQuestion(OpenQuestion openQuestion) {
        return false;
    }

    @Override
    public int getLatestSurveyId(int userId) {

        int latestSurveyId;

        String sql = "SELECT surveyId FROM survey " +
                " WHERE userId =" + userId +
                " ORDER BY surveyId DESC LIMIT 1;";

        try (Connection con = sql2o.open()) {

            latestSurveyId = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            return 0;
        }

        return latestSurveyId;
    }
}
