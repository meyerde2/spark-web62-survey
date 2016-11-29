package app.survey;

import app.survey.elements.ClosedQuestion;
import app.survey.elements.OpenQuestion;
import app.survey.elements.PersonalData;
import app.survey.elements.Text;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

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

        List<SurveyElement> surveyElements;

        String sql = "SELECT " +
                "surveyelement.elementId, " +
                "surveyelement.surveyId, " +
                "surveyelement.elementType, " +
                "surveyelementtitle.elementTitle " +
                "FROM " +
                "survey.surveyelement " +
                "LEFT JOIN " +
                "surveyelementtitle ON surveyelement.elementId = surveyelementtitle.elementId " +
                "WHERE " +
                "surveyelement.surveyid = " + surveyId +
                " ORDER BY elementId ASC ;";
        try (Connection conn = sql2o.open()) {
            surveyElements = conn.createQuery(sql)
                    .executeAndFetch(SurveyElement.class);

            return surveyElements;
        }

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
    public int createNewSurveyElement(SurveyElement surveyElement) {
        String sql =
                "INSERT INTO surveyelement(surveyId, elementType) " +
                        "VALUES (:surveyId, :elementType)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("surveyId", surveyElement.getSurveyId())
                    .addParameter("elementType", surveyElement.getElementType())
                    .executeUpdate();

            String sqlGetElementId = "SELECT elementId FROM surveyelement " +
                    " WHERE surveyId =" + surveyElement.getSurveyId() +
                    " ORDER BY elementId DESC LIMIT 1;";

            int latestElementId = Integer.parseInt(con.createQuery(sqlGetElementId).executeScalar().toString());

            return latestElementId;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return 0;
        }
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

    @Override
    public boolean createTextElement(Text text) {

        String sql =
                "INSERT INTO text(tId, elementId, text, picture) " +
                        "VALUES (:tId, :elementId, :text, :picture)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("tId", text.getTId())
                    .addParameter("elementId", text.getElementId())
                    .addParameter("text", text.getText())
                    .addParameter("picture", text.getPicture())
                    .executeUpdate();


            String sqlSurveyElementTitle =
                    "INSERT INTO surveyelementtitle(surveyId, elementId, elementTitle) " +
                            "VALUES (:surveyId, :elementId, :elementTitle)";
            con.createQuery(sqlSurveyElementTitle)
                    .addParameter("surveyId", text.getSurveyId())
                    .addParameter("elementId", text.getElementId())
                    .addParameter("elementTitle", text.getElementTitle())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean createPersonalDataElement(PersonalData personalData) {
        String sql =
                "INSERT INTO personaldata(pId, elementId, isFirstname, isLastname, isAge, isGender, isLocation) " +
                        "VALUES (:pId, :elementId, :isFirstname, :isLastname, :isAge, :isGender, :isLocation)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("pId", personalData.getPId())
                    .addParameter("elementId", personalData.getElementId())
                    .addParameter("isFirstname", personalData.isFirstname())
                    .addParameter("isLastname", personalData.isLastname())
                    .addParameter("isAge", personalData.isAge())
                    .addParameter("isGender", personalData.isGender())
                    .addParameter("isLocation", personalData.isLocation())
                    .executeUpdate();

            String sqlSurveyElementTitle =
                    "INSERT INTO surveyelementtitle(surveyId, elementId, elementTitle) " +
                            "VALUES (:surveyId, :elementId, :elementTitle)";
            con.createQuery(sqlSurveyElementTitle)
                    .addParameter("surveyId", personalData.getSurveyId())
                    .addParameter("elementId", personalData.getElementId())
                    .addParameter("elementTitle", personalData.getElementTitle())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean createClosedQuestion(ClosedQuestion closedQuestion) {
        System.out.println("closedQuestion in DaoImpl:  " +  closedQuestion);
        String sql =
                "INSERT INTO closedquestion(cId, elementId, situation, questiontext, answer1, answer2, answer3, answer4, answer5, answer6, optionalTextfield, multipleSelection, picture) " +
                        "VALUES (:cId, :elementId, :situation, :questiontext, :answer1, :answer2, :answer3, :answer4, :answer5, :answer6, :optionalTextfield, :multipleSelection, :picture)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("cId", closedQuestion.getCId())
                    .addParameter("elementId", closedQuestion.getElementId())
                    .addParameter("situation", closedQuestion.getSituation())
                    .addParameter("questiontext", closedQuestion.getQuestiontext())
                    .addParameter("answer1", closedQuestion.getAnswer1())
                    .addParameter("answer2", closedQuestion.getAnswer2())
                    .addParameter("answer3", closedQuestion.getAnswer3())
                    .addParameter("answer4", closedQuestion.getAnswer4())
                    .addParameter("answer5", closedQuestion.getAnswer5())
                    .addParameter("answer6", closedQuestion.getAnswer6())
                    .addParameter("optionalTextfield", closedQuestion.isOptionalTextfield())
                    .addParameter("multipleSelection", closedQuestion.isMultipleSelection())
                    .addParameter("picture", closedQuestion.getPicture())
                    .executeUpdate();


            System.out.println("closedQuestion.getElementTitle():  " + closedQuestion.getElementTitle());
            String sqlSurveyElementTitle =
                    "INSERT INTO surveyelementtitle(surveyId, elementId, elementTitle) " +
                            "VALUES (:surveyId, :elementId, :elementTitle) ; ";
            con.createQuery(sqlSurveyElementTitle)
                    .addParameter("surveyId", closedQuestion.getSurveyId())
                    .addParameter("elementId", closedQuestion.getElementId())
                    .addParameter("elementTitle", closedQuestion.getElementTitle())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }
}
