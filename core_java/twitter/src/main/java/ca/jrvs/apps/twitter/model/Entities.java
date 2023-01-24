package ca.jrvs.apps.twitter.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entities {
    List<Hashtag> hashtags;
    List<UserMention> userMentions;

    public List<Hashtag> getHashtags() {
        return hashtags;
    }
    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }

}
