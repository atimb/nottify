package com.nottify.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;
import com.nottify.gdrive.FeedParser;
import com.nottify.notify.PollManager;
import com.nottify.utils.OAuth2Helper;

/**
 * @author Attila Incze
 * OAuth2 authorization, second (callback with code) phase handler
 */
public class AuthorizationCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
			throws ServletException, IOException {
		// After successful authorization, create feed parser instance for user, and add it to poll manager
		FeedParser feedParser = new FeedParser(credential);
		PollManager.getInstance().add(req.getSession().getId(), feedParser);
		// Store it in session, may be useful
		req.getSession().setAttribute("credential", credential);
		req.getSession().setAttribute("email", feedParser.getOwnerEmail());
		// Redirect back to home page
		resp.sendRedirect("/");
	}

	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
			throws ServletException, IOException {
		resp.sendRedirect("/?denied");
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		url.setRawPath("/oauth2callback");
		return url.build();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		return OAuth2Helper.codeFlow;
	}

	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		return (String) req.getSession().getId();
	}
}
