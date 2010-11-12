package com.hsiconsultoria.business;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.business.impl.TwitterFacade;

/**
 * @author Rodrigo Hjort
 */
public class TwitterFacadeTest {

	private static ITwitterFacade facade;
	
	private static Logger log = Logger.getLogger(TwitterFacadeTest.class);
	
	private static Random random = new Random();
	
	private static final String USER_LOGIN = "iara" + random.nextInt(100);
	private static final String USER_NAME = "Iara";
	private static final String USER_PASS = "pass";

	private static final String FOLLOWED_LOGIN = "boto" + random.nextInt(100);
	private static final String FOLLOWED_NAME = "Boto";

	private static Long lastTweetId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		facade = new TwitterFacade();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		facade = null;
	}

	@Test
	public void testCreateUser() {
		
		log.debug("Creating user: " + USER_LOGIN);
		
		User user =	facade.createUser(USER_LOGIN, USER_NAME, USER_PASS);
		Assert.assertNotNull(user);
		
		log.debug("Created user: " + user);
	}

	@Test
	public void testFindUserByName() {
		
		log.debug("Finding user: " + USER_LOGIN);
		
		User user = facade.findUserByLogin(USER_LOGIN);
		Assert.assertNotNull(user);
		Assert.assertEquals(USER_NAME, user.getName());
		Assert.assertEquals(USER_PASS, user.getPassword());
		
		log.debug("Found user: " + user);
	}

	@Test
	public void testFollowUser() {
		
		facade.createUser(FOLLOWED_LOGIN, FOLLOWED_NAME, null);
		Assert.assertTrue(true);
		
		log.debug("User " + USER_LOGIN + " following " + FOLLOWED_LOGIN);
		Followship followship = facade.followUser(USER_LOGIN, FOLLOWED_LOGIN);
		Assert.assertNotNull(followship);
		log.debug("Created followship: " + followship);
		
		log.debug("Finding users " + USER_LOGIN + " is following");
		List<User> followings = facade.findUserFollowings(USER_LOGIN);
		Assert.assertNotNull(followings);
		Assert.assertFalse(followings.isEmpty());
		Assert.assertEquals(1, followings.size());
		Assert.assertEquals(FOLLOWED_LOGIN, followings.get(0).getLogin());
		Assert.assertEquals(FOLLOWED_NAME, followings.get(0).getName());
		log.debug("Following users: " + followings);
		
		log.debug("Finding users that follow " + FOLLOWED_LOGIN);
		List<User> followers = facade.findUserFollowers(FOLLOWED_LOGIN);
		Assert.assertNotNull(followers);
		Assert.assertFalse(followers.isEmpty());
		Assert.assertEquals(1, followers.size());
		Assert.assertEquals(USER_LOGIN, followers.get(0).getLogin());
		Assert.assertEquals(USER_NAME, followers.get(0).getName());
		log.debug("Follower users: " + followers);
		
		facade.followUser(FOLLOWED_LOGIN, USER_LOGIN);
		Assert.assertTrue(true);
	}

	@Test
	public void testPostTweet() {
		
		final String SAMPLE_TEXT = "This is a simple tweet!";
		
		log.debug("User " + USER_LOGIN + " is tweeting");
		Tweet tweet = facade.postTweet(USER_LOGIN, SAMPLE_TEXT);
		Assert.assertNotNull(tweet);
		lastTweetId = tweet.getId();
		log.debug("Tweet posted: " + tweet);

		log.debug("Finding tweet: " + lastTweetId);
		Tweet retrieved = facade.findTweet(lastTweetId);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(lastTweetId, retrieved.getId());
		log.debug("Tweet found: " + retrieved);
		
		log.debug("Finding last tweets from " + USER_LOGIN);
		List<Tweet> tweets = facade.findUserLastTweets(USER_LOGIN);
		Assert.assertNotNull(tweets);
		Assert.assertFalse(tweets.isEmpty());
		Assert.assertEquals(1, tweets.size());
		Assert.assertEquals(SAMPLE_TEXT, tweets.get(0).getText());
		log.debug("Tweets found: " + tweets);

		log.debug("Finding last tweets from " + FOLLOWED_LOGIN);
		tweets = facade.findUserLastTweets(FOLLOWED_LOGIN);
		Assert.assertNotNull(tweets);
		Assert.assertFalse(tweets.isEmpty());
		Assert.assertEquals(1, tweets.size());
		Assert.assertEquals(SAMPLE_TEXT, tweets.get(0).getText());
		log.debug("Tweets found: " + tweets);
	}
	
	@Test
	public void testUnfollowUser() {
		
		log.debug("User " + USER_LOGIN + " is unfollowing " + FOLLOWED_LOGIN);
		facade.unfollowUser(USER_LOGIN, FOLLOWED_LOGIN);
		Assert.assertTrue(true);
		
		List<User> followings = facade.findUserFollowings(USER_LOGIN);
		Assert.assertNull(followings);
		log.debug("Following users: " + followings);
		
		List<User> followers = facade.findUserFollowers(FOLLOWED_LOGIN);
		Assert.assertNull(followers);
		log.debug("Follower users: " + followers);
	}

	@Test
	public void testRemoveTweet() {
		
		Assert.assertNotNull(lastTweetId);
		
		Tweet retrieved = facade.findTweet(lastTweetId);
		Assert.assertNotNull(retrieved);
		
		log.debug("Removing tweet: " + lastTweetId);
		facade.removeTweet(lastTweetId);
		Assert.assertTrue(true);

		retrieved = facade.findTweet(lastTweetId);
		Assert.assertNull(retrieved);
		log.debug("Tweets found: " + retrieved);

		List<Tweet> tweets = facade.findUserLastTweets(USER_LOGIN);
		Assert.assertTrue(tweets.isEmpty());
		log.debug("Tweets found for user: " + tweets);
	}

	@Test
	public void testRemoveUser() {
		
		log.debug("Removing user: " + USER_LOGIN);
		facade.removeUser(USER_LOGIN);
		facade.removeUser(FOLLOWED_LOGIN);
		Assert.assertTrue(true);
		
		User user = facade.findUserByLogin(USER_LOGIN);
		Assert.assertNull(user);
		log.debug("User found: " + user);
	}

}
