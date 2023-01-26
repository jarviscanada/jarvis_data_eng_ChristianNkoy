package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Field;

public class TwitterService implements Service{

    private CrdDao dao;
    private static final int MAX_TWEET_LENGTH = 140;
    private static final int MAX_LONGITUDE = 180;
    private static final int MIN_LONGITUDE = -180;
    private static final int MAX_LATITUDE = 90;
    private static final int MIN_LATITUDE = -90;

    public TwitterService(CrdDao dao){this.dao = dao;}

    /**
     * Validate and post a user input Tweet
     *
     * @param tweet tweet to be created
     * @return created tweet
     * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long out of range
     */
    @Override
    public Tweet postTweet(Tweet tweet) throws IllegalArgumentException{
        validatePostTweet(tweet);
        return (Tweet) dao.create(tweet);
    }

    private void validatePostTweet(Tweet tweet) {
        if(tweet.getText().length() > MAX_TWEET_LENGTH ||
            tweet.getCoordinates().getCoordinates().get(0) < MIN_LONGITUDE ||
            tweet.getCoordinates().getCoordinates().get(0) > MAX_LONGITUDE ||
            tweet.getCoordinates().getCoordinates().get(1) < MIN_LATITUDE ||
            tweet.getCoordinates().getCoordinates().get(1) > MAX_LATITUDE) {
            throw new IllegalArgumentException("Invalid tweet input.");
        }
    }

    /**
     * Search a tweet by ID
     *
     * @param id     tweet id
     * @param fields set fields not in the list to null
     * @return Tweet object which is returned by the Twitter API
     * @throws IllegalArgumentException if id or fields param is invalid
     */
    @Override
    public Tweet showTweet(String id, String[] fields) throws IllegalArgumentException{
        validateTweetId(id);
        validateTweetFields(fields);
        Tweet tweet = (Tweet) dao.findById(id);
        handleFields(fields, tweet);
        return tweet;
    }

    /* Validate tweet fields array method */
    private void validateTweetFields(String[] fields) {
        if(fields == null){
            throw new IllegalArgumentException("Invalid fields input.");
        }
    }

    /* Validate tweet id */
    private void validateTweetId(String id) {
        if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("Invalid id input.");
        }
    }

    /***
     * Set fields not in the list of fields to null
     *
     * @param fields list of fields to NOT set to null (to include)
     * @param tweet target object
     */
    private void handleFields(String [] fields, Tweet tweet){
        Set<String> userFieldsList = new HashSet<>(Arrays.asList(fields));
        Field[] tweetFieldsList = tweet.getClass().getDeclaredFields();

        for (Field tweetField : tweetFieldsList) {
            if (userFieldsList.contains(tweetField.getName())) {
                continue;
            }
            if(!tweetField.getType().isPrimitive() && !tweetField.getName().equals("id_str")) {
                // Only set object fields, including strings, to null except for id_str
                tweetField.setAccessible(true);
                try {
                    tweetField.set(tweet, null);
                } catch (IllegalAccessException e) {
                    System.out.println("Denied access to tweet field.");
                }
            }
        }
    }

    /**
     * Delete Tweet(s) by id(s).
     *
     * @param ids tweet IDs which will be deleted
     * @return A list of Tweets
     * @throws IllegalArgumentException if one of the IDs is invalid.
     */
    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        List<Tweet> deletedTweets = new ArrayList<>();
        for(String id : ids){
           validateTweetId(id);
           deletedTweets.add((Tweet) dao.deleteById(id));
        }
        return deletedTweets;
    }
}
