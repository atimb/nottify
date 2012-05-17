package com.nottify.utils;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.oauth2.MemoryCredentialStore;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * @author Attila Incze
 * OAuth2 constants, helpers
 */
public class OAuth2Helper {

	private static final String AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/auth";

	private static final String TOKEN_SERVER_URL = "https://accounts.google.com/o/oauth2/token";

	// This is what do we ask authorization for
	private static final String GDRIVE_SCOPE = "https://docs.google.com/feeds/";

	private static String CLIENT_ID;

	private static String CLIENT_SECRET;

	public static AuthorizationCodeFlow codeFlow;

	/**
	 * Setup called during startup
	 * @param client_id OAuth2
	 * @param client_secret OAuth2
	 */
	public static void configure(String client_id, String client_secret) {
		CLIENT_ID = client_id;
		CLIENT_SECRET = client_secret;

		// Prepare the authorization code flow object
		ClientParametersAuthentication auth = new ClientParametersAuthentication(CLIENT_ID, CLIENT_SECRET);
		AccessMethod method = BearerToken.authorizationHeaderAccessMethod();
		GenericUrl url = new GenericUrl(TOKEN_SERVER_URL);
		Builder builder = new AuthorizationCodeFlow.Builder(method, new NetHttpTransport(), new JacksonFactory(), url,
				auth, CLIENT_ID, AUTHORIZATION_SERVER_URL);
		builder.setCredentialStore(new MemoryCredentialStore());
		builder.setScopes(GDRIVE_SCOPE);
		codeFlow = builder.build();
	}
}
