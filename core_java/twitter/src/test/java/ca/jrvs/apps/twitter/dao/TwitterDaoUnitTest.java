package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

    @Mock
    HttpHelper mockHelper;

    @InjectMocks
    TwitterDao dao;
    @Test
    public void showTweet() throws Exception {
        // Test failed request
        String hashTag = "#abc";
        String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        String id = "1097607853932564480";
        // Exception is expected here
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try{
            dao.findById(id);
            fail();
        } catch(RuntimeException e){
            assertTrue(true);
        }

        // Test happy path
        String tweetJsonStr = "{\n"
                + "     \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "     \"id\":1097607853932564480,\n"
                + "     \"id_str\":\"1097607853932564480\",\n"
                + "     \"text\":\"found tweet\",\n"
                + "     \"entities\":{\n"
                + "         \"hashtags\":[],\n"
                + "         \"user_mentions\":[]\n"
                + "     },\n"
                + "     \"coordinates\":null,\n"
                + "     \"retweet_count\":0,\n"
                + "     \"favorite_count\":0,\n"
                + "     \"favorited\":false,\n"
                + "     \"retweeted\":false\n"
                + "}";
        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
        // Mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.findById(id);
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
        assertEquals("found tweet", tweet.getText());
    }

    @Test
    public void postTweet() throws Exception {
        // Test failed request
        String hashTag = "#abc";
        String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        // Exception is expected here
        when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
        try{
            dao.create(TweetUtil.buildTweet(text, lon, lat));
            fail();
        } catch(RuntimeException e){
            assertTrue(true);
        }

        // Test happy path
        String tweetJsonStr = "{\n"
                + "     \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "     \"id\":1097607853932564480,\n"
                + "     \"id_str\":\"1097607853932564480\",\n"
                + "     \"text\":\"test tweet\",\n"
                + "     \"entities\":{\n"
                + "         \"hashtags\":[],\n"
                + "         \"user_mentions\":[]\n"
                + "     },\n"
                + "     \"coordinates\":{\n"
                + "         \"type\":\"Point\",\n"
                + "         \"coordinates\":[1.0, -1.0]\n"
                + "     },\n"
                + "     \"retweet_count\":0,\n"
                + "     \"favorite_count\":0,\n"
                + "     \"favorited\":false,\n"
                + "     \"retweeted\":false\n"
                + "}";
        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
        // Mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.create(TweetUtil.buildTweet(text, lon, lat));
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void deleteTweet() throws Exception {
        // Test failed request
        // Test failed request
        String hashTag = "#abc";
        String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        String id = "1097607853932564480";
        // Exception is expected here
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try{
            dao.findById(id);
            fail();
        } catch(RuntimeException e){
            assertTrue(true);
        }

        // Test happy path
        String tweetJsonStr = "{\n"
                + "     \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "     \"id\":1097607853932564480,\n"
                + "     \"id_str\":\"1097607853932564480\",\n"
                + "     \"text\":\"deleted tweet\",\n"
                + "     \"entities\":{\n"
                + "         \"hashtags\":[],\n"
                + "         \"user_mentions\":[]\n"
                + "     },\n"
                + "     \"coordinates\":null,\n"
                + "     \"retweet_count\":0,\n"
                + "     \"favorite_count\":0,\n"
                + "     \"favorited\":false,\n"
                + "     \"retweeted\":false\n"
                + "}";
        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
        // Mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.deleteById(id);
        assertNotNull(tweet);
        try {
            dao.findById(expectedTweet.getId_str());
        } catch (RuntimeException e) {
            System.out.println("Tweet does not exist");
        }
    }




}