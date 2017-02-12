package app.user;

import app.Application;
import app.login.LoginController;
import app.util.Path;
import app.util.ViewUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
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
import static app.util.RequestUtil.getParamUsername;

public class UserController {


    // Authenticate the app.user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, user.getSalt());
        return hashedPassword.equals(user.getHashedPassword());
    }

    public static String generateHashedPassword(String newPassword, String salt) {

        String hashedPassword = BCrypt.hashpw(newPassword, salt);

        return hashedPassword;
    }

    public static String generateSalt(){

        String newSalt = BCrypt.gensalt();
        return newSalt;
    }

    public static int getCurrentUserRole(String username){

        return userDao.getUserByUsername(username).getRole();
    }


    public static Route serveUsercontrolPage = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (getCurrentUserRole(request.session().attribute("currentUser")) ==1){

            if (clientAcceptsHtml(request)) {
                Map<String, Object> attributes = new HashMap<>();
                attributes.putAll(ViewUtil.getTemplateVariables(request));
                attributes.put("currentPage", "usercontrol");


                attributes.put("users", userDao.getAllUsers());

                return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.USERCONTROL));
            }
        }
            if (clientAcceptsJson(request)) {
                return dataToJson(userDao.getAllUserNames());
            }

        return ViewUtil.forbidden.handle(request, response);
    };

    public static Route jsonGetAllUsers = (Request request, Response response) -> {
        response.status(200);

        User user = userDao.getUserByUsername(getParamUsername(request));
        System.out.println("user.getRole() |||||  " + user.getRole());
        return dataToJson(userDao.getAllUsersForRole(user.getUsername(), user.getRole()));
    };

    public static Route jsonGetUserByUsername = (Request request, Response response) -> {
        response.status(200);
        return dataToJson(userDao.getUserByUsername(getParamUsername(request)));
    };

    public static Route serveNewUser = (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {

            System.out.println("username:  " + request.queryParams("username"));
            System.out.println("firstname:  " + request.queryParams("firstname"));
            System.out.println("lastname:  " + request.queryParams("lastname"));
            System.out.println("password:  " + request.queryParams("password"));
            System.out.println("passwordConfirmed:  " + request.queryParams("passwordConfirmed"));


            if (request.queryParams("password").length() >= 6 && request.queryParams("password").equals(request.queryParams("passwordConfirmed"))){
                String salt = generateSalt();
                String hashedPassword = generateHashedPassword(request.queryParams("password"), salt);

                System.out.println("role:  " + request.queryParams("role"));

                User newUser = new User(0, request.queryParams("username"), request.queryParams("firstname"), request.queryParams("lastname"), salt, hashedPassword,  Integer.parseInt(request.queryParams("role")), 0);

                System.out.println("Status createUser" + userDao.createUser(newUser));

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("users", userDao.getAllUsers());

                response.redirect(Path.Web.USERCONTROL);

                return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.USERCONTROL));

            }else{
                return false;
            }

        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route jsonCreateNewUser = (Request request, Response response) -> {

        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(request.body(), HashMap.class);
        JSONObject object = new JSONObject(map);


        System.out.println("_________________________________________json create new User__________________________________________________________________________");
            if (object.get("password").toString().length() >= 6 && object.get("password").toString().equals(object.get("confirmedPassword").toString())) {
                String salt = generateSalt();
                String hashedPassword = generateHashedPassword(object.get("password").toString(), salt);

                System.out.println("role:  " + object.get("role").toString());

                User newUser = new User(0,object.get("username").toString(), object.get("firstname").toString(), object.get("lastname").toString(), salt, hashedPassword, Integer.parseInt(object.get("role").toString()), 0);

                System.out.println("Status createUser" + userDao.createUser(newUser));
                return dataToJson(userDao.getUserByUsername(newUser.getUsername()));
            }
        return "error: passwords not equal";
    };

    public static Route serveNewUserLogin = (Request request, Response response) -> {

        System.out.println("username:  " + request.queryParams("username"));
        System.out.println("password:  " + request.queryParams("password"));
        System.out.println("passwordConfirmed:  " + request.queryParams("passwordConfirmed"));


        if (request.queryParams("password").length() >= 6 && request.queryParams("password").equals(request.queryParams("passwordConfirmed"))){
            String salt = generateSalt();
            String hashedPassword = generateHashedPassword(request.queryParams("password"), salt);

            System.out.println("role:  " + request.queryParams("role"));

            User newUser = new User(0, request.queryParams("username"), "", "", salt, hashedPassword,  2, 0);

            userDao.getAllUsers();

            String returnString;
            if(userDao.createUser(newUser)){
                returnString = "success";
            }else{
                returnString = "usernameAlreadyExists";
            }

            return returnString;

        }else {

            return "pwFailed";

        }

    };


    public static Route serveOneUser= (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.putAll(ViewUtil.getTemplateVariables(request));
            attributes.put("currentPage", "usercontrol");


            User user = userDao.getUserByUsername(getParamUsername(request));
            attributes.put("user", user);

            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.UPDATEUSER));
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(userDao.getAllUserNames());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };


    public static Route jsonUpdateUser= (Request request, Response response) -> {

        System.out.println("body:  " + request.body());

        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        map = mapper.readValue(request.body(), HashMap.class);
        JSONObject object = new JSONObject(map);

        User user = userDao.getUserByUsername(object.get("username").toString());

        user.setFirstname(object.get("firstname").toString());
        user.setLastname(object.get("lastname").toString());

        if (object.get("password") !=null && object.get("confirmedPassword") !=null && object.get("password").toString().length() > 0 && object.get("password").toString().equals(object.get("confirmedPassword").toString())){

            String salt = generateSalt();
            String hashedPassword = generateHashedPassword(object.get("password").toString(), salt);

            user.setSalt(salt);
            user.setHashedPassword(hashedPassword);
            System.out.println("new PW");
        }

        user.setRole(Integer.parseInt(object.get("role").toString()));
        userDao.updateUser(user);

        return dataToJson(userDao.getUserByUsername(user.getUsername()));

    };


    public static Route updateUser= (Request request, Response response) -> {

        LoginController.ensureUserIsLoggedIn(request, response);

        if (clientAcceptsHtml(request)) {
            Map<String, Object> attributes = new HashMap<>();

            User user = userDao.getUserByUsername(request.queryParams("username"));


            System.out.println("username:  " + request.queryParams("username"));
            System.out.println("firstname:  " + request.queryParams("firstname"));
            System.out.println("lastname:  " + request.queryParams("lastname"));
            System.out.println("password:  " + request.queryParams("password"));
            System.out.println("passwordConfirmed:  " + request.queryParams("passwordConfirmed"));

            user.setFirstname(request.queryParams("firstname"));
            user.setLastname(request.queryParams("lastname"));

            if (request.queryParams("password") !=null && !request.queryParams("password").isEmpty() && request.queryParams("password").equals(request.queryParams("passwordConfirmed"))){
                String salt = generateSalt();
                String hashedPassword = generateHashedPassword(request.queryParams("password"), salt);

                user.setSalt(salt);
                user.setHashedPassword(hashedPassword);

            }

            user.setRole(Integer.parseInt(request.queryParams("role")));
            userDao.updateUser(user);
            attributes.put("user", user);

            response.redirect(Path.Web.USERCONTROL);
            return Application.freeMarkerEngine.render(new ModelAndView(attributes, Path.Template.USERCONTROL));
        }

        return ViewUtil.notAcceptable.handle(request, response);
    };
}
