package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TweetUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

    @Mock
    private CrdDao dao;

    @InjectMocks
    private TwitterService service;

    private Tweet testTweet;
    private Tweet testTweet2;

    @Before
    public void setUp() {
        String text = "This is a tweet from Rio de Janeiro.";
        Double lon = -43.179872073443065;
        Double lat = -22.90346877288241;
        try {
            testTweet = TweetUtil.buildTweet(text, lon, lat);
        } catch (Exception e) {
            throw new RuntimeException("Could not build tweet.");
        }
        String text2 = "This is a tweet from Paris.";
        Double lon2 = 2.2690042584256296;
        Double lat2 = 48.841220322056635;
        try {
            testTweet2 = TweetUtil.buildTweet(text2, lon2, lat2);
        } catch (Exception e) {
            throw new RuntimeException("Could not build tweet.");
        }
    }

    @Test
    public void postTweet() throws Exception {
        when(dao.create(any())).thenReturn(testTweet);
        Tweet postedTweet = service.postTweet(testTweet);
        assertEquals(testTweet.getText(), postedTweet.getText());
    }

    @Test
    public void showTweet() throws Exception {
        when(dao.findById(any())).thenReturn(testTweet);
        String[] fields = {"text", "coordinates"};
        Tweet foundTweet = service.showTweet("1", fields);
        assertEquals(testTweet.getText(), foundTweet.getText());
        assertNotEquals(testTweet2.getText(), foundTweet.getText());
        assertNotNull( "Value should NOT be null.", foundTweet.getCoordinates().getCoordinates());
        assertNull("Value should be null.", foundTweet.getEntities());
    }

    @Test
    public void deleteTweets() throws Exception {
        when(dao.deleteById(any(String.class))).thenReturn(testTweet);
        String [] ids = {"1"};
        List<Tweet> deletedTweets = service.deleteTweets(ids);
        assertFalse(deletedTweets.isEmpty());
        assertEquals(testTweet.getText(), deletedTweets.get(0).getText());

    }
}
