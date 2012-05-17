package com.nottify.notify;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.map.ObjectMapper;

import com.nottify.model.ModifiedEntry;
import com.nottify.utils.CryptoUtils;

/**
 * @author Attila Incze
 * Responsible to send out HTTP notifications for the modified entries
 */
public class NotificationSender {

	public static String PUSHER_KEY;

	public static String PUSHER_CHANNEL;

	private static String PUSHER_APP_ID;

	private static String PUSHER_SECRET;

	private static String REQUESTBIN_ID;

	private static final HttpClient client = new HttpClient();

	/**
	 * HTTP notification to RequestBin and Pusher
	 * @param targetUser SessionID, who to target the push notification
	 * @param entries Modified entries
	 */
	public static void notify(String targetUser, List<ModifiedEntry> entries) {
		String body = createJsonMessage(entries);

		// Push to RequestBin
		doPost("http://requestb.in/" + REQUESTBIN_ID, body);

		// Push to Pusher
		doPost(signPusherRequest(targetUser, body), body);
	}

	/**
	 * Maps the modified entries to JSON representation
	 * @param entries Modified entries
	 * @return The JSON array
	 */
	private static String createJsonMessage(List<ModifiedEntry> entries) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(entries);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Perform an HTTP post request
	 * @param url The URL to send
	 * @param body The body to send
	 */
	private static void doPost(String url, String body) {
		try {
			PostMethod post = new PostMethod(url);
			post.setRequestEntity(new StringRequestEntity(body, "application/json", "UTF-8"));

			client.executeMethod(post);

			int statusCode = post.getStatusCode();
			System.out.println("Notification request response code: " + statusCode);

			post.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prepare a Pusher notification and sign it
	 * @param targetUser Who we send the notification
	 * @param body The message body to be sent
	 * @return The prepared and signed URL
	 */
	private static String signPusherRequest(String targetUser, String body) {
		String md5 = DigestUtils.md5Hex(body);
		String timestamp = Long.toString(new Date().getTime() / 1000);

		String domain = "http://api.pusherapp.com";
		String path = "/apps/" + PUSHER_APP_ID + "/channels/" + PUSHER_CHANNEL + "/events";
		String query = "auth_key=" + PUSHER_KEY + "&auth_timestamp=" + timestamp + "&auth_version=1.0" + "&body_md5="
				+ md5 + "&name=" + targetUser;

		String request = "POST\n" + path + "\n" + query;
		query += "&auth_signature=" + CryptoUtils.signRequest(request, PUSHER_SECRET);

		return domain + path + "?" + query;
	}

	/**
	 * Configure the credentials
	 * @param app_id Pusher
	 * @param key Pusher
	 * @param secret Pusher
	 * @param channel Pusher
	 * @param requestbin RequestBin ID
	 */
	public static void configure(String app_id, String key, String secret, String channel, String requestbin) {
		PUSHER_APP_ID = app_id;
		PUSHER_KEY = key;
		PUSHER_SECRET = secret;
		PUSHER_CHANNEL = channel;
		REQUESTBIN_ID = requestbin;
	}
}
