package com.nottify.notify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nottify.gdrive.FeedParser;
import com.nottify.model.ModifiedEntry;

/**
 * @author Attila Incze
 * This Runnable class is responsible for periodically calling all the feed parsers
 * and signaling to HTTP notifiers
 */
public class PollManager implements Runnable {

	// How often (ms) to poll for changes
	private static Long POLL_PERIOD;

	private static PollManager instance;

	private Map<String, FeedParser> feedParsers = new HashMap<String, FeedParser>();

	PollManager() {
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * A user authorized, let's add it to the collection
	 * @param userId The sessionID of user
	 * @param feedParser The user's feed parser instance
	 */
	public void add(String userId, FeedParser feedParser) {
		feedParsers.put(userId, feedParser);
	}

	/**
	 * This thread keeps polling the GDrive API
	 */
	public void run() {
		try {
			while (true) {
				// Wait configured time
				Thread.sleep(POLL_PERIOD);
				// Poll all authorized sessions
				for (Map.Entry<String, FeedParser> entry : feedParsers.entrySet()) {
					// Retrieve modified entries for a user
					List<ModifiedEntry> entries = entry.getValue().getModifiedEntries();
					// If authorization is revoked, don't keep trying it 
					if (entries == null) {
						feedParsers.remove(entry.getKey());
						break;
					} else if (entries.size() > 0) {
						// Otherwise notify via HTTP
						String targetUser = entry.getKey();
						NotificationSender.notify(targetUser, entries);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Singleton pattern
	 * @return The singleton instance
	 */
	public static PollManager getInstance() {
		if (instance == null) {
			instance = new PollManager();
		}
		return instance;
	}

	/**
	 * Setup
	 * @param poll_period In milliseconds the poll time
	 */
	public static void configure(String poll_period) {
		POLL_PERIOD = Long.parseLong(poll_period);
	}
}
