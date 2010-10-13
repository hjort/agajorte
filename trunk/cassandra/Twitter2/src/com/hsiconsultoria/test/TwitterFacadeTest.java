package com.hsiconsultoria.test;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.thiesen.helenaorm.HelenaORMDAOFactory;
import org.thiesen.helenaorm.SerializeUnknownClasses;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.business.ITwitterFacade;
import com.hsiconsultoria.business.impl.TwitterFacade;

public class TwitterFacadeTest {

	private static HelenaORMDAOFactory factory;
	private static ITwitterFacade facade;
	
	private static Random random = new Random();
	
	private static final String USER_LOGIN = "iara" + random.nextInt(100);
	private static final String USER_NAME = "Iara";
	private static final String USER_PASS = "pass";

	private static final String FOLLOWED_LOGIN = "boto" + random.nextInt(100);
	private static final String FOLLOWED_NAME = "Boto";

	private static Long lastTweetId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String[] nodes = new String[] {"node1:9160", "node2:9160", "node3:9160"};
		factory = HelenaORMDAOFactory.withConfig(nodes, SerializeUnknownClasses.YES);
//		factory = HelenaORMDAOFactory.withConfig("localhost", 9160, SerializeUnknownClasses.YES);
		facade = new TwitterFacade(factory);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		facade = null;
	}

	@Test
	public void testCreateUser() {
		
		User user =	facade.createUser(USER_LOGIN, USER_NAME, USER_PASS);
		Assert.assertNotNull(user);
	}

	@Test
	public void testFindUserByName() {
		
		User user = facade.findUserByLogin(USER_LOGIN);
		Assert.assertNotNull(user);
		Assert.assertEquals(USER_NAME, user.getName());
		Assert.assertEquals(USER_PASS, user.getPassword());
	}

	@Test
	public void testFollowUser() {
		
		facade.createUser(FOLLOWED_LOGIN, FOLLOWED_NAME, null);
		Assert.assertTrue(true);
		
		Followship followship = facade.followUser(USER_LOGIN, FOLLOWED_LOGIN);
		Assert.assertNotNull(followship);
		
		List<User> followings = facade.findUserFollowings(USER_LOGIN);
		Assert.assertNotNull(followings);
		Assert.assertFalse(followings.isEmpty());
		Assert.assertEquals(1, followings.size());
		Assert.assertEquals(FOLLOWED_LOGIN, followings.get(0).getLogin());
		Assert.assertEquals(FOLLOWED_NAME, followings.get(0).getName());
		
		List<User> followers = facade.findUserFollowers(FOLLOWED_LOGIN);
		Assert.assertNotNull(followers);
		Assert.assertFalse(followers.isEmpty());
		Assert.assertEquals(1, followers.size());
		Assert.assertEquals(USER_LOGIN, followers.get(0).getLogin());
		Assert.assertEquals(USER_NAME, followers.get(0).getName());
		
		facade.followUser(FOLLOWED_LOGIN, USER_LOGIN);
		Assert.assertTrue(true);
	}

	@Test
	public void testPostTweet() {
		
		final String SAMPLE_TEXT = "This is a simple tweet!";
		
		Tweet tweet = facade.postTweet(USER_LOGIN, SAMPLE_TEXT);
		Assert.assertNotNull(tweet);
		lastTweetId = tweet.getId();
		System.out.println("Tweet posted: " + tweet);

		Tweet retrieved = facade.findTweet(lastTweetId);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(lastTweetId, retrieved.getId());
		
		List<Tweet> tweets = facade.findUserLastTweets(USER_LOGIN);
		Assert.assertNotNull(tweets);
		Assert.assertFalse(tweets.isEmpty());
		Assert.assertEquals(1, tweets.size());
		Assert.assertEquals(SAMPLE_TEXT, tweets.get(0).getText());

		tweets = facade.findUserLastTweets(FOLLOWED_LOGIN);
		Assert.assertNotNull(tweets);
		Assert.assertFalse(tweets.isEmpty());
		Assert.assertEquals(1, tweets.size());
		Assert.assertEquals(SAMPLE_TEXT, tweets.get(0).getText());
	}
	
	@Test
	public void testUnfollowUser() {
		
		facade.unfollowUser(USER_LOGIN, FOLLOWED_LOGIN);
		Assert.assertTrue(true);
		
		List<User> followings = facade.findUserFollowings(USER_LOGIN);
		Assert.assertNull(followings);
		
		List<User> followers = facade.findUserFollowers(FOLLOWED_LOGIN);
		Assert.assertNull(followers);
	}

	@Test
	public void testRemoveTweet() {
		
		Assert.assertNotNull(lastTweetId);
		
		Tweet retrieved = facade.findTweet(lastTweetId);
		Assert.assertNotNull(retrieved);
		
		facade.removeTweet(lastTweetId);
		Assert.assertTrue(true);

		retrieved = facade.findTweet(lastTweetId);
		Assert.assertNull(retrieved);

//		List<Tweet> tweets = facade.findUserLastTweets(USER_LOGIN);
//		Assert.assertNull(tweets);
	}

	@Test
	public void testRemoveUser() {
		
		facade.removeUser(USER_LOGIN);
		facade.removeUser(FOLLOWED_LOGIN);
		Assert.assertTrue(true);
		
		User user = facade.findUserByLogin(USER_LOGIN);
		Assert.assertNull(user);
	}

}
