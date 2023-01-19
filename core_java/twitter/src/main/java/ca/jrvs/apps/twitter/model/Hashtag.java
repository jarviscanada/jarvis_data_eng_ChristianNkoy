package ca.jrvs.apps.twitter.model;

public class Hashtag {
    private String text;
    private int [] indices;
    public Hashtag(String text, int [] indices){
        this.text = text;
        this.indices = indices;
    }


    public String getText() {
        return text;
    }

    public int[] getIndices() {
        return indices;
    }
}
