package app.survey;

import app.survey.elements.*;
import app.survey.executionElements.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.Date;
import java.util.Calendar;
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
    public boolean updateTextElement(Text text) {

        try (Connection con = sql2o.open()) {

            con.setRollbackOnException(false);

            System.out.println(text.toString());

            if (text.getPicture() == null || text.getPicture().length() < 1){

                String sqlText ="UPDATE text SET text = :text " +
                                "WHERE elementId = " + text.getElementId()+ " ;";

                con.createQuery(sqlText).addParameter("text", text.getText()).executeUpdate().getResult();
            }else{

                String sqlText =
                        "UPDATE text SET text = :text, picture = :picture " +
                                "WHERE elementId = " + text.getElementId()+ " ;";
                con.createQuery(sqlText)
                        .addParameter("text", text.getText())
                        .addParameter("picture", text.getPicture())
                        .executeUpdate();
            }

            String sqlSurveyElementTitle =
                    "UPDATE surveyelementtitle SET elementTitle = :elementTitle " +
                            "WHERE elementId = " +text.getElementId() + " ;";
            con.createQuery(sqlSurveyElementTitle)
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
    public boolean updatePersonalDataElement(PersonalData personalData) {
        try (Connection con = sql2o.open()) {

            con.setRollbackOnException(false);

            System.out.println(personalData.toString());

            String sqlText =
                    "UPDATE personaldata SET isFirstname = :isFirstname, isLastname = :isLastname, isAge = :isAge, " +
                            "isGender = :isGender, isLocation = :isLocation " +
                            "WHERE elementId = " + personalData.getElementId()+ " ;";
            con.createQuery(sqlText)
                    .addParameter("isFirstname", personalData.isFirstname())
                    .addParameter("isLastname", personalData.isLastname())
                    .addParameter("isAge", personalData.isAge())
                    .addParameter("isGender", personalData.isGender())
                    .addParameter("isLocation", personalData.isLocation())
                    .executeUpdate();


            String sqlSurveyElementTitle =
                    "UPDATE surveyelementtitle SET elementTitle = :elementTitle " +
                            "WHERE elementId = " +personalData.getElementId() + " ;";
            con.createQuery(sqlSurveyElementTitle)
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
    public boolean updateClosedQuestion(ClosedQuestion closedQuestion) {
        try (Connection con = sql2o.open()) {

            con.setRollbackOnException(false);

            if (closedQuestion.getPicture() == null || closedQuestion.getPicture().length() < 1){
                String sql =
                        "UPDATE closedquestion SET situation = :situation, questiontext = :questiontext, answer1 = :answer1, answer2 = :answer2, " +
                                "answer3 = :answer3, answer4 = :answer4, answer5 = :answer5, answer6 = :answer6, optionalTextfield = :optionalTextfield, " +
                                "multipleSelection = :multipleSelection  " +
                                "WHERE elementId = " + closedQuestion.getElementId()+ " ;";
                con.setRollbackOnException(false);
                con.createQuery(sql)
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
                        .executeUpdate();
            }else{
                String sql =
                        "UPDATE closedquestion SET situation = :situation, questiontext = :questiontext, answer1 = :answer1, answer2 = :answer2, " +
                                "answer3 = :answer3, answer4 = :answer4, answer5 = :answer5, answer6 = :answer6, optionalTextfield = :optionalTextfield, " +
                                "multipleSelection = :multipleSelection, picture = :picture " +
                                "WHERE elementId = " + closedQuestion.getElementId()+ " ;";
                con.setRollbackOnException(false);
                con.createQuery(sql)
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

            }

            String sqlSurveyElementTitle =
                    "UPDATE surveyelementtitle SET elementTitle = :elementTitle " +
                            "WHERE elementId = " +closedQuestion.getElementId() + " ;";
            con.createQuery(sqlSurveyElementTitle)
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
    public boolean updateOpenQuestion(OpenQuestion openQuestion) {

        try (Connection con = sql2o.open()) {

            con.setRollbackOnException(false);

            if (openQuestion.getPicture() == null || openQuestion.getPicture().length() < 1){

                String sql =
                        "UPDATE openquestion SET situation = :situation, questiontext = :questiontext " +
                                "WHERE elementId = " +openQuestion.getElementId() + " ;";

                con.createQuery(sql)
                        .addParameter("situation", openQuestion.getSituation())
                        .addParameter("questiontext", openQuestion.getQuestiontext())
                        .executeUpdate();

            }else{

                String sql =
                        "UPDATE openquestion SET situation = :situation, questiontext = :questiontext, picture = :picture " +
                                "WHERE elementId = " +openQuestion.getElementId() + " ;";

                con.createQuery(sql)
                        .addParameter("situation", openQuestion.getSituation())
                        .addParameter("questiontext", openQuestion.getQuestiontext())
                        .addParameter("picture", openQuestion.getPicture())
                        .executeUpdate();
            }


            String sqlSurveyElementTitle =
                    "UPDATE surveyelementtitle SET elementTitle = :elementTitle " +
                            "WHERE elementId = " +openQuestion.getElementId() + " ;";
            con.createQuery(sqlSurveyElementTitle)
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
    public boolean updateScoreTableQuestion(ScoreTable scoreTable) {


        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);

            if (scoreTable.getPicture() == null || scoreTable.getPicture().length() < 1){
                String sql =
                        "UPDATE scoretable SET situation = :situation, questiontext = :questiontext, criterion1 = :criterion1, criterion2 = :criterion2, criterion3 = :criterion3, " +
                                "criterion4 = :criterion4, criterion5 = :criterion5, criterion6 = :criterion6 " +
                                "WHERE elementId = " + scoreTable.getElementId() + " ;";
                con.createQuery(sql)
                        .addParameter("situation", scoreTable.getSituation())
                        .addParameter("questiontext", scoreTable.getQuestiontext())
                        .addParameter("criterion1", scoreTable.getCriterion1())
                        .addParameter("criterion2", scoreTable.getCriterion2())
                        .addParameter("criterion3", scoreTable.getCriterion3())
                        .addParameter("criterion4", scoreTable.getCriterion4())
                        .addParameter("criterion5", scoreTable.getCriterion5())
                        .addParameter("criterion6", scoreTable.getCriterion6())
                        .executeUpdate();
            }else{
                String sql =
                        "UPDATE scoretable SET situation = :situation, questiontext = :questiontext, criterion1 = :criterion1, criterion2 = :criterion2, criterion3 = :criterion3, " +
                                "criterion4 = :criterion4, criterion5 = :criterion5, criterion6 = :criterion6, picture = :picture " +
                                "WHERE elementId = " + scoreTable.getElementId() + " ;";
                con.createQuery(sql)
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
            }





            String sqlSurveyElementTitle =
                    "UPDATE surveyelementtitle SET elementTitle = :elementTitle " +
                            "WHERE elementId = " +scoreTable.getElementId() + " ;";
            con.createQuery(sqlSurveyElementTitle)
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

    @Override
    public int getLastAnsweredQuestionId(boolean isSessionId, String session, boolean isIpAddress, String ipAddress, int surveyId, int executionId) {
        int lastAnsweredQuestionId;

        String sql;

        System.out.println("getLastAnsweredQuestionId---");
        if (isSessionId || isIpAddress) {

            if (isIpAddress){
                sql = "SELECT questionId FROM execution_survey " +
                        " WHERE surveyId = " + surveyId + " AND " +
                        " ipAddress ='" + ipAddress+ "' " +
                        " ORDER BY questionId DESC LIMIT 1 ;";
                System.out.println("isIpAddress:..   " + sql);
            }else{
                sql = "SELECT questionId FROM execution_survey  " +
                        " WHERE sessionId ='" + session + "' AND " +
                        " surveyId = " + surveyId + " " +
                        " ORDER BY questionId DESC LIMIT 1 ;";
                System.out.println("isIpAddress ELSE::: " + sql);
            }

        }else {
            if(executionId == 0){
              return 0;
            }else{
                sql = "SELECT questionId FROM execution_survey " +
                        " WHERE sessionId ='" + session + "' AND " +
                        " surveyId = " + surveyId + " AND " +
                        " surveyCounterId = " + executionId + " " +
                        " ORDER BY questionId DESC LIMIT 1 ;";
                System.out.println("else:  " + sql);
            }

        }

        try (Connection con = sql2o.open()) {

            lastAnsweredQuestionId = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            return 0;
        }

        return lastAnsweredQuestionId;
    }

    @Override
    public int getLatestExecutionId(String session, int surveyId, String ipAddress) {

        int executionId;

        System.out.println("getLatestExecutionId------");
        /*
        String sql = "SELECT surveyCounterId FROM execution_counter " +
                " surveyId = " + surveyId + " AND " +
                " end = " + 0 + " AND " +
                " ORDER BY surveyCounterId DESC LIMIT 1 ;";
*/


        String sql = "SELECT" +
                "  execution_counter.surveyCounterId" +
                " FROM" +
                "  execution_counter" +
                " LEFT JOIN execution_survey " +
                " ON execution_survey.surveyCounterId = execution_counter.surveyCounterId" +
                " WHERE execution_counter.surveyId = "+ surveyId +
                " AND execution_counter.end = 0" +
                " AND execution_survey.ipAddress = '" + ipAddress + "'" +
                " AND execution_survey.sessionId = '" + session + "'" +
                " ORDER BY " +
                "  execution_counter.surveyCounterId DESC LIMIT 1;";

        System.out.println("getLatestExecutionId - sql: " +sql);

        try (Connection con = sql2o.open()) {

            executionId = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            System.out.println(e.toString());
            executionId = 0;
        }
        return executionId;
    }

    @Override
    public int insertExecutionEnd(String session, int surveyId, String ipAddress) {
        String sql =
                "INSERT INTO execution_counter(surveyId, end) " +
                        "VALUES (:surveyId, :end)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("surveyId", surveyId)
                    .addParameter("end", 0)
                    .executeUpdate();

            String sqlSelect =
                    "Select surveyCounterId FROM execution_counter ORDER BY surveyCounterId DESC;";
            int surveyCounterId = Integer.parseInt(con.createQuery(sqlSelect).executeScalar().toString());
            return surveyCounterId;
        }catch (Exception e){
            System.out.println(e.toString());
            return 0;
        }

    }

    @Override
    public boolean updateExecutionEnd(int executionId) {
        String updateSql = "UPDATE execution_counter SET " +
                "end = :end " +
                "WHERE surveyCounterId = " + executionId +" ;";

        try (Connection con = sql2o.open()) {
            con.createQuery(updateSql)
                    .addParameter("end", 1)
                    .executeUpdate();

            return true;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean saveExecutionText(TextExecution textExecution) {

        System.out.println("Texti speicher,,,,,,,,,,,,,,,,,,");
        String sql =
                "INSERT INTO execution_survey(surveyId, elementId, elementType, sessionId, ipAddress, questionId, surveyCounterId, date) " +
                        "VALUES (:surveyId, :elementId, :elementType, :sessionId, :ipAddress, :questionId, :surveyCounterId, :date)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("surveyId", textExecution.getSurveyId())
                    .addParameter("elementId", textExecution.getElementId())
                    .addParameter("elementType", textExecution.getElementType())
                    .addParameter("sessionId", textExecution.getSessionId())
                    .addParameter("ipAddress", textExecution.getIpAddres())
                    .addParameter("questionId", textExecution.getQuestionId())
                    .addParameter("surveyCounterId", textExecution.getSurveyCounterId())
                    .addParameter("date", getCurrentDate())
                    .executeUpdate();
            return true;
        }
        catch (Exception e){
            System.out.println("texti wrongiiiiiiiiiii");
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean saveExecutionPersonalData(PersonalDataExecution personalDataExecution) {

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);

            String sql =
                "INSERT INTO execution_survey(surveyId, elementId, elementType, sessionId, ipAddress, questionId, surveyCounterId, date) " +
                        "VALUES (:surveyId, :elementId, :elementType, :sessionId, :ipAddress, :questionId, :surveyCounterId, :date)";

            con.createQuery(sql)
                    .addParameter("surveyId", personalDataExecution.getSurveyId())
                    .addParameter("elementId", personalDataExecution.getElementId())
                    .addParameter("elementType", personalDataExecution.getElementType())
                    .addParameter("sessionId", personalDataExecution.getSessionId())
                    .addParameter("ipAddress", personalDataExecution.getIpAddres())
                    .addParameter("questionId", personalDataExecution.getQuestionId())
                    .addParameter("surveyCounterId", personalDataExecution.getSurveyCounterId())
                    .addParameter("date", getCurrentDate())

                    .executeUpdate();

            String sqlPersonalData =
                    "INSERT INTO execution_personaldata(surveyId, elementId, firstname, lastname, age, gender, location) " +
                            "VALUES (:surveyId, :elementId, :firstname, :lastname, :age, :gender, :location)";
            con.createQuery(sqlPersonalData)
                    .addParameter("surveyId", personalDataExecution.getSurveyId())
                    .addParameter("elementId", personalDataExecution.getElementId())
                    .addParameter("firstname", personalDataExecution.getFirstname())
                    .addParameter("lastname", personalDataExecution.getLastname())
                    .addParameter("age", personalDataExecution.getAge())
                    .addParameter("gender", personalDataExecution.getGender())
                    .addParameter("location", personalDataExecution.getLocation())

                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean saveExecutionClosedQuestion(ClosedQuestionExecution closedQuestionExecution) {

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);

            String sql =
                    "INSERT INTO execution_survey(surveyId, elementId, elementType, sessionId, ipAddress, questionId, surveyCounterId, date) " +
                            "VALUES (:surveyId, :elementId, :elementType, :sessionId, :ipAddress, :questionId, :surveyCounterId, :date)";
            con.createQuery(sql)
                    .addParameter("surveyId", closedQuestionExecution.getSurveyId())
                    .addParameter("elementId", closedQuestionExecution.getElementId())
                    .addParameter("elementType", closedQuestionExecution.getElementType())
                    .addParameter("sessionId", closedQuestionExecution.getSessionId())
                    .addParameter("ipAddress", closedQuestionExecution.getIpAddres())
                    .addParameter("questionId", closedQuestionExecution.getQuestionId())
                    .addParameter("surveyCounterId", closedQuestionExecution.getSurveyCounterId())
                    .addParameter("date", getCurrentDate())

                    .executeUpdate();


            String sqlClosedQuestion =
                    "INSERT INTO execution_closedquestion(surveyId, elementId, answer1, answer2, answer3, answer4, answer5, answer6, optionalTextfield) " +
                            "VALUES (:surveyId, :elementId, :answer1, :answer2, :answer3, :answer4, :answer5, :answer6, :optionalTextfield)";
            con.createQuery(sqlClosedQuestion)
                    .addParameter("surveyId", closedQuestionExecution.getSurveyId())
                    .addParameter("elementId", closedQuestionExecution.getElementId())
                    .addParameter("answer1", closedQuestionExecution.isAnswer1())
                    .addParameter("answer2", closedQuestionExecution.isAnswer2())
                    .addParameter("answer3", closedQuestionExecution.isAnswer3())
                    .addParameter("answer4", closedQuestionExecution.isAnswer4())
                    .addParameter("answer5", closedQuestionExecution.isAnswer5())
                    .addParameter("answer6", closedQuestionExecution.isAnswer6())
                    .addParameter("optionalTextfield", closedQuestionExecution.getOptionalTextfield())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean saveExecutionOpenQuestion(OpenQuestionExecution openQuestionExecution) {

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            String sql =
                    "INSERT INTO execution_survey(surveyId, elementId, elementType, sessionId, ipAddress, questionId, surveyCounterId, date) " +
                            "VALUES (:surveyId, :elementId, :elementType, :sessionId, :ipAddress, :questionId, :surveyCounterId, :date)";

            con.createQuery(sql)
                    .addParameter("surveyId", openQuestionExecution.getSurveyId())
                    .addParameter("elementId", openQuestionExecution.getElementId())
                    .addParameter("elementType", openQuestionExecution.getElementType())
                    .addParameter("sessionId", openQuestionExecution.getSessionId())
                    .addParameter("ipAddress", openQuestionExecution.getIpAddres())
                    .addParameter("questionId", openQuestionExecution.getQuestionId())
                    .addParameter("surveyCounterId", openQuestionExecution.getSurveyCounterId())
                    .addParameter("date", getCurrentDate())

                    .executeUpdate();

            String sqlOpenQuestion =
                    "INSERT INTO execution_openquestion(surveyId, elementId, text) " +
                            "VALUES (:surveyId, :elementId, :text)";

            con.createQuery(sqlOpenQuestion)
                    .addParameter("surveyId", openQuestionExecution.getSurveyId())
                    .addParameter("elementId", openQuestionExecution.getElementId())
                    .addParameter("text", openQuestionExecution.getText())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean saveExecutionScoreTable(ScoreTableExecution scoreTableExecution) {

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);

            String sql =
                    "INSERT INTO execution_survey(surveyId, elementId, elementType, sessionId, ipAddress, questionId, surveyCounterId, date) " +
                            "VALUES (:surveyId, :elementId, :elementType, :sessionId, :ipAddress, :questionId, :surveyCounterId, :date)";
            con.createQuery(sql)
                    .addParameter("surveyId", scoreTableExecution.getSurveyId())
                    .addParameter("elementId", scoreTableExecution.getElementId())
                    .addParameter("elementType", scoreTableExecution.getElementType())
                    .addParameter("sessionId", scoreTableExecution.getSessionId())
                    .addParameter("ipAddress", scoreTableExecution.getIpAddres())
                    .addParameter("questionId", scoreTableExecution.getQuestionId())
                    .addParameter("surveyCounterId", scoreTableExecution.getSurveyCounterId())
                    .addParameter("date", getCurrentDate())
                    .executeUpdate();

            String sqlScoreTable =
                    "INSERT INTO execution_scoretable(surveyId, elementId, answer1, answer2, answer3, answer4, answer5, answer6) " +
                            "VALUES (:surveyId, :elementId, :answer1, :answer2, :answer3, :answer4, :answer5, :answer6)";

            con.createQuery(sqlScoreTable)
                    .addParameter("surveyId", scoreTableExecution.getSurveyId())
                    .addParameter("elementId", scoreTableExecution.getElementId())
                    .addParameter("answer1", scoreTableExecution.getAnswer1())
                    .addParameter("answer2", scoreTableExecution.getAnswer2())
                    .addParameter("answer3", scoreTableExecution.getAnswer3())
                    .addParameter("answer4", scoreTableExecution.getAnswer4())
                    .addParameter("answer5", scoreTableExecution.getAnswer5())
                    .addParameter("answer6", scoreTableExecution.getAnswer6())
                    .executeUpdate();

            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
    }


    public Date getCurrentDate(){
        Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        return date;
    }


}
