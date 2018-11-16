package spark.template.velocity;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import spark.ModelAndView;
import spark.TemplateEngine;

public class VelocityTemplateEngine extends TemplateEngine {
    private final VelocityEngine velocity_engine;

    public VelocityTemplateEngine () {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty(
                "class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
        );
        velocity_engine = new org.apache.velocity.app.VelocityEngine(properties);
    }

    public VelocityTemplateEngine(VelocityEngine velocity_engine) {
        if (velocity_engine == null){
            throw new IllegalArgumentException("velocity_engine must not be null");
        }
        this.velocity_engine = velocity_engine;
    }

    @Override
    public String render(ModelAndView model_and_view){
        Template template = velocity_engine.getTemplate(model_and_view.getViewName());
        Object model = model_and_view.getModel();
        if(model instanceof  Map){
            Map<?, ?> model_map = (Map<?, ?>) model;
            VelocityContext context = new VelocityContext(model_map);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } else {
            throw new IllegalArgumentException(
                    "model_and_view must be of type java.util.Map"
            );
        }
    }
}