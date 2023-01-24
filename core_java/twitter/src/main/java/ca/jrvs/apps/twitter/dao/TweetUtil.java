package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetUtil {
    /***
     * Utility method for building a tweet object
     *
     * @param text tweet string
     * @param lon longitude
     * @param lat latitude
     * @return tweet object
     * @throws Exception
     */
    public static Tweet buildTweet (String text, Double lon, Double lat) throws Exception {
        Tweet tweet = new Tweet();
        List<Double> coordinates = new ArrayList<>();
        Hashtag hashtag = new Hashtag();
        UserMention userMention = new UserMention();
        List<Hashtag> hashtags = new ArrayList<>();
        List<UserMention> userMentions = new ArrayList<>();
        Coordinates coordinatesObject = new Coordinates();

        tweet.setText(text);
        coordinates.add(lon);
        coordinates.add(lat);
        coordinatesObject.setCoordinates(coordinates);
        coordinatesObject.setType("Point");
        tweet.setCoordinates(coordinatesObject);

        // Collect hashtags from the tweet text
        Matcher matcher = Pattern.compile("#\\w+").matcher(text);
        int[] indices;
        while (matcher.find()) {
            indices = new int[]{matcher.start(), matcher.end()};
            hashtag.setText(matcher.group());
            hashtag.setIndices(indices);
            hashtags.add(hashtag);
        }

        // Collect userMentions from the text
        matcher = Pattern.compile("@\\w+").matcher(text);
        while (matcher.find()){
            indices = new int[]{matcher.start(), matcher.end()};
            userMention.setName(matcher.group());
            userMention.setIndices(indices);
            userMentions.add(userMention);
        }

        Entities entities = new Entities();
        entities.setHashtags(hashtags);
        entities.setUserMentions(userMentions);

        tweet.setEntities(entities);

        return tweet;
    }

}
