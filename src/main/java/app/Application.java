package app;

import app.evaluation.EvaluationController;
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
import app.util.MessageBundle;
import app.util.Path;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.sql2o.Sql2o;
import spark.*;
import spark.debug.DebugScreen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.getSessionCurrentUser;
import static app.util.RequestUtil.getSessionLocale;
import static spark.Spark.*;


public class Application{


    public static UserDao userDao;
    public static SurveyDao surveyDao;

    public static String picturesDir;

    public static  FreeMarkerEngine freeMarkerEngine;

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
            get("/evaluation/", EvaluationController.serveEvaluationPage);

            //Survey
            get("/surveyoverview/", SurveyController.serveSurveyOverview);
            get("/surveycreation/", SurveyController.serveSurveyCreation);
            get("/surveycreation/:id/", SurveyController.serveSurveyCreation);
            get("/deletesurvey/:id/", SurveyController.deleteSurvey);

            post("/createNewSurvey/", SurveyController.createNewServery);
            post("/updateSurvey/:id/", SurveyController.updateSurvey);


            post("/textupload/", SurveyController.createTextElement);
            post("/personaldata/", SurveyController.createPersonalDataElement);
            post("/closedquestion/", SurveyController.createClosedQuestion);

        //get("/surveyTest/:id/", SurveyController.serveSurveyCreation);

            //User
            get("/usercontrol/", UserController.serveUsercontrolPage);

            get("/user/:username/", UserController.serveOneUser);

            post("/createNewUser/", UserController.serveNewUser);
           // post("/createNewUser",                  (ICRoute) UserController.serveNewUser);
            post("/createNewUserLogin/", UserController.serveNewUserLogin);

            post("/updateUser/", UserController.updateUser);

            //Set up after-filters (called after each get/post)
            after("*", Filters.addGzipHeader);


            get("/hello", (request, response) -> {

                System.out.println("hello world......");

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("message", "Hello World, my Friend!");

                attributes.put("users", userDao.getAllUsers());

                // The login.ftl file is located in directory:
                // src/test/resources/spark/template/freemarker

                attributes.put("msg", new MessageBundle(getSessionLocale(request)));
                attributes.put("currentUser", getSessionCurrentUser(request));

                attributes.put("WebPath", Path.Web.class);

                return new ModelAndView(attributes, "/login.ftl");
            }, new FreeMarkerEngine());

        }

    @FunctionalInterface
    private interface ICRoute extends Route {
        default Object handle(Request request, Response response) throws Exception {
            handle(request);
            return "";
        }
        void handle(Request request) throws Exception;
    }


/*
    @Override
    public void init() {


           Spark.staticFileLocation("/public");
           Spark.externalStaticFileLocation("/webapps/pictures");


            picturesDir="webapps/pictures";
            //picturesDir=new File("").getAbsolutePath().toString() + "\\target\\classes\\public\\pictures";
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
            get("/evaluation/", EvaluationController.serveEvaluationPage);

            //Survey
            get("/surveyoverview/", SurveyController.serveSurveyOverview);
            get("/surveycreation/", SurveyController.serveSurveyCreation);
            get("/surveycreation/:id/", SurveyController.serveSurveyCreation);
            get("/deletesurvey/:id/", SurveyController.deleteSurvey);

            post("/createNewSurvey/", SurveyController.createNewServery);
            post("/updateSurvey/:id/", SurveyController.updateSurvey);


            post("/upload/", SurveyController.createTextElement);

            //User
            get("/usercontrol/", UserController.serveUsercontrolPage);

            get("/user/:username/", UserController.serveOneUser);

            post("/createNewUser/", UserController.serveNewUser);
           // post("/createNewUser",                  (ICRoute) UserController.serveNewUser);
            post("/createNewUserLogin/", UserController.serveNewUserLogin);

            post("/updateUser/", UserController.updateUser);

            //Set up after-filters (called after each get/post)
            after("*", Filters.addGzipHeader);


            get("/hello", (request, response) -> {

                System.out.println("hello world......");

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("message", "Hello World, my Friend!");

                attributes.put("users", userDao.getAllUsers());

                // The login.ftl file is located in directory:
                // src/test/resources/spark/template/freemarker

                attributes.put("msg", new MessageBundle(getSessionLocale(request)));
                attributes.put("currentUser", getSessionCurrentUser(request));

                attributes.put("WebPath", Path.Web.class);

                return new ModelAndView(attributes, "/login.ftl");
            }, new FreeMarkerEngine());

    }
*/
}
