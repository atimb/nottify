package com.nottify.model;

/**
 * @author Attila Incze
 * Represents a modified (added/updated/deleted) entry (document/folder)
 */
public class ModifiedEntry {

	private String fileName;

	private String downloadUrl;

	private String ownerEmail;

	private boolean isDirectory;

	private boolean isDeleted;

	/**
	 * Constructor
	 * @param fileName
	 * @param downloadUrl
	 * @param ownerEmail
	 * @param isDirectory
	 * @param isDeleted
	 */
	public ModifiedEntry(String fileName, String downloadUrl, String ownerEmail, boolean isDirectory, boolean isDeleted) {
		this.fileName = fileName;
		this.downloadUrl = downloadUrl;
		this.isDirectory = isDirectory;
		this.isDeleted = isDeleted;
		this.ownerEmail = ownerEmail;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public String getFileName() {
		return fileName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public String toString() {
		return "Filename: " + fileName + " downloadUrl: " + downloadUrl + " isDirectory:" + isDirectory + " isDeleted:"
				+ isDeleted;
	}
}
