package com.nottify.gdrive;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.google.api.client.auth.oauth2.Credential;
import com.nottify.model.ModifiedEntry;

/**
 * @author Attila Incze
 * Responsible to parse the Atom feed for a user returned by the Google Drive API
 */
public class FeedParser {

	private List<ModifiedEntry> modifiedEntries;

	private long largestChangeStamp;

	private String ownerEmail;

	// The associated user's OAuth2 credential
	private Credential credential;

	/**
	 * Constructor
	 * @param credential The OAuth2 credential
	 */
	public FeedParser(Credential credential) {
		this.credential = credential;
		initialParse();
	}

	/**
	 * First fetch of feed, to retrieve the changestamp
	 */
	public void initialParse() {
		try {
			// Fetch API
			String content = GDriveClient.fetchChangeFeed(credential.getAccessToken(), 0, null);

			// Parse changestamp
			Element root = DocumentHelper.parseText(content).getRootElement();
			ownerEmail = root.element("author").elementText("email");
			largestChangeStamp = Long.parseLong(root.element("largestChangestamp").attributeValue("value"));

			System.out.println("Intial parse done, email: " + getOwnerEmail() + ", changestamp: " + largestChangeStamp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves and parses all modifications made since the last changestamp
	 * @return List of modified entries, or null if authorization is revoked
	 */
	@SuppressWarnings("rawtypes")
	public List<ModifiedEntry> getModifiedEntries() {
		modifiedEntries = new LinkedList<ModifiedEntry>();

		try {
			// Fetch API, changes from changestamp+1
			String content = GDriveClient.fetchChangeFeed(credential.getAccessToken(), 1000, largestChangeStamp + 1);

			// Refreshing access token if necessary
			if (content.equals("401")) {
				boolean refreshed = credential.refreshToken();
				if (refreshed) {
					System.out.println("Access token refreshed");
					return modifiedEntries;
				} else {
					// Signal that could not renew token, remove this feed parser
					System.out.println("Parser removed from queue");
					return null;
				}
			}

			// Parse changestamp
			Element root = DocumentHelper.parseText(content).getRootElement();
			largestChangeStamp = Long.parseLong(root.element("largestChangestamp").attributeValue("value"));

			// Parse each entry one-by-one
			for (Iterator i = root.elementIterator("entry"); i.hasNext();) {
				Element element = (Element) i.next();

				String fileName = element.elementText("filename");
				if (fileName == null) {
					fileName = element.elementText("title");
				}
				String downloadUrl = element.element("content").attributeValue("src").replace("gd=true", "");
				boolean isDeleted = element.element("deleted") != null;
				boolean isDirectory = element.elementText("resourceId").startsWith("folder:");

				// Instantiate and add to list
				ModifiedEntry entry = new ModifiedEntry(fileName, downloadUrl, ownerEmail, isDirectory, isDeleted);
				modifiedEntries.add(entry);
			}

			System.out.println("Fetching modifications done, record number: " + modifiedEntries.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return modifiedEntries;
	}

	/**
	 * Returns the associated user's email address
	 * @return Email address
	 */
	public String getOwnerEmail() {
		return ownerEmail;
	}
}
