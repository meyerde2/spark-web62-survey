package app.survey;


import app.Application;
import app.login.LoginController;
import app.survey.elements.*;
import app.survey.executionElements.*;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import com.google.common.base.Strings;
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
import java.util.List;
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

            attributes.put("surveyElements", surveyDao.getAllSurveyElements(surveyId));

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


    public static Route updateTextElement = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl updateTextElement----------------");

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
                System.out.println("right Format" + pictureFilename);

                Text text = new Text(0, Integer.parseInt(request.queryParams("elementId")),
                        request.queryParams("elementTitle").toString(),
                        request.queryParams("textElement").toString(),
                        pictureFilename,
                        Integer.parseInt(request.queryParams("surveyId").toString()));

                surveyDao.updateTextElement(text);
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


    public static Route updatePersonalDataElement = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl UPDATE  PersonalDataElement----------------");

            try {

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                request.raw().getPart("picture");


                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));


                if ((request.queryParams("elementTitle").toString().length() < 1) ||
                        (!Boolean.parseBoolean(request.queryParams("firstname")) &&
                                !Boolean.parseBoolean(request.queryParams("lastname")) &&
                                !Boolean.parseBoolean(request.queryParams("age")) &&
                                !Boolean.parseBoolean(request.queryParams("gender")) &&
                                !Boolean.parseBoolean(request.queryParams("location")))) {
                    System.out.println("false - - - - ");
                    return false;
                }

                PersonalData personalData = new PersonalData(0, Integer.parseInt(request.queryParams("elementId")),
                        request.queryParams("elementTitle"),
                        Boolean.parseBoolean(request.queryParams("firstname")),
                        Boolean.parseBoolean(request.queryParams("lastname")),
                        Boolean.parseBoolean(request.queryParams("age")),
                        Boolean.parseBoolean(request.queryParams("gender")),
                        Boolean.parseBoolean(request.queryParams("location")),
                        Integer.parseInt(request.queryParams("surveyId")));

                surveyDao.updatePersonalDataElement(personalData);
            }catch (Exception e){
                System.out.println(e.toString());
            }
            return true;
        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route createClosedQuestion = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);

                int surveyId = Integer.parseInt(request.queryParams("surveyId"));

                if (request.queryParams("elementTitle").length() < 1){
                    return false;
                }
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{

                    SurveyElement surveyElement = new SurveyElement(0, surveyId, 3, request.queryParams("elementTitle"));
                    int elementId = surveyDao.createNewSurveyElement(surveyElement);

                    ClosedQuestion closedQuestion = new ClosedQuestion(0, elementId, surveyElement.getElementTitle(),
                            request.queryParams("situation"), request.queryParams("questiontext"), request.queryParams("answer1"),
                            request.queryParams("answer2"), request.queryParams("answer3"), request.queryParams("answer4"),
                            request.queryParams("answer5"), request.queryParams("answer6"),
                            Boolean.parseBoolean(request.queryParams("optionalTextfield")), Boolean.parseBoolean(request.queryParams("multipleSelection")),
                            pictureFilename, Integer.parseInt(request.queryParams("surveyId")));

                    surveyDao.createClosedQuestion(closedQuestion);

                }

            }catch (Exception e){
                System.out.println("Exception:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route updateClosedQuestion = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl UPDATE closed question..");

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);

                System.out.println("" + request.queryParams("surveyId"));
                System.out.println("" + request.queryParams("elementTitle"));
                System.out.println("" + request.queryParams("questiontext"));
                System.out.println("" + request.queryParams("answer1"));
                System.out.println("" + request.queryParams("answer2"));
                System.out.println("" + request.queryParams("answer3"));


                if (request.queryParams("elementTitle").toString().length() < 1){
                    return false;
                }
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{
                    System.out.println("right Format");

                    ClosedQuestion closedQuestion = new ClosedQuestion(0, Integer.parseInt(request.queryParams("elementId").toString()), request.queryParams("elementTitle").toString(),
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

                    surveyDao.updateClosedQuestion(closedQuestion);

                }

            }catch (Exception e){
                System.out.println("EXCEPTION:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route createOpenQuestion = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl create OPEN question..");

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);

                System.out.println("" + request.queryParams("surveyId"));
                System.out.println("" + request.queryParams("elementTitle"));
                System.out.println("" + request.queryParams("questiontext"));
                System.out.println("" + request.queryParams("situation"));


                int surveyId = Integer.parseInt(request.queryParams("surveyId"));

                if (request.queryParams("elementTitle").toString().length() < 1){
                    return false;
                }
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{
                    System.out.println("right Format");

                    SurveyElement surveyElement = new SurveyElement(0, surveyId, 4, request.queryParams("elementTitle").toString());
                    int elementId = surveyDao.createNewSurveyElement(surveyElement);

                    System.out.println("surveyElement.getElementTitle()):  " + surveyElement.getElementTitle());
                    OpenQuestion openQuestion = new OpenQuestion(0, elementId, surveyElement.getElementTitle(),
                            request.queryParams("situation").toString(),
                            request.queryParams("questiontext").toString(),
                            pictureFilename,
                            Integer.parseInt(request.queryParams("surveyId").toString()));

                    surveyDao.createOpenQuestion(openQuestion);

                }

            }catch (Exception e){
                System.out.println("EXCEPTION:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route updateOpenQuestion = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl UPDATE OPEN question..");

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);

                if (request.queryParams("elementTitle").toString().length() < 1){
                    return false;
                }
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{
                    System.out.println("right Format");


                    OpenQuestion openQuestion = new OpenQuestion(0, Integer.parseInt(request.queryParams("elementId").toString()), request.queryParams("elementTitle").toString(),
                            request.queryParams("situation").toString(),
                            request.queryParams("questiontext").toString(),
                            pictureFilename,
                            Integer.parseInt(request.queryParams("surveyId").toString()));

                    surveyDao.updateOpenQuestion(openQuestion);

                }

            }catch (Exception e){
                System.out.println("EXCEPTION:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route createScoreTable = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl create scoretable..");

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);

                System.out.println("" + request.queryParams("surveyId"));
                System.out.println("" + request.queryParams("elementTitle"));
                System.out.println("" + request.queryParams("questiontext"));
                System.out.println("" + request.queryParams("situation"));


                int surveyId = Integer.parseInt(request.queryParams("surveyId"));

                if ((request.queryParams("situation").toString().length() < 1) || (request.queryParams("elementTitle").toString().length() < 1)){
                    return false;
                }
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{
                    System.out.println("right Format");

                    SurveyElement surveyElement = new SurveyElement(0, surveyId, 5, request.queryParams("elementTitle").toString());
                    int elementId = surveyDao.createNewSurveyElement(surveyElement);

                    ScoreTable scoreTable = new ScoreTable(0, elementId, surveyElement.getElementTitle(),
                            request.queryParams("situation").toString(),
                            request.queryParams("questiontext").toString(),
                            request.queryParams("criterion1").toString(),
                            request.queryParams("criterion2").toString(),
                            request.queryParams("criterion3").toString(),
                            request.queryParams("criterion4").toString(),
                            request.queryParams("criterion5").toString(),
                            request.queryParams("criterion6").toString(),
                            pictureFilename,
                            Integer.parseInt(request.queryParams("surveyId").toString()));

                    surveyDao.createScoreTableQuestion(scoreTable);

                }

            }catch (Exception e){
                System.out.println("EXCEPTION:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route updateScoreTable = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyControl create scoretable..");

            try{

                Map attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));

                if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                    request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                }

                Part file = request.raw().getPart("picture");
                String pictureFilename = savePictureFile(file);


                if ((request.queryParams("situation").toString().length() < 1) || (request.queryParams("elementTitle").toString().length() < 1)){
                    return false;
                }
                if("wrongFormat".equals(pictureFilename)){
                    attributes.put("pictureUpload", "wrongFormat");
                    return false;
                }else{
                    System.out.println("right Format");


                    ScoreTable scoreTable = new ScoreTable(0, Integer.parseInt(request.queryParams("elementId").toString()), request.queryParams("elementTitle").toString(),
                            request.queryParams("situation").toString(),
                            request.queryParams("questiontext").toString(),
                            request.queryParams("criterion1").toString(),
                            request.queryParams("criterion2").toString(),
                            request.queryParams("criterion3").toString(),
                            request.queryParams("criterion4").toString(),
                            request.queryParams("criterion5").toString(),
                            request.queryParams("criterion6").toString(),
                            pictureFilename,
                            Integer.parseInt(request.queryParams("surveyId").toString()));

                    surveyDao.updateScoreTableQuestion(scoreTable);

                }

            }catch (Exception e){
                System.out.println("EXCEPTION:   " + e.toString());
            }

            return true;

        }

        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route serveUpdateSurveyElement = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        System.out.println("------------------------UPDATE--------------------------------");
        switch (getParamElementtype(request)) {
            case 1:
                return dataToJson(surveyDao.getTextElement(getParamElementId(request)));
            case 2:
                return dataToJson(surveyDao.getPersonalData(getParamElementId(request)));
            case 3:
                return dataToJson(surveyDao.getClosedQuestion(getParamElementId(request)));
            case 4:
                return dataToJson(surveyDao.getOpenedQuestion(getParamElementId(request)));
            case 5:
                return dataToJson(surveyDao.getScoreTable(getParamElementId(request)));
        }
            return null;

    };

    public static Route deleteSurveyElement = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController createNewServery");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "survey");

            surveyDao.deleteSurveyElementById(getParamSurveyId(request), getParamElementId(request));

            attributes.put("currentSurvey", surveyDao.getSurveyById(getParamSurveyId(request)));

            response.redirect("/surveycreation/" + getParamSurveyId(request) + "/");
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYCREATION));

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route serveSurveyExecutionPage = (Request request, Response response) -> {

        if (clientAcceptsHtml(request)) {

            System.out.println("SurveyController serveSurveyExecutionPage");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "survey");

            ///
            ////


            ///Existiert bereits eine Session-ID?
            System.out.println("session_id:  " + request.session().id());

            int surveyId = Integer.parseInt(getParamId(request));
            Survey  survey = surveyDao.getSurveyById(surveyId);

            if (!survey.isPublished){
                attributes.put("currentSurvey", surveyDao.getSurveyById(Integer.parseInt(getParamId(request))));
                attributes.put("draft", true);


                return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYEND));
            }
            String ipAddress = request.ip();
            String sessionId = request.session().id();

            int lastQuestionId = 0;

            int executionId = surveyDao.getLatestExecutionId(sessionId,surveyId,ipAddress);

            lastQuestionId = surveyDao.getLastAnsweredQuestionId(survey.isSessionId(), sessionId, survey.isIpAddress(), ipAddress, surveyId, executionId);

            List<SurveyElement> allSurveyElements = surveyDao.getAllSurveyElements(surveyId);


            System.out.println("lastQuestionId:   " + lastQuestionId);
            System.out.println("executionId:   " + executionId);

            int elementId = 0;
            int elementType = 0;
            if(lastQuestionId == 0){
                //new survey execution
                System.out.println("new survey execution = lastQuestionId == " + lastQuestionId);

                elementType = allSurveyElements.get(0).getElementType();
                switch (elementType){
                    case 1:
                        Text text = surveyDao.getTextElement(allSurveyElements.get(0).getElementId());
                        attributes.put("currentSurveyText", text);
                        elementId = text.getElementId();
                        break;
                    case 2:
                        PersonalData personalData = surveyDao.getPersonalData(allSurveyElements.get(0).getElementId());
                        attributes.put("currentSurveyPersonalData", personalData);
                        elementId = personalData.getElementId();
                        break;
                    case 3:
                        ClosedQuestion closedQuestion = surveyDao.getClosedQuestion(allSurveyElements.get(0).getElementId());
                        attributes.put("currentSurveyClosedQuestion", closedQuestion);
                        elementId = closedQuestion.getElementId();
                        break;
                    case 4:
                        OpenQuestion openQuestion = surveyDao.getOpenedQuestion(allSurveyElements.get(0).getElementId());
                        attributes.put("currentSurveyOpenQuestion", openQuestion);
                        elementId = openQuestion.getElementId();
                        break;
                    case 5:
                        ScoreTable scoreTable = surveyDao.getScoreTable(allSurveyElements.get(0).getElementId());
                        attributes.put("currentSurveyScoreTable", scoreTable);
                        elementId = scoreTable.getElementId();
                        break;
                }

            }else if(lastQuestionId < allSurveyElements.size()){
                // next Question

                System.out.println("next Question == lastQuestionId ==" + lastQuestionId + " allsurveyelements.size()= " +allSurveyElements.size() );
                elementType = allSurveyElements.get(lastQuestionId).getElementType();
                switch (elementType){
                    case 1:
                        Text text = surveyDao.getTextElement(allSurveyElements.get(lastQuestionId).getElementId());
                        attributes.put("currentSurveyText", text);
                        elementId = text.getElementId();
                        break;
                    case 2:
                        PersonalData personalData = surveyDao.getPersonalData(allSurveyElements.get(lastQuestionId).getElementId());
                        attributes.put("currentSurveyPersonalData", personalData);
                        elementId = personalData.getElementId();
                        break;
                    case 3:
                        ClosedQuestion closedQuestion = surveyDao.getClosedQuestion(allSurveyElements.get(lastQuestionId).getElementId());
                        attributes.put("currentSurveyClosedQuestion", closedQuestion);
                        elementId = closedQuestion.getElementId();
                        break;
                    case 4:
                        OpenQuestion openQuestion = surveyDao.getOpenedQuestion(allSurveyElements.get(lastQuestionId).getElementId());
                        attributes.put("currentSurveyOpenQuestion", openQuestion);
                        elementId = openQuestion.getElementId();
                        break;
                    case 5:
                        ScoreTable scoreTable = surveyDao.getScoreTable(allSurveyElements.get(lastQuestionId).getElementId());
                        attributes.put("currentSurveyScoreTable", scoreTable);
                        elementId = scoreTable.getElementId();
                        break;
                }


            }else{
                //survey end
                System.out.println("END OF GAME!!!!!!!!!!!!!!!!!!!!");

                System.out.println("survey.isIpAddress().  " + survey.isIpAddress());
                System.out.println("survey.isSessionId().  " + survey.isSessionId());
                if (survey.isIpAddress() && survey.isSessionId()) {

                    attributes.put("multipleSurveyExecution", false);

                }else if(survey.isIpAddress() ){
                    attributes.put("multipleSurveyExecution", false);

                }else{
                    if(survey.isSessionId()){
                        attributes.put("multipleSurveyExecution", false);
                    }else{
                        System.out.println("UPDAT EXECUTION ID");
                        surveyDao.updateExecutionEnd(executionId);
                        attributes.put("multipleSurveyExecution", true);
                    }
                }
                attributes.put("currentSurvey", surveyDao.getSurveyById(Integer.parseInt(getParamId(request))));

                return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYEND));
            }


            System.out.println("Client-IP-Address:  ____ : " + request.ip());

            attributes.put("elementId", elementId);
            attributes.put("elementType", elementType);
            attributes.put("currentSurvey", surveyDao.getSurveyById(Integer.parseInt(getParamId(request))));

            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYEXECUTION));

        }
        return ViewUtil.notAcceptable.handle(request, response);
    };



    public static Route saveSurveyExecutionQuestion = (Request request, Response response) -> {

        if (clientAcceptsHtml(request)) {

            System.out.println("---------------------SurveyController SAVE SURVEY EXECUTION------------------");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "survey");

            ///
            ////


            ///Existiert bereits eine Session-ID?
            System.out.println("session_id:  " + request.session().id());
            String sessionId = request.session().id();
            int surveyId= Integer.parseInt(request.queryParams("surveyId"));

            Survey  survey = surveyDao.getSurveyById(surveyId);
            String ipAddress = request.ip();

            int executionId = surveyDao.getLatestExecutionId(sessionId,surveyId,ipAddress);

            int questionId = surveyDao.getLastAnsweredQuestionId(survey.isSessionId(), sessionId, survey.isIpAddress(), ipAddress, surveyId, executionId) + 1;
            int surveyCounterId = 0;

            if(questionId == 1){
                surveyCounterId = surveyDao.insertExecutionEnd(sessionId, surveyId, ipAddress);
            }else{
                surveyCounterId = surveyDao.getLatestExecutionId(sessionId,surveyId,ipAddress);

            }

            System.out.println("SURVEY---------COUNTER-----ID:::     " + surveyCounterId);

            int elementType = Integer.parseInt(request.queryParams("elementType"));
            int elementId = Integer.parseInt(request.queryParams("elementId"));
            System.out.println("ELEMENTID ==========" + elementId);
            System.out.println("ELEMENTTYPE ==========" + elementType);

            int resultId = 0;
                switch (elementType){
                    case 1:
                        //TextExecution
                        surveyDao.saveExecutionText(new TextExecution(0, surveyId, elementId,elementType, sessionId, ipAddress, questionId, surveyCounterId));
                        break;
                    case 2:
                        //PersonalDataExecution
                        if (!Strings.isNullOrEmpty(request.queryParams("firstname"))
                                || !Strings.isNullOrEmpty(request.queryParams("lastname"))
                                || !Strings.isNullOrEmpty(request.queryParams("age"))
                                || !Strings.isNullOrEmpty(request.queryParams("gender"))
                                || !Strings.isNullOrEmpty(request.queryParams("location"))){

                            //new Personal Data Object needed
                            surveyDao.saveExecutionPersonalData(new PersonalDataExecution(0, surveyId, elementId, elementType, sessionId, ipAddress, questionId,
                                    request.queryParams("firstname"),
                                    request.queryParams("lastname"),
                                    Integer.parseInt(request.queryParams("age")),
                                    Integer.parseInt(request.queryParams("gender")),
                                    request.queryParams("location"), surveyCounterId));
                        }else{
                            resultId = -1;
                        }
                        break;
                    case 3:
                        //closedQuestionExecution
                        boolean answer1 = false, answer2 = false, answer3 = false, answer4 = false, answer5 =false, answer6 = false;
                        if(!Strings.isNullOrEmpty(request.queryParams("answer"))){

                            switch(Integer.parseInt(request.queryParams("answer"))){
                                case 1:
                                    answer1 = true;
                                    break;
                                case 2:
                                    answer2 = true;
                                    break;
                                case 3:
                                    answer3 = true;
                                    break;
                                case 4:
                                    answer4 = true;
                                    break;
                                case 5:
                                    answer5 = true;
                                    break;
                                case 6:
                                    answer6 = true;
                                    break;
                            }

                        }else{

                            answer1 = Boolean.parseBoolean(request.queryParams("answer1"));
                            answer2 = Boolean.parseBoolean(request.queryParams("answer2"));
                            answer3 = Boolean.parseBoolean(request.queryParams("answer3"));
                            answer4 = Boolean.parseBoolean(request.queryParams("answer4"));
                            answer5 = Boolean.parseBoolean(request.queryParams("answer5"));
                            answer6 = Boolean.parseBoolean(request.queryParams("answer6"));

                        }
                        String optionalTextfield;
                        if (Strings.isNullOrEmpty(request.queryParams("optionalTextfield"))){
                            optionalTextfield = null;
                        }else{
                            optionalTextfield = request.queryParams("optionalTextfield");
                        }

                        surveyDao.saveExecutionClosedQuestion(new ClosedQuestionExecution(0, surveyId, elementId, elementType, sessionId, ipAddress, questionId,
                                answer1, answer2, answer3, answer4, answer5, answer6,
                                optionalTextfield, surveyCounterId));
                        break;
                    case 4:
                        surveyDao.saveExecutionOpenQuestion(new OpenQuestionExecution(0, surveyId, elementId, elementType, sessionId, ipAddress, questionId, request.queryParams("textfield"), surveyCounterId));
                        break;
                    case 5:
                        Integer criterion1 = null, criterion2 = null, criterion3 = null, criterion4 = null, criterion5 = null, criterion6 = null;

                        if(!Strings.isNullOrEmpty(request.queryParams("criterion1")))
                                criterion1 = Integer.parseInt(request.queryParams("criterion1"));

                        if(!Strings.isNullOrEmpty(request.queryParams("criterion2")))
                            criterion2 = Integer.parseInt(request.queryParams("criterion2"));

                        if(!Strings.isNullOrEmpty(request.queryParams("criterion3")))
                            criterion3 = Integer.parseInt(request.queryParams("criterion3"));

                        if(!Strings.isNullOrEmpty(request.queryParams("criterion4")))
                            criterion4 = Integer.parseInt(request.queryParams("criterion4"));

                        if(!Strings.isNullOrEmpty(request.queryParams("criterion5")))
                            criterion5 = Integer.parseInt(request.queryParams("criterion5"));

                        if(!Strings.isNullOrEmpty(request.queryParams("criterion6")))
                            criterion6 = Integer.parseInt(request.queryParams("criterion6"));


                        surveyDao.saveExecutionScoreTable(new ScoreTableExecution(0, surveyId, elementId, elementType, sessionId, ipAddress, questionId,
                                criterion1, criterion2, criterion3, criterion4, criterion5, criterion6, surveyCounterId));
                        break;
                }


            System.out.println("RESULT OF SAVING  -------------- : " + resultId);
            attributes.put("elementId", elementId);
            attributes.put("elementType", elementType);
            attributes.put("currentSurvey", surveyDao.getSurveyById(surveyId));

            response.redirect("/survey/execution/" + surveyId +"/");
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.SURVEYEXECUTION));

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

