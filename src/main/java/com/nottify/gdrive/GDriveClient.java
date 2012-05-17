package com.nottify.gdrive;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author Attila Incze
 * Client implementation for Google Drive Change Feed API
 */
public class GDriveClient {

	private static final String DRIVE_API_VERSION = "3.0";

	private static final String DRIVE_FEED_URL = "https://docs.google.com/feeds/default/private/changes";

	private static final HttpClient client = new HttpClient();

	/**
	 * Fetches the document/folder change feed
	 * @param access_token OAuth2 token
	 * @param maxResults Maximum number of returned entries (max 1000)
	 * @param startChangestamp Changestamp to start from
	 * @return The fetched Atom XML as a string, or the status code, if not 200
	 * @throws IOException
	 */
	public static String fetchChangeFeed(String access_token, int maxResults, Long startChangestamp) throws IOException {

		// Make an authenticated request
		String params = "?max-results=" + maxResults;
		if (startChangestamp != null) {
			params += "&start-index=" + startChangestamp;
		}

		GetMethod get = new GetMethod(DRIVE_FEED_URL + params);

		// Necessary headers
		get.addRequestHeader("GData-Version", DRIVE_API_VERSION);
		get.addRequestHeader("Authorization", "Bearer " + access_token);

		client.executeMethod(get);

		int statusCode = get.getStatusCode();
		System.out.println("Google Drive feed request response code: " + statusCode);

		String content;
		if (statusCode == 200) {
			content = get.getResponseBodyAsString();
		} else {
			content = Integer.toString(statusCode);
		}
		get.releaseConnection();

		return content;
	}

}
