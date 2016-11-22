package app.survey;


import app.Application;
import app.login.LoginController;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

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

            attributes.put("surveyElements", surveyDao.getAllSurveyElements(0));

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
}

