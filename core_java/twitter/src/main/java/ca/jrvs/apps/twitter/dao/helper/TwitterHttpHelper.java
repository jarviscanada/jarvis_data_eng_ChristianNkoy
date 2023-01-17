package ca.jrvs.apps.twitter.dao.helper;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URI;

public class TwitterHttpHelper implements HttpHelper{
    /**
     * Dependencies are specified as private member variables
     */
    private OAuthConsumer consumer;
    private HttpClient httpClient;

    /**
     * Constructor
     * Setup dependencies using secrets
     *
     * @param consumerKey
     * @param consumerSecret
     * @param accessToken
     * @param tokenSecret
     */
    public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken, String tokenSecret){
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);
        /**
         * Default = single connection. Discuss source code if time permits
         */
        httpClient = new DefaultHttpClient();
    }

    /**
     * @param uri 
     * @return response to http POST request
     */
    @Override
    public HttpResponse httpPost(URI uri) {
        try{
            return executeHttpRequest(HttpMethod.POST, uri, null);
        } catch (OAuthException | IOException e){
            throw new RuntimeException("Failed to execute", e);
        }
    }

    /**
     * @param uri 
     * @return
     */
    @Override
    public HttpResponse httpGet(URI uri) {
        try{
            return executeHttpRequest(HttpMethod.GET, uri, null);
        } catch (OAuthException | IOException e){
            throw new RuntimeException("Failed to execute", e);
        }
    }

    private HttpResponse executeHttpRequest(HttpMethod method, URI uri, StringEntity stringEntity) throws OAuthException, IOException {
        if(method == HttpMethod.GET){
            HttpGet request = new HttpGet(uri);
            consumer.sign(request);
            return httpClient.execute(request);
        } else if(method == HttpMethod.POST){
            HttpPost request = new HttpPost(uri);
            if(stringEntity != null){
                request.setEntity(stringEntity);
            }
            consumer.sign(request);
            return httpClient.execute(request);
        } else{
            throw new IllegalArgumentException("Unknown HTTP method: " + method.name());
        }
    }

    public static void main(String[] args) throws Exception{
        String consumerKey = System.getenv("CONSUMER_KEY");
        String consumerSecret = System.getenv("CONSUMER_SECRET");
        String accessToken = System.getenv("ACCESS_TOKEN");
        String tokenSecret = System.getenv("TOKEN_SECRET");
        System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

        // Create components
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        HttpResponse response = httpHelper
                .httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=first tweet"));
        System.out.println(EntityUtils.toString(response.getEntity()));

    }
}
