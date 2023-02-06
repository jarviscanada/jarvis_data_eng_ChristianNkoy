package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.JsonUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class TwitterDaoIntTest {

    private TwitterDao dao;

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
    }

    @Test
    public void create() throws Exception {
        String text = "Dao create tweet number 2";
        Double lat = -33d;
        Double lon = 83d;
        Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
        System.out.println(JsonUtil.toPrettyJson(postTweet));

        Tweet tweet = dao.create(postTweet);

        if(tweet != null){
            System.out.println(JsonUtil.toPrettyJson(tweet));;
        }

        assertEquals(text, tweet.getText());

        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void findById() throws Exception {
        String text = "Dao find tweet number 2";
        Double lat = 55d;
        Double lon = -45d;
        Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
        Tweet createdTweet = dao.create(postTweet);
        System.out.println(JsonUtil.toPrettyJson(createdTweet));

        String id = createdTweet.getId_str();

        Tweet foundTweet = dao.findById(id);

        assertEquals(createdTweet.getId_str(), foundTweet.getId_str());
        assertEquals(createdTweet.getCreated_at(), foundTweet.getCreated_at());
        assertEquals(createdTweet.getText(), foundTweet.getText());
        assertEquals(createdTweet.getCoordinates().getCoordinates().get(0),
                        foundTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(createdTweet.getCoordinates().getCoordinates().get(1),
                        foundTweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void deleteById() throws Exception {
        String text = "Dao delete tweet number 2";
        Double lat = 10d;
        Double lon = -8d;
        Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
        System.out.println(JsonUtil.toPrettyJson(postTweet));
        Tweet tweet = dao.create(postTweet);

        assertNotNull(tweet);
        assertNotNull(tweet.getId_str());

        Tweet deletedTweet = dao.deleteById(tweet.getId_str());
        assertNotNull(deletedTweet);
        assertNotNull(deletedTweet.getId_str());
        assertEquals(tweet.getId_str(), deletedTweet.getId_str());

        try {
            dao.findById(tweet.getId_str());
            fail();
        } catch (RuntimeException e) {
            System.out.println("Tweet does not exist");
        }
    }

}