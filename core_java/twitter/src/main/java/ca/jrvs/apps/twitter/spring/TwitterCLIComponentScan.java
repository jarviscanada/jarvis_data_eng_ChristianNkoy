package ca.jrvs.apps.twitter.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ca.jrvs.apps.twitter.controller.TwitterCLIApp;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(value = "ca.jrvs.apps.twitter")
public class TwitterCLIComponentScan {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCLIComponentScan.class);
        TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
        app.run(args);
    }
}
