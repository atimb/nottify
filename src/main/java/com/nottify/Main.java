package com.nottify;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.nottify.notify.NotificationSender;
import com.nottify.notify.PollManager;
import com.nottify.utils.OAuth2Helper;

/**
 * 
 * This class launches the web application in an embedded Jetty container.
 * This is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		loadConfig();

		String webappDirLocation = "src/main/webapp/";

		//The port that we should run on can be set into an environment variable
		//Look for that variable and default to 8080 if it isn't there.
		String webPort = System.getenv("PORT");
		if (webPort == null || webPort.isEmpty()) {
			webPort = "8080";
		}

		Server server = new Server(Integer.valueOf(webPort));
		WebAppContext root = new WebAppContext();

		root.setContextPath("/");
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		//Parent loader priority is a class loader setting that Jetty accepts.
		//By default Jetty will behave like most web containers in that it will
		//allow your application to replace non-server libraries that are part of the
		//container. Setting parent loader priority to true changes this behavior.
		//Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
		root.setParentLoaderPriority(true);

		server.setHandler(root);

		server.start();
		server.join();
	}

	/**
	 * Startup configuration from property file
	 */
	private static void loadConfig() {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("config.properties"));

			PollManager.configure(prop.getProperty("poll_period"));

			OAuth2Helper.configure(prop.getProperty("oauth2.client_id"), prop.getProperty("oauth2.client_secret"));

			NotificationSender.configure(prop.getProperty("pusher.app_id"), prop.getProperty("pusher.key"),
					prop.getProperty("pusher.secret"), prop.getProperty("pusher.channel"),
					prop.getProperty("requestbin.id"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
