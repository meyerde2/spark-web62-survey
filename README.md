spark-web62-survey
==============================================

How to deploy the application to external jetty webserver?
- delete the main()-method
- add "implements SparkApplication" to "public class Application"
- enable the init()-method.

```java

public class Application implements SparkApplication{


    public static UserDao userDao;
    public static SurveyDao surveyDao;
    public static EvaluationDao evaluationDao;
    public static String picturesDir;
    public static FreeMarkerEngine freeMarkerEngine;

@Override
    public void init() {

            Spark.staticFileLocation("/public");
            Spark.externalStaticFileLocation("/pictures");

            picturesDir=new File("").getAbsolutePath().toString() + "\\target\\classes\\public\\pictures";

            freeMarkerEngine = new FreeMarkerEngine();
            Configuration freeMarkerConfiguration = new Configuration();

            freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Application.class, "/public/templates/"));
            freeMarkerEngine.setConfiguration(freeMarkerConfiguration);
        .....
    }

```
