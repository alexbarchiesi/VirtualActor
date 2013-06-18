package sourceutils;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.*;

/**
 * class used to access twitter for initial tests
 *
 */
public class TwitterRetriever {
	public static TwitterFactory tFactory;

	public static void configure() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("dy3Z94eJIsr0OcQ9G1BtjA");
		cb.setOAuthConsumerSecret("VCbzCa3PnaKjgqFSc1QjBNc9aihWzr50EmwYq0Vv5Cw");
		cb.setOAuthAccessToken("1327751431-sBCPz18R2MLvzQphFgHkBDnKfezZ3OxG18Ca7vv");
		cb.setOAuthAccessTokenSecret("Quo6D14qtNQdwoSZXbee1ScUlIndjTLHcCv7N1ZVuhg");

		tFactory = new TwitterFactory(cb.build());
	}

	public static String getFirstTweet(String keyword) {
		String tweet = "nothing found";

		System.out.println("Reading first tweet about \""+ keyword + "\":");

		List<Status> tweets = getTweetList(keyword);

		if(tweets.isEmpty()) {
			return tweet;
		}

		tweet = tweets.get(0).getText();
		System.out.println(tweet);

		return tweet;
	}

	public static List<Status> getTweetList(String keyword) {
		List<Status> tweets = null;
		Twitter twitter = getTwitter();

		try {
			Query query = new Query(keyword);
			QueryResult result = twitter.search(query);
			tweets = result.getTweets();
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}

		return tweets;        
	}

	private static Twitter getTwitter() {
		if(tFactory == null)
			configure();

		return tFactory.getInstance();
	}
}
