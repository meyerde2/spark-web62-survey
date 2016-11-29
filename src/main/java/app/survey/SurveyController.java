package app.survey;


import app.Application;
import app.login.LoginController;
import app.survey.elements.ClosedQuestion;
import app.survey.elements.PersonalData;
import app.survey.elements.Text;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static app.Application.surveyDao;
import static app.Application.userDao;
import static app.util.JsonUtil.dataToJson;
import static app.util.RequestUtil.*;

public class SurveyController {

    public static Route serveSurveyOverview = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveCreateSurvey");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            attributes.put("surveyList", surveyDao.getAllSurveys(userDao.getUserByUsername(request.session().attribute("currentUser")).getId()));

            attributes.put("currentPage", "survey");
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYOVERVIEW));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route createNewServery = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController createNewServery");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "survey");


            User user = userDao.getUserByUsername(request.session().attribute("currentUser"));
            System.out.println("username:::  " + request.session().attribute("currentUser"));

            System.out.println("user:::  " + user);

            surveyDao.createNewSurvey(new Survey(0, request.queryParams("surveyTitle"),
                    user.getId(),
                    Boolean.parseBoolean(request.queryParams("ipAddress")),
                    Boolean.parseBoolean(request.queryParams("sessionId")),
                    false));

            //get new survey id by userid

            int latestSurveyId = surveyDao.getLatestSurveyId(user.getId());

            System.out.println("latestSurveyId: " + latestSurveyId);
            if (latestSurveyId == 0){
                response.redirect(Path.Web.SURVEY);
                return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYOVERVIEW));
            }else{
                response.redirect("/surveycreation/"+ latestSurveyId + "/");
                return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYCREATION));
            }

        }
        if (clientAcceptsJson(request)) {
            return false;
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route serveSurveyCreation = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            int surveyId = Integer.parseInt(getParamId(request).toString());

            System.out.println("getParamId:  " + surveyId);

            attributes.put("currentSurvey", surveyDao.getSurveyById(surveyId));

            attributes.put("surveyElements", surveyDao.getAllSurveyElements(surveyId));

            attributes.put("currentPage", "SURVEYCREATION");
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYCREATION));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route updateSurvey = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl UpdateSurvey");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            int surveyId = Integer.parseInt(getParamId(request));

            System.out.println("getParamId:  " + surveyId);

            System.out.println("ipAddress:  " + request.queryParams("ipAddress"));
            surveyDao.updateSurvey(new Survey(surveyId, request.queryParams("surveyTitle"),
                    0,
                    Boolean.parseBoolean(request.queryParams("ipAddress")),
                    Boolean.parseBoolean(request.queryParams("sessionId")),
                    Boolean.parseBoolean(request.queryParams("isPublished"))));

            attributes.put("currentSurvey", surveyDao.getSurveyById(surveyId));

            attributes.put("surveyElements", surveyDao.getAllSurveyElements(0));

            attributes.put("currentPage", "survey");
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYCREATION));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route deleteSurvey = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl UpdateSurvey");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            int surveyId = Integer.parseInt(getParamId(request));

            System.out.println("getParamId:  " + surveyId);

            System.out.println("ipAddress:  " + request.queryParams("ipAddress"));
            surveyDao.deleteSurvey(surveyDao.getSurveyById(surveyId));

            attributes.put("currentPage", "survey");
            response.redirect(Path.Web.SURVEY);
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYOVERVIEW));

        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route createTextElement = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl createTextElement----------------");


            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            }

            Part file = request.raw().getPart("picture");
            String pictureFilename = savePictureFile(file);
            int surveyId = Integer.parseInt(request.queryParams("surveyId"));

            if ((request.queryParams("textElement").toString().length() < 1) || (request.queryParams("elementTitle").toString().length() < 1)){
                return false;
            }

            if("wrongFormat".equals(pictureFilename)){
                attributes.put("pictureUpload", "wrongFormat");
                return false;
            }else{
                System.out.println("right Format");

                SurveyElement surveyElement = new SurveyElement(0, surveyId, 1, request.queryParams("elementTitle").toString());
                int elementId = surveyDao.createNewSurveyElement(surveyElement);

                Text text = new Text(0, elementId, surveyElement.elementTitle, request.queryParams("textElement").toString(), pictureFilename, surveyElement.getSurveyId());
                surveyDao.createTextElement(text);
            }


            attributes.put("currentSurvey", surveyDao.getSurveyById(surveyId));
            attributes.put("surveyElements", surveyDao.getAllSurveyElements(0));
            attributes.put("currentPage", "survey");

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route createPersonalDataElement = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl createPersonalDataElement----------------");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            int surveyId = Integer.parseInt(request.queryParams("surveyId"));

            System.out.println(Boolean.parseBoolean(request.queryParams("firstname").toString()));
            System.out.println(Boolean.parseBoolean(request.queryParams("lastname").toString()));
            System.out.println(Boolean.parseBoolean(request.queryParams("age").toString()));
            System.out.println(Boolean.parseBoolean(request.queryParams("gender").toString()));
            System.out.println(Boolean.parseBoolean(request.queryParams("locationP").toString()));


            if ((request.queryParams("elementTitle").toString().length() < 1) ||
                    (!Boolean.parseBoolean(request.queryParams("firstname").toString()) &&
                            !Boolean.parseBoolean(request.queryParams("lastname").toString()) &&
                            !Boolean.parseBoolean(request.queryParams("age").toString()) &&
                            !Boolean.parseBoolean(request.queryParams("gender").toString()) &&
                            !Boolean.parseBoolean(request.queryParams("locationP").toString()))) {
                System.out.println("false - - - - ");
                return false;
            }

            SurveyElement surveyElement = new SurveyElement(0, surveyId, 2, request.queryParams("elementTitle"));
            int elementId = surveyDao.createNewSurveyElement(surveyElement);

            PersonalData personalData = new PersonalData(0, elementId,
                    request.queryParams("elementTitle"),
                    Boolean.parseBoolean(request.queryParams("firstname")),
                    Boolean.parseBoolean(request.queryParams("lastname")),
                    Boolean.parseBoolean(request.queryParams("age")),
                    Boolean.parseBoolean(request.queryParams("gender")),
                    Boolean.parseBoolean(request.queryParams("locationP")),
                    surveyElement.getSurveyId());

            surveyDao.createPersonalDataElement(personalData);

            return true;
        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route createClosedQuestion = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl create closed question..");

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                System.out.println("SurveyController 1");

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);

                    System.out.println("" + request.queryParams("surveyId"));
                    System.out.println("" + request.queryParams("elementTitle"));
                    System.out.println("" + request.queryParams("questiontext"));
                    System.out.println("" + request.queryParams("answer1"));
                    System.out.println("" + request.queryParams("answer2"));
                    System.out.println("" + request.queryParams("answer3"));

                int surveyId = Integer.parseInt(request.queryParams("surveyId"));

                if ((request.queryParams("situation").toString().length() < 1) || (request.queryParams("elementTitle").toString().length() < 1)){
                    return false;
                }
                    System.out.println("SurveyController 2");
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{
                    System.out.println("right Format");

                    SurveyElement surveyElement = new SurveyElement(0, surveyId, 3, request.queryParams("elementTitle").toString());
                    int elementId = surveyDao.createNewSurveyElement(surveyElement);

                    System.out.println("SurveyController 3");

                    System.out.println("surveyElement.getElementTitle()):  " + surveyElement.getElementTitle());
                    ClosedQuestion closedQuestion = new ClosedQuestion(0, elementId, surveyElement.getElementTitle(),
                            request.queryParams("situation").toString(),
                            request.queryParams("questiontext").toString(),
                            request.queryParams("answer1").toString(),
                            request.queryParams("answer2").toString(),
                            request.queryParams("answer3").toString(),
                            request.queryParams("answer4").toString(),
                            request.queryParams("answer5").toString(),
                            request.queryParams("answer6").toString(),

                            Boolean.parseBoolean(request.queryParams("optionalTextfield")),
                            Boolean.parseBoolean(request.queryParams("multipleSelection")),

                            pictureFilename,
                            Integer.parseInt(request.queryParams("surveyId").toString()));

                    System.out.println("closed.toString.  " + closedQuestion.toString());
                    surveyDao.createClosedQuestion(closedQuestion);
                    System.out.println("SurveyController 4");

                }

            }catch (Exception e){
                System.out.println("EXCEPTION:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static String savePictureFile(Part file){

        String pictureFilename= null;
        if (file != null){

            String filename = file.getSubmittedFileName();
            System.out.println("filename::   -____" + filename);
            if (filename.length() >= 1) {
                if (filename.contains(".png") || filename.contains(".jpg")) {

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                    File dirPictures = new File(Application.picturesDir);
                    if (!dirPictures.exists()) {
                        dirPictures.mkdir();
                    }

                    String dtfDir = dtf.format(now);

                    File dir = new File(Application.picturesDir + "//" + dtfDir);

                    dir.mkdir();

                    try {
                        Files.copy(file.getInputStream(), Paths.get(Application.picturesDir + "//" + dtfDir, filename));
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                    pictureFilename = dtfDir + "/" + filename;
                } else {
                    pictureFilename = "wrongFormat";
                }
            }
        }
        return pictureFilename;
    }
}

