import app.evaluation.EvaluationDao;
import app.evaluation.EvaluationDaoImpl;
import org.junit.Test;
import org.sql2o.Sql2o;

/**
 * Created by Dennis on 06.12.2016.
 */
public class TestClass {

    @Test
    public void testGameId() {

        int i = 1 +1;

        //System.out.println("i: " + i);
        String DB_URL = "jdbc:mysql://localhost:3306/survey";
        String USER = "root";
        String PASS = "";
        Sql2o sql2o = new Sql2o(DB_URL, USER, PASS);


        EvaluationDao evaluationDao = new EvaluationDaoImpl(sql2o);


        System.out.println(evaluationDao.getCountOfDifferentLocations(3, 73));


    }
}
