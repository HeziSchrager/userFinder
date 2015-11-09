package www.mfg.magic;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@ComponentScan
@EnableWebMvc
@PropertySource("file:userFinder.properties")
public class Application {
    public static void main(String[] args) throws BeansException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        // context.close();
    }
}
