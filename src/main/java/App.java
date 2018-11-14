import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

public class App {
    final static Logger logger = Logger.getLogger(App.class);
    public static void main (String[] args) {
        BasicConfigurator.configure();
        get("/", (request, response) -> "Hello Maven!");
    }
}