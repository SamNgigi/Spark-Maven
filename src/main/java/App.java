import java.util.HashMap;
/*
 * ModelAndView is a spark tool that allows us to pass dynamic info like, vars
 * from our App.java file to our template files.
 * */
import spark. ModelAndView;
/*
* We import the Velocity template engine adapter we created
* */
import spark.template.velocity.VelocityTemplateEngine;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.*;

public class App {
    /* slf4j logger manenos */
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
        staticFileLocation("/public");
        /* slf4j logger manenos */
        BasicConfigurator.configure();
       /*  Assigning the heroku port to our app. */
        port(getHerokuAssignedPort());

        get("/", (request, response) ->{
            return new ModelAndView(new HashMap(), "templates/hello.vtl");
        }, new VelocityTemplateEngine());
       
    }
}