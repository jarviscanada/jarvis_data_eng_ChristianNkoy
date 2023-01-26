package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TweetUtil;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Before;
import org.junit.Test;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterServiceIntTest {

    private TwitterService service;
    private TwitterDao dao;

    private Tweet buildTweet() throws Exception {
        //build tweet
        String hashtag = "#abc";
        String text = "@someone Test tweet from Marrakesh" + hashtag + System.currentTimeMillis();
        Double lon = -7.988225530198496;
        Double lat = 31.62604888712559;
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
    }

    @Test
    public void postTweet() throws Exception {
        Tweet tweet = buildTweet();
        Tweet createdTweet = service.postTweet(tweet);

        //check if tweet is created
        Tweet foundTweet = dao.findById(createdTweet.getId_str());
        assertEquals(foundTweet.getText(), createdTweet.getText());
        assertEquals(createdTweet.getCoordinates().getCoordinates().get(0),
                foundTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(createdTweet.getCoordinates().getCoordinates().get(1),
                foundTweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() throws Exception {
        Tweet tweet = buildTweet();
        Tweet createdTweet = dao.create(tweet);

        String[] fields = {"text", "id", "coordinates"};
        Tweet showTweet = service.showTweet(createdTweet.getId_str(), fields);
        assertEquals(createdTweet.getText(), showTweet.getText());
        assertEquals(createdTweet.getId_str(), showTweet.getId_str());
        assertNotNull("Value should NOT be null.", showTweet.getCoordinates());
        assertNull("Value should be null.", showTweet.getEntities());
    }

    @Test
    public void deleteTweets() throws Exception {
        Tweet tweet = buildTweet();
        Tweet createdTweet = dao.create(tweet);
        List<Tweet> deletedTweets = service.deleteTweets(new String[] {createdTweet.getId_str()});

        // check if tweet is deleted
        try {
            dao.findById(createdTweet.getId_str());
            fail();
        } catch (RuntimeException e) {
            System.out.println("Tweet does not exist.");
        }

        assertNotNull("Value should NOT be null.", deletedTweets.get(0));
    }
}
