import java.io.*;
import java.util.HashMap;
import java.util.Map;


import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.nio.file.*;

/*
* We import the Velocity template engine adapter we created
* */
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;



import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;
/*
 * ModelAndView is a spark tool that allows us to pass dynamic info like, vars
 * from our App.java file to our template files.
 * */
import spark. ModelAndView;


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



    /* Methods used for logging the multipart/form-data */
    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private static void logInfo(Request request, Path pic_file) throws IOException, ServletException {
        System.out.println("Uploaded file '" + getFileName(request.raw().getPart("uploaded_file")) + "'saved as '" + pic_file.toAbsolutePath() + "'");
    }

    public static void main (String[] args) {
        staticFileLocation("/public");

        enableDebugScreen();
        /* slf4j logger manenos */
        BasicConfigurator.configure();
       /*  Assigning the heroku port to our app. */
        port(getHerokuAssignedPort());

        File upload_dir = new File("upload");
        upload_dir.mkdir(); // create the upload directory if it doesn't exist

        staticFiles.externalLocation("upload");

        get("/", (request, response) ->{

            Map<String, Object> context = new HashMap<>();
            Map<String, String> data = new HashMap<>();

            data.put("test", "Hello Velocity World. This is awesome!");
            data.put("faith", "Evidence of things hoped for, Substance of things unseen");

            context.put("my_data", data);
            context.put("testing", "Because am the best you have ever and will ever see!");

            /* We access  everything in the context hash map using the key values */
            return new ModelAndView(context, "templates/hello.vtl");
        }, new VelocityTemplateEngine());

        post("/", (request, response) -> {
            Path pic_file = Files.createTempFile(upload_dir.toPath(), "", "");

            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (
                    InputStream input = request.raw().getPart("uploaded_file").getInputStream()
            ){
                /* getPart string needs to use same "name" as input field in form */
                Files.copy(input, pic_file, StandardCopyOption.REPLACE_EXISTING);
            }

            logInfo(request, pic_file);

            Map <String, String> context = new HashMap<>();

            context.put("pic_path", pic_file.getFileName().toString());

            return new ModelAndView(context, "templates/hello.vtl");

        }, new VelocityTemplateEngine());

    }
}