package app.survey;

import app.survey.elements.*;
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

    @Override
    public boolean createOpenQuestion(OpenQuestion openQuestion) {
        String sql =
                "INSERT INTO openquestion(oId, elementId, situation, questiontext, picture) " +
                        "VALUES (:oId, :elementId, :situation, :questiontext, :picture)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("oId", openQuestion.getOId())
                    .addParameter("elementId", openQuestion.getElementId())
                    .addParameter("situation", openQuestion.getSituation())
                    .addParameter("questiontext", openQuestion.getQuestiontext())
                    .addParameter("picture", openQuestion.getPicture())
                    .executeUpdate();


            String sqlSurveyElementTitle =
                    "INSERT INTO surveyelementtitle(surveyId, elementId, elementTitle) " +
                            "VALUES (:surveyId, :elementId, :elementTitle) ; ";
            con.createQuery(sqlSurveyElementTitle)
                    .addParameter("surveyId", openQuestion.getSurveyId())
                    .addParameter("elementId", openQuestion.getElementId())
                    .addParameter("elementTitle", openQuestion.getElementTitle())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean createScoreTableQuestion(ScoreTable scoreTable) {
        String sql =
                "INSERT INTO scoretable(sId, elementId, situation, questiontext, criterion1, criterion2, criterion3, criterion4, criterion5, criterion6, picture) " +
                        "VALUES (:sId, :elementId, :situation, :questiontext, :criterion1, :criterion2, :criterion3, :criterion4, :criterion5, :criterion6, :picture)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("sId", scoreTable.getSId())
                    .addParameter("elementId", scoreTable.getElementId())
                    .addParameter("situation", scoreTable.getSituation())
                    .addParameter("questiontext", scoreTable.getQuestiontext())
                    .addParameter("criterion1", scoreTable.getCriterion1())
                    .addParameter("criterion2", scoreTable.getCriterion2())
                    .addParameter("criterion3", scoreTable.getCriterion3())
                    .addParameter("criterion4", scoreTable.getCriterion4())
                    .addParameter("criterion5", scoreTable.getCriterion5())
                    .addParameter("criterion6", scoreTable.getCriterion6())
                    .addParameter("picture", scoreTable.getPicture())
                    .executeUpdate();


            String sqlSurveyElementTitle =
                    "INSERT INTO surveyelementtitle(surveyId, elementId, elementTitle) " +
                            "VALUES (:surveyId, :elementId, :elementTitle) ; ";
            con.createQuery(sqlSurveyElementTitle)
                    .addParameter("surveyId", scoreTable.getSurveyId())
                    .addParameter("elementId", scoreTable.getElementId())
                    .addParameter("elementTitle", scoreTable.getElementTitle())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public SurveyElement getSurveyElementById(int surveyId, int elementId) {

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
                " AND surveyelement.elementId = " + elementId +
                " ORDER BY elementId ASC ;";

        try (Connection conn = sql2o.open()) {
            surveyElements = conn.createQuery(sql)
                    .executeAndFetch(SurveyElement.class);
        }

        System.out.println("surveyElements.get(0):  " + surveyElements.get(0).toString());
        return surveyElements.get(0);
    }

    @Override
    public boolean deleteSurveyElementById(int surveyId, int elementId) {

        try (Connection con = sql2o.open()) {

            SurveyElement surveyElement = getSurveyElementById(surveyId, elementId);

            String sqlDeleteElementType = "";

            System.out.println("Which ElementType?  " + surveyElement.getElementType());
            switch (surveyElement.getElementType()){
                case 1:
                    sqlDeleteElementType = "DELETE FROM `text` WHERE elementId = " + elementId +" ;";
                    break;
                case 2:
                    System.out.println("surveyElement.getElementType()    " + surveyElement.getElementType());
                    sqlDeleteElementType = "DELETE FROM `personaldata` WHERE elementId = " + elementId +" ;";
                    break;
                case 3:
                    sqlDeleteElementType = "DELETE FROM `closedquestion` WHERE elementId = " + elementId +" ;";
                    break;
                case 4:
                    sqlDeleteElementType = "DELETE FROM `openquestion` WHERE elementId = " + elementId +" ;";
                    break;
                case 5:
                    sqlDeleteElementType = "DELETE FROM `scoretable` WHERE elementId = " + elementId +" ;";
                    break;
            }

            System.out.println("SQL-String ElementType" + sqlDeleteElementType);
            String sql = "DELETE FROM `surveyelement` WHERE`surveyId` = "+ surveyId +" AND elementId = " + elementId +" ;";
            String sqlDeleteSurveyElementTitle = "DELETE FROM `surveyelementtitle` WHERE`surveyId` = "+ surveyId +" AND elementId = " + elementId +" ;";

            con.createQuery(sql).executeUpdate();
            con.createQuery(sqlDeleteElementType).executeUpdate();
            con.createQuery(sqlDeleteSurveyElementTitle).executeUpdate();

            return true;
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Text getTextElement(int elementId) {

        List<Text> textList;

        String sql = "SELECT "+
        " text.tId,surveyelement.elementId, surveyelementtitle.elementTitle, text.text, text.picture, surveyelement.surveyId" +
        " FROM surveyelement" +
        " LEFT JOIN" +
        " surveyelementtitle ON surveyelement.elementId = surveyelementtitle.elementId" +
        " LEFT JOIN" +
        " TEXT ON surveyelement.elementId = TEXT.elementId WHERE surveyelement.elementId = " + elementId + ";";


        try (Connection conn = sql2o.open()) {
            textList = conn.createQuery(sql)
                    .executeAndFetch(Text.class);

            return textList.get(0);
        }

    }

    @Override
    public PersonalData getPersonalData(int elementId) {
        List<PersonalData> personalData;

        String sql = "SELECT personaldata.pId," +
                " surveyelement.elementId, " +
                "surveyelementtitle.elementTitle, " +
                "personaldata.isFirstname, " +
                "personaldata.isLastname, " +
                "personaldata.isAge, " +
                "personaldata.isGender, " +
                "personaldata.isLocation, " +
                "surveyelement.surveyId " +
                "FROM surveyelement " +
                "LEFT JOIN surveyelementtitle ON surveyelement.elementId = surveyelementtitle.elementId " +
                "LEFT JOIN personaldata ON surveyelement.elementId = personaldata.elementId " +
                "WHERE surveyelement.elementId = " + elementId +";";


        try (Connection conn = sql2o.open()) {
            personalData = conn.createQuery(sql)
                    .executeAndFetch(PersonalData.class);

            return personalData.get(0);
        }
    }

    @Override
    public ClosedQuestion getClosedQuestion(int elementId) {
        List<ClosedQuestion> closedQuestions;

        String sql = "SELECT closedquestion.cId, " +
                "surveyelement.elementId, " +
                "surveyelementtitle.elementTitle, " +
                "closedquestion.situation, " +
                "closedquestion.questiontext, " +
                "closedquestion.answer1, " +
                "closedquestion.answer2, " +
                "closedquestion.answer3, " +
                "closedquestion.answer4, " +
                "closedquestion.answer5, " +
                "closedquestion.answer6, " +
                "closedquestion.optionalTextfield, " +
                "closedquestion.multipleSelection, " +
                "closedquestion.picture, " +
                "surveyelement.surveyId FROM surveyelement " +
                "LEFT JOIN surveyelementtitle ON surveyelement.elementId = surveyelementtitle.elementId " +
                "LEFT JOIN closedquestion ON surveyelement.elementId = closedquestion.elementId " +
                "WHERE surveyelement.elementId = " + elementId +" ;";


        try (Connection conn = sql2o.open()) {
            closedQuestions = conn.createQuery(sql)
                    .executeAndFetch(ClosedQuestion.class);

            return closedQuestions.get(0);
        }
    }

    @Override
    public OpenQuestion getOpenedQuestion(int elementId) {
        List<OpenQuestion> openQuestions;

        String sql = "SELECT openquestion.oId, " +
                "surveyelement.elementId, " +
                "surveyelementtitle.elementTitle, " +
                "openquestion.situation, " +
                "openquestion.questiontext, " +
                "openquestion.picture, " +
                "surveyelement.surveyId " +
                "FROM surveyelement " +
                "LEFT JOIN surveyelementtitle ON surveyelement.elementId = surveyelementtitle.elementId " +
                "LEFT JOIN openquestion ON surveyelement.elementId = openquestion.elementId " +
                "WHERE surveyelement.elementId = " + elementId + " ;";

        try (Connection conn = sql2o.open()) {
            openQuestions = conn.createQuery(sql)
                    .executeAndFetch(OpenQuestion.class);

            return openQuestions.get(0);
        }
    }

    @Override
    public ScoreTable getScoreTable(int elementId) {
        List<ScoreTable> scoreTables;

        String sql = "SELECT scoretable.sId, " +
                "surveyelement.elementId, " +
                "surveyelementtitle.elementTitle, " +
                "scoretable.situation, " +
                "scoretable.questiontext, " +
                "scoretable.criterion1, " +
                "scoretable.criterion2, " +
                "scoretable.criterion3, " +
                "scoretable.criterion4, " +
                "scoretable.criterion5, " +
                "scoretable.criterion6, " +
                "scoretable.picture, " +
                "surveyelement.surveyId FROM surveyelement " +
                "LEFT JOIN surveyelementtitle ON surveyelement.elementId = surveyelementtitle.elementId " +
                "LEFT JOIN scoretable ON surveyelement.elementId = scoretable.elementId " +
                "WHERE surveyelement.elementId = " + elementId +" ;";

        try (Connection conn = sql2o.open()) {
            scoreTables = conn.createQuery(sql)
                    .executeAndFetch(ScoreTable.class);

            return scoreTables.get(0);
        }
    }
}
