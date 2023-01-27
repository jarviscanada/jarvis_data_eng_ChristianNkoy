package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.TweetUtil;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

import static ca.jrvs.apps.twitter.dao.TweetUtil.buildTweet;

public class TwitterController implements Controller{
    private static final String COORD_SEP = ":";
    private static final String COMMA = ",";

    private Service service;

    /***
     * Constructor that initializes service object
     *
     * @param service instance of class Service
     */
    public TwitterController (Service service) {
        this.service = service;
    }

    /**
     * Parse user argument and post a tweet by calling service classes
     *
     * @param args
     * @return a posted tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet postTweet(String[] args) {
        if(args.length != 3) {
            throw new IllegalArgumentException(
                    "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }

        String tweet_text = args[1];
        String coordinates = args[2];
        String [] coordArray = coordinates.split(COORD_SEP);

        if((coordArray.length != 2) || StringUtils.isBlank(tweet_text)) {
            throw new IllegalArgumentException (
                    "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        Double lat = null;
        Double lon = null;
        try{
            lat = Double.parseDouble(coordArray[0]);
            lon = Double.parseDouble(coordArray[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid location format\nUSAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"", e);
        }

        try {
            Tweet tweet = buildTweet(tweet_text, lon, lat);
            return service.postTweet(tweet);
        } catch (Exception e) {
            throw new RuntimeException("Could not build tweet.", e);
        }

    }

    /**
     * Parse user argument and search a tweet by calling service classes
     *
     * @param args
     * @return a tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet showTweet(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "USAGE: TwitterCLIApp show \"tweet_id\" [fields]");
        }
        String id = args[1];
        String [] fields = null;
        List<String> fieldList = new ArrayList<>();
        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                fieldList.add(args[i].toLowerCase());
            }
            fields = fieldList.toArray(new String[0]);
        }
        return service.showTweet(id, fields);
    }

    /**
     * Parse user argument and delete tweets by calling service classes
     *
     * @param args
     * @return a list of deleted tweets
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "USAGE: TwitterCLIApp delete \"tweet_id\"");
        } else {
            String [] tweetIds = null;
            List<String> idList = new ArrayList<>();
            for (int i = 1; i < args.length; i++){
                idList.add(args[i]);
            }
            tweetIds = idList.toArray(new String[0]);
            return service.deleteTweets(tweetIds);
        }
    }
}
