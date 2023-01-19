package ca.jrvs.apps.twitter.model;

public class Tweet {
    private String created_at;
    private long id;
    private String id_str;
    private String text;
    private Entities entities;
    private Coordinates coordinates;
    private int retweet_count;
    private int favorite_count;
    private boolean favorited;
    private boolean retweeted;
}
