package app;

import app.evaluation.EvaluationController;
import app.evaluation.EvaluationDao;
import app.evaluation.EvaluationDaoImpl;
import app.index.IndexController;
import app.login.LoginController;
import app.survey.SurveyController;
import app.survey.SurveyDao;
import app.survey.SurveyDaoImpl;
import app.user.UserController;
import app.user.UserDao;
import app.user.UserDaoImpl;
import app.util.Filters;
import app.util.FreeMarkerEngine;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.sql2o.Sql2o;
import spark.Spark;
import spark.debug.DebugScreen;

import java.io.File;

import static spark.Spark.*;


public class Application{


    public static UserDao userDao;
    public static SurveyDao surveyDao;
    public static EvaluationDao evaluationDao;
    public static String picturesDir;

    public static FreeMarkerEngine freeMarkerEngine;

    public static void main(String args[]) {

            Spark.staticFileLocation("/public");
            Spark.externalStaticFileLocation("/pictures");

            picturesDir=new File("").getAbsolutePath().toString() + "\\target\\classes\\public\\pictures";
            //Test
            DebugScreen.enableDebugScreen();

            freeMarkerEngine = new FreeMarkerEngine();
            Configuration freeMarkerConfiguration = new Configuration();

            freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Application.class, "/public/templates/"));
            freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

            try {
                Class.forName("com.mysql.jdbc.Driver");
                String DB_URL = "jdbc:mysql://localhost:3306/survey";
                String USER = "root";
                String PASS = "";
                Sql2o sql2o = new Sql2o(DB_URL, USER, PASS);

                userDao = new UserDaoImpl(sql2o);
                surveyDao = new SurveyDaoImpl(sql2o);
                evaluationDao = new EvaluationDaoImpl(sql2o);

                System.out.println(userDao.getAllUsers());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            // Set up before-filters (called before each get/post)
            before("*", Filters.addTrailingSlashes);
            before("*", Filters.handleLocaleChange);

            //Login
            get("/login/", LoginController.serveLoginPage);
            post("/login/", LoginController.handleLoginPost);

            //Logout
            post("/logout/", LoginController.handleLogoutPost);

            //Index
            get("/index/", IndexController.serveIndexPage);

            //Evaluation
            get("/evaluation/:id/", EvaluationController.serveEvaluationPage);

            //Execute Survey
            get("/survey/execution/:id/", SurveyController.serveSurveyExecutionPage);

            post("/survey/executionQuestion/", SurveyController.saveSurveyExecutionQuestion);
            //Create Survey
            get("/surveyoverview/", SurveyController.serveSurveyOverview);
            get("/surveycreation/", SurveyController.serveSurveyCreation);
            get("/surveycreation/:id/", SurveyController.serveSurveyCreation);
            get("/deletesurvey/:id/", SurveyController.deleteSurvey);

            post("/createNewSurvey/", SurveyController.createNewServery);
            post("/updateSurvey/:id/", SurveyController.updateSurvey);


            post("/textupload/", SurveyController.createTextElement);
            post("/textuploadUpdate/", SurveyController.updateTextElement);

            post("/personaldata/", SurveyController.createPersonalDataElement);
            post("/personaldataUpdate/", SurveyController.updatePersonalDataElement);

            post("/closedquestion/", SurveyController.createClosedQuestion);
            post("/closedquestionUpdate/", SurveyController.updateClosedQuestion);

            post("/openquestion/", SurveyController.createOpenQuestion);
            post("/openquestionUpdate/", SurveyController.updateOpenQuestion);

            post("/scoretable/", SurveyController.createScoreTable);
            post("/scoretableUpdate/", SurveyController.updateScoreTable);

            get("/survey/:surveyId/element/:elementId/elementtype/:elementtype/", SurveyController.serveUpdateSurveyElement);
            get("/delete/survey/:surveyId/element/:elementId/", SurveyController.deleteSurveyElement);

            //get("/surveyTest/:id/", SurveyController.serveSurveyCreation);

            //User
            get("/usercontrol/", UserController.serveUsercontrolPage);

            get("/user/:username/", UserController.serveOneUser);

            post("/createNewUser/", UserController.serveNewUser);

            post("/createNewUserLogin/", UserController.serveNewUserLogin);

            post("/updateUser/", UserController.updateUser);

            //Set up after-filters (called after each get/post)
            after("*", Filters.addGzipHeader);

        }
    /*
    @Override
    public void init() {
            Spark.staticFileLocation("/public");
            Spark.externalStaticFileLocation("/webapps/pictures");

            picturesDir = new File("").getAbsolutePath().toString() + "\\webapps\\pictures";
            //Test
            //DebugScreen.enableDebugScreen();

            freeMarkerEngine = new FreeMarkerEngine();
            Configuration freeMarkerConfiguration = new Configuration();

            freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Application.class, "/public/templates/"));
            freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

            try {
                Class.forName("com.mysql.jdbc.Driver");
                String DB_URL = "jdbc:mysql://localhost:3306/survey";
                String USER = "root";
                String PASS = "";
                Sql2o sql2o = new Sql2o(DB_URL, USER, PASS);

                userDao = new UserDaoImpl(sql2o);
                surveyDao = new SurveyDaoImpl(sql2o);
                evaluationDao = new EvaluationDaoImpl(sql2o);

                System.out.println(userDao.getAllUsers());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            // Set up before-filters (called before each get/post)
            before("*", Filters.addTrailingSlashes);
            before("*", Filters.handleLocaleChange);

            //Login
            get("/login/", LoginController.serveLoginPage);
            post("/login/", LoginController.handleLoginPost);

            //Logout
            post("/logout/", LoginController.handleLogoutPost);

            //Index
            get("/index/", IndexController.serveIndexPage);

            //Evaluation
            get("/evaluation/:id/", EvaluationController.serveEvaluationPage);

            //Execute Survey
            get("/survey/execution/:id/", SurveyController.serveSurveyExecutionPage);

            post("/survey/executionQuestion/", SurveyController.saveSurveyExecutionQuestion);
            //Create Survey
            get("/surveyoverview/", SurveyController.serveSurveyOverview);
            get("/surveycreation/", SurveyController.serveSurveyCreation);
            get("/surveycreation/:id/", SurveyController.serveSurveyCreation);
            get("/deletesurvey/:id/", SurveyController.deleteSurvey);

            post("/createNewSurvey/", SurveyController.createNewServery);
            post("/updateSurvey/:id/", SurveyController.updateSurvey);


            post("/textupload/", SurveyController.createTextElement);
            post("/textuploadUpdate/", SurveyController.updateTextElement);

            post("/personaldata/", SurveyController.createPersonalDataElement);
            post("/personaldataUpdate/", SurveyController.updatePersonalDataElement);

            post("/closedquestion/", SurveyController.createClosedQuestion);
            post("/closedquestionUpdate/", SurveyController.updateClosedQuestion);

            post("/openquestion/", SurveyController.createOpenQuestion);
            post("/openquestionUpdate/", SurveyController.updateOpenQuestion);

            post("/scoretable/", SurveyController.createScoreTable);
            post("/scoretableUpdate/", SurveyController.updateScoreTable);

            get("/survey/:surveyId/element/:elementId/elementtype/:elementtype/", SurveyController.serveUpdateSurveyElement);
            get("/delete/survey/:surveyId/element/:elementId/", SurveyController.deleteSurveyElement);

            //get("/surveyTest/:id/", SurveyController.serveSurveyCreation);

            //User
            get("/usercontrol/", UserController.serveUsercontrolPage);

            get("/user/:username/", UserController.serveOneUser);

            post("/createNewUser/", UserController.serveNewUser);

            post("/createNewUserLogin/", UserController.serveNewUserLogin);

            post("/updateUser/", UserController.updateUser);

            //Set up after-filters (called after each get/post)
            after("*", Filters.addGzipHeader);


    }
*/

}
