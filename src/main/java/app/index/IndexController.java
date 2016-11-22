package app.index;

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


public class IndexController {
    public static Route serveIndexPage = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("IndexController serveIndexPage");
            Map attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));

            attributes.put("users", userDao.getAllUserNames());

            attributes.put("currentPage", "index");
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.INDEX));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
