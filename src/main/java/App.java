import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

public class App {
    final static Logger logger = Logger.getLogger(App.class);

    static int getHerokuAssignedPort () {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    public static void main (String[] args) {
        BasicConfigurator.configure();
        get("/", (request, response) -> "Hello Maven! Am the best baby!!");
    }
}