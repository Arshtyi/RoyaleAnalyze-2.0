package org.arshtyi.royaleanalyze2.royaleanalyze2.extern;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This class contains the URLs and headers used for making API requests to the
 * Clash Royale API.
 */
public class Urls {
    /**
     * This is the base URL for the Clash Royale API.
     */
    private static final String BASE_URL = "https://api.clashroyale.com/v1";
    /**
     * This is the player URL for the Clash Royale API.
     */
    private static final String PLAYER_URL = getFullUrl(BASE_URL, "/players");
    /**
     * This is the clan URL for the Clash Royale API.
     */
    private static final String CLAN_URL = getFullUrl(BASE_URL, "/clans");
    /**
     * This is a token character equivalent to the "#" character in URLs.
     */
    private static final String TAG_TOKEN = "/%23";
    /**
     * This is the header for the user agent used in API requests.
     */
    private static final String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36 Edg/133.0.0.0";

    /**
     * This method constructs the full URL for a given endpoint by appending it to
     * the base URL.
     * 
     * @param baseUrl
     *            The base URL to append the endpoint to.
     * @param endpoint
     *            The endpoint to append to the base URL.
     * @return The full URL as a string.
     * @throws IllegalArgumentException
     *             if the base URL is null or empty.
     *             if the endpoint is null or empty.
     */
    public static String getFullUrl(String baseUrl, String endpoint) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty");
        }
        return baseUrl + endpoint;
    }

    /**
     * This method constructs the URL for clan information by appending the clan tag
     * to the clan URL.
     * 
     * @param clanTag
     *            The tag of the clan to get information for.
     * @return The full URL for the clan information as a string.
     * @throws IllegalArgumentException
     *             if the clan tag is null or empty.
     */
    public static String getClanInformationUrl(String clanTag) {
        if (clanTag == null || clanTag.isEmpty()) {
            throw new IllegalArgumentException("Clan tag cannot be null or empty");
        }
        return CLAN_URL + TAG_TOKEN + clanTag;
    }

    /**
     * This method constructs the URL for player information by appending the
     * player tag to the player URL.
     * 
     * @param playerTag
     *            The tag of the player to get information for.
     * @return The full URL for the player information as a string.
     * @throws IllegalArgumentException
     *             if the player tag is null or empty.
     */
    public static String getPlayerInformationUrl(String playerTag) {
        if (playerTag == null || playerTag.isEmpty()) {
            throw new IllegalArgumentException("Player tag cannot be null or empty");
        }
        return PLAYER_URL + TAG_TOKEN + playerTag;
    }

    /**
     * This method creates an HTTP GET request with the necessary headers for making
     * API requests.
     * It sets the user agent, authorization token, and other headers required by
     * the API.
     * 
     * @param url
     *            The URL for the GET request.
     * @return An HttpGet object with the specified URL and headers.
     * @throws IllegalArgumentException
     *             if the URL is null or empty.
     */
    public static HttpGet createHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        // Basic headers for the GET request
        httpGet.addHeader("user-agent", Urls.AGENT);
        String token = Externs.getFixedApiKey();
        httpGet.addHeader("Authorization", token);
        httpGet.addHeader("accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/**;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpGet.addHeader("cache_expires", "20");
        httpGet.addHeader("Access-Control-Allow-Origin", "no-cors");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    /**
     * This method creates an HTTP client with custom configurations.
     * 
     * @param void
     * @return A CloseableHttpClient with the specified configurations.
     */
    public static CloseableHttpClient createHttpClient() {
        return HttpClients.custom()
                .setSSLHostnameVerifier(new DefaultHostnameVerifier())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(5000)
                        .build())
                .build();
    }
}
