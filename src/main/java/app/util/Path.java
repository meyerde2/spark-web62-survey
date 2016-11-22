package app.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public final class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {

        @Getter public static final String INDEX = "/index/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String NEWUSER = "/loginNewUser/";
        @Getter public static final String USERCONTROL = "/usercontrol/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String EVALUATION ="/evaluation/";
        @Getter public static final String SURVEYCREATION ="/surveycreation/";
        @Getter public static final String SURVEY ="/surveyoverview/";

    }

    public static class Template {
        public static final String INDEX = "index/index.ftl";
        public static final String LOGIN = "login/login.ftl";
        public static final String USERCONTROL = "usercontrol/usercontrol.ftl";
        public static final String SURVEYCREATION = "survey/survey.ftl";
        public static final String EVALUATION = "evaluation/evaluation.ftl";
        public static final String NOT_FOUND = "notFound.ftl";
        public static final String SURVEYEND = "survey/surveyend.ftl";
        public static final String SURVEYSTART = "survey/surveystart.ftl";
        public static final String SURVEYOVERVIEW = "survey/surveyOverview.ftl";
        public static final String UPDATESURVEY = "survey/updateSurvey.ftl";
        public static final String UPDATEUSER = "usercontrol/updateUser.ftl";
    }

}
