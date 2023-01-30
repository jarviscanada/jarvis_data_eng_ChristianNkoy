package ca.jrvs.apps.twitter.spring;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.controller.TwitterCLIApp;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.ApplicationContext;


//@Configuration
public class TwitterCLIBean {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCLIBean.class);
        TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
        app.run(args);
    }

    @Bean
    public TwitterCLIApp twitterCLIApp(Controller controller) {
        return new TwitterCLIApp(controller);
    }

    @Bean
    public Controller controller(Service service) {
        return new TwitterController(service);
    }

    @Bean
    public Service service(CrdDao dao) {
        return new TwitterService(dao);
    }

    @Bean
    public CrdDao crdDao(HttpHelper httpHelper) {
        return new TwitterDao(httpHelper);
    }
    @Bean
    HttpHelper helper() {
        String consumerKey = System.getenv("CONSUMER_KEY");
        String consumerSecret = System.getenv("CONSUMER_SECRET");
        String accessToken = System.getenv("ACCESS_TOKEN");
        String tokenSecret = System.getenv("TOKEN_SECRET");
        return new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    }
}
