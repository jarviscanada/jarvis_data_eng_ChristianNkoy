package ca.jrvs.apps.twitter.dao;


import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jcp.xml.dsig.internal.dom.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;



public class TwitterDao implements CrdDao<Tweet, String> {
    //URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";

    //URI symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    //Response code
    private static final int HTTP_OK = 200;

    private HttpHelper httpHelper;

    /***
     * Constructor
     * @param httpHelper
     */
    public TwitterDao(HttpHelper httpHelper){this.httpHelper = httpHelper;}

    /***
     *
     * @param tweet to be created
     * @return
     */
    @Override
    public Tweet create(Tweet tweet) {
        // Construct URI
        URI uri;
        try{
            uri = getPostUri(tweet);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Invalid tweet input", e);
        }

        //Execute HTTP Request
        HttpResponse response = httpHelper.httpPost(uri);

        //Validate response and deser response to Tweet object
        return parseResponseBody(response, HTTP_OK);
    }


    @Override
    public Tweet findById(String id) {
        URI uri;
    try {
        uri = getShowUri(id);
    } catch (URISyntaxException e) {
        throw new IllegalArgumentException("Invalid id input.", e);
    }

    HttpResponse response = httpHelper.httpGet(uri);
    return parseResponseBody(response, HTTP_OK);

    }

    @Override
    public Tweet deleteById(String id) {
        URI uri;
    try {
        uri = getDeleteUri(id);
    } catch (URISyntaxException e) {
        throw new IllegalArgumentException("Invalid id input", e);
    }
    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
    }

    /***
     * Create URI for posting tweet
     *
     * @param tweet
     * @return URI object
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     */
    private URI getPostUri(Tweet tweet) throws URISyntaxException, UnsupportedEncodingException {
        String status = tweet.getText();
        String coordinates = "long=" + tweet.getCoordinates().getCoordinates().get(0) + AMPERSAND +
                                "lat=" + tweet.getCoordinates().getCoordinates().get(1);
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        return new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + percentEscaper.escape(status) +
                            AMPERSAND + coordinates);
    }

    /***
     * Create URI for showing tweet
     *
     * @param id tweet id string
     * @return URI for showing tweet
     * @throws URISyntaxException
     */
    private URI getShowUri(String id) throws URISyntaxException {
        return new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + id);
    }

    /***
     * Create URI for deleting tweet
     *
     * @param id tweet id
     * @return URI for deleting tweet
     * @throws URISyntaxException
     */
    private URI getDeleteUri(String id) throws URISyntaxException {
        String slashIdDotJson = "/" + id + ".json";
        return new URI(API_BASE_URI + DELETE_PATH + slashIdDotJson);
    }

    /***
     * Check response status code
     * Convert response entity to tweet
     *
     * @param response
     * @param expectedStatusCode
     * @return Tweet object
     */
    Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
        Tweet tweet = null;

        //Check response status
        int status = response.getStatusLine().getStatusCode();
        if (status != expectedStatusCode) {
            try{
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                System.out.println("Response has no entity");
            }
            throw new RuntimeException("Expected HTTP status: " + status);
        }

        if (response.getEntity() == null){
            throw new RuntimeException("Empty response body");
        }

        //Convert response entity to str
        String jsonStr;
        try{
            jsonStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert entity to string", e);
        }

        //Deser JSON string to Tweet object
        try{
            tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert JSON str to Object", e);
        }

        return tweet;
    }
}
