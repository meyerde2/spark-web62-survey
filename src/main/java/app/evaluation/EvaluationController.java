package app.evaluation;

import app.Application;
import app.login.LoginController;
import app.util.Path;
import app.util.ViewUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.Application.userDao;
import static app.util.JsonUtil.dataToJson;
import static app.util.RequestUtil.clientAcceptsHtml;
import static app.util.RequestUtil.clientAcceptsJson;
import static app.util.RequestUtil.getParamId;


public class EvaluationController {

    public static Route serveEvaluationPage = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("EvaluationController serveEvaluationPage");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "evaluation");

            int surveyId = Integer.parseInt(getParamId(request));


            System.out.println("SurveyId=  " + surveyId);


            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.EVALUATION));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
