package app.login;

import app.Application;
import app.user.UserController;
import app.util.Path;
import app.util.ViewUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

import static app.Application.userDao;
import static app.util.RequestUtil.*;

public class LoginController {
    public static Route serveLoginPage = (Request request, Response response) -> {


        System.out.println("Hello Login");

        Map attributes = new HashMap<>();
        attributes.putAll(ViewUtil.getTemplateVariables(request));
        attributes.put("currentPage", "login");


        return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.LOGIN));
    };

    public static Route handleLoginPost = (Request request, Response response) -> {

        Map attributes = new HashMap<>();
        attributes.putAll(ViewUtil.getTemplateVariables(request));

        if (!UserController.authenticate(getQueryUsername(request), getQueryPassword(request))) {
            attributes.put("authenticationFailed", true);
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.LOGIN));
        }

        System.out.println("Login authenticationSucceeded = true");
        attributes.put("authenticationSucceeded", true);

        //test
        request.session().removeAttribute("loggedOut");

        request.session().attribute("currentUser", getQueryUsername(request));

        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }

        attributes.put("users", userDao.getAllUserNames());

        response.redirect(Path.Web.INDEX);
        return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.INDEX));
    };

    public static Route jsonHandleLoginPost = (Request request, Response response) -> {

        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(request.body(), HashMap.class);
        JSONObject object = new JSONObject(map);

        System.out.println("USERNAME: jsonHandleLoginPost:--  " + object.get("username").toString());
        System.out.println("password: jsonHandleLoginPost:--  " + object.get("password").toString());

        if (UserController.authenticate(object.get("username").toString(), object.get("password").toString())) {
            request.session().removeAttribute("loggedOut");
            request.session().attribute("currentUser", object.get("username").toString());

            System.out.println("request.session().attribute(currentUser)):   " + request.session().attribute("currentUser"));
            return true;
        }else{
            request.session().removeAttribute("currentUser");
            return false;
        }


    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    // The origin of the request (request.pathInfo()) is saved in the session so
    // the app.user can be redirected back after app.login
    public static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute("currentUser") == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
    };



}
