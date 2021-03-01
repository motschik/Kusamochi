package twitmochi;



import kusamochi.KataokaStation;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TweetTest {
 public static void main(String args,String token,String secret) throws TwitterException {
  Twitter twitter = new TwitterFactory().getInstance();
  AccessToken accessToken = new AccessToken(
   token,secret);
  twitter.setOAuthConsumer(KataokaStation.conkey,KataokaStation.consec);
  twitter.setOAuthAccessToken(accessToken);
  try {
   Status status = twitter.updateStatus(args);
   System.out.println("Ç¬Ç‘Ç‚Ç´ê¨å˜ [" + status.getText() + "].");
  } catch (TwitterException e1) {
	  System.out.println("Ç¶ÇÁÅ[" + e1.toString());
   //e1.printStackTrace();
   throw e1;
  }
 // System.exit(0);
 }
}
