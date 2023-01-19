package ca.jrvs.apps.twitter.model;
import java.util.List;

public class Entities {
    List<Hashtag> hashtags;
    List<UserMention> userMentions;
    public Entities(List<Hashtag> hashtags, List<UserMention> userMentions){
        this.hashtags = hashtags;
        this.userMentions = userMentions;
    }
}
