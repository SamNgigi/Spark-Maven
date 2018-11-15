import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

public class App {
    /* sl4j logger manenos */
    final static Logger logger = Logger.getLogger(App.class);
    /* Responsible for getting the heroku port for our app.  */
    static int getHerokuAssignedPort () {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    public static void main (String[] args) {
        /* sl4j logger manenos */
        BasicConfigurator.configure();
       /*  Assigning the heroku port to our app. */
        port(getHerokuAssignedPort());
        get("/", (request, response) -> "Hello Maven!\nI am the best that ever was and will be!!\nI am plethorically blessed!\nI am so happy and so very grateful for the fact fact that I am now earning $1 million a second.");
    }
}