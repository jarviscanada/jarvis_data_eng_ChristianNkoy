package ca.jrvs.apps.twitter.model;

public class UserMention {
    private String name;
    private int[] indices;
    private String screenName;
    private long id;
    private String idStr;

    public UserMention(String name, int[] indices, String screenName, long id, String idStr) {
        this.name = name;
        this.indices = indices;
        this.screenName = screenName;
        this.id = id;
        this.idStr = idStr;
    }
}
