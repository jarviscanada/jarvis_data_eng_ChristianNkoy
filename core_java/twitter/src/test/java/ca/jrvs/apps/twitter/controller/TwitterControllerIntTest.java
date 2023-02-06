package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TweetUtil;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterControllerIntTest {

    private TwitterController controller;
    private TwitterService service;
    private TwitterDao dao;

    private Tweet buildTweet(String text) throws Exception {
        //build tweet
        Double lon = 10d;
        Double lat = 11d;
        return TweetUtil.buildTweet(text, lon, lat);
    }

    @Before
    public void setup() {

        String consumerKey = System.getenv("CONSUMER_KEY");
        String consumerSecret = System.getenv("CONSUMER_SECRET");
        String accessToken = System.getenv("ACCESS_TOKEN");
        String tokenSecret = System.getenv("TOKEN_SECRET");
        System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

        // Set up dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);

        // Pass dependency
        this.dao = new TwitterDao(httpHelper);
        this.service = new TwitterService(dao);
        this.controller = new TwitterController(service);
    }

    @Test
    public void postTweet() throws Exception {
        String[] args = {"post", "Violets are blue", "-14:19"};
        Tweet createdTweet = controller.postTweet(args);
        String [] fields = {"id_str", "text", "coordinates"};

        //check if tweet is created
        Tweet foundTweet = service.showTweet(createdTweet.getId_str(), fields);
        assertEquals(foundTweet.getText(), createdTweet.getText());
        assertEquals(foundTweet.getId_str(), createdTweet.getId_str());
        assertEquals(createdTweet.getCoordinates().getCoordinates().get(0),
                foundTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(createdTweet.getCoordinates().getCoordinates().get(1),
                foundTweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() throws Exception {
        // Create tweet
        String text = "Controller show tweet number 2";
        Tweet tweet = buildTweet(text);
        Tweet createdTweet = service.postTweet(tweet);
        String id = createdTweet.getId_str();
        String [] args = {"show", id, "text", "id_str", "coordinates"};
        // Find tweet
        Tweet foundTweet = controller.showTweet(args);
        assertEquals(createdTweet.getText(), foundTweet.getText());
        assertEquals(createdTweet.getId_str(), foundTweet.getId_str());
        assertNotNull("Value should NOT be null.", foundTweet.getCoordinates());
        assertNull("Value should be null.", foundTweet.getEntities());
    }

    @Test
    public void deleteTweets() throws Exception {
        String text = "Controller delete tweet number 2";
        Tweet tweet = buildTweet(text);
        Tweet createdTweet = service.postTweet(tweet);
        String id = createdTweet.getId_str();
        String [] args = {"delete", id};
        //Delete tweet
        List<Tweet> deletedTweets = controller.deleteTweet(args);
        assertEquals(1, deletedTweets.size());
        assertNotNull("Value should NOT be null.", deletedTweets.get(0));
    }
}
