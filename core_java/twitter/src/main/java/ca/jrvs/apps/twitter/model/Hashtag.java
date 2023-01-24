package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hashtag {
    private String text;
    private int [] indices;

    public String getText() {
        return text;
    }
    public int[] getIndices() {
        return indices;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

}
