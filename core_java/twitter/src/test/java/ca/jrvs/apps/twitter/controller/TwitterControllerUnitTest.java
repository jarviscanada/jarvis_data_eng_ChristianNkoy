package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.TweetUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

    @Mock
    private Service service;

    @InjectMocks
    private TwitterController controller;

    private Tweet testTweet;
    private Tweet testTweet2;

    @Before
    public void setUp() {
        String text = "This is a tweet from Cape Town.";
        Double lon = 18.421556980661816;
        Double lat = -33.92893433929781;
        try {
            testTweet = TweetUtil.buildTweet(text, lon, lat);
        } catch (Exception e) {
            throw new RuntimeException("Could not build tweet.");
        }

        String text2 = "This is a tweet from Tokyo.";
        Double lon2 = 139.75653106591554;
        Double lat2 = 35.70287309799651;
        try {
            testTweet2 = TweetUtil.buildTweet(text2, lon2, lat2);
        } catch (Exception e) {
            throw new RuntimeException("Could not build tweet.");
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void postTweet() throws Exception {
        String [] args = {"post", "This is a tweet from Cape Town.", "-33.92893433929781:18.421556980661816"};
        when(service.postTweet(any(Tweet.class))).thenReturn(testTweet);
        Tweet postedTweet = controller.postTweet(args);
        assertEquals(testTweet.getText(), postedTweet.getText());
        assertNotEquals(testTweet2.getText(), postedTweet.getText());
        // Trigger IllegalArgumentException
        String [] str = {"post text"};
        controller.postTweet(str);
    }

    @Test (expected = IllegalArgumentException.class)
    public void showTweet() throws Exception {
        when(service.showTweet(any(String.class), any(String[].class))).thenReturn(testTweet2);
        String[] fields = {"text", "coordinates"};
        Tweet foundTweet = service.showTweet("1", fields);
        assertEquals(testTweet2.getText(), foundTweet.getText());
        // Trigger IllegalArgumentException
        String [] str = {"show"};
        controller.postTweet(str);
    }

    @Test (expected = IllegalArgumentException.class)
    public void deleteTweets() throws Exception {
        List<Tweet> tweetList = Arrays.asList(testTweet, testTweet2);
        when(service.deleteTweets(any(String[].class))).thenReturn(tweetList);
        String [] ids = {"1", "2"};
        List<Tweet> deletedTweets = service.deleteTweets(ids);
        assertFalse(deletedTweets.isEmpty());
        assertEquals(testTweet.getText(), deletedTweets.get(0).getText());
        assertEquals(testTweet2.getText(), deletedTweets.get(1).getText());
        assertNotEquals(testTweet.getText(), deletedTweets.get(1).getText());
        // Trigger IllegalArgumentException
        String [] str = {"delete"};
        controller.postTweet(str);
    }
}
