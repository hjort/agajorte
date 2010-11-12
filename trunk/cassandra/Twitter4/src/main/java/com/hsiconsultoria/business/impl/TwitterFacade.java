package com.hsiconsultoria.business.impl;

import java.util.ArrayList;
import java.util.List;

import br.gov.framework.demoiselle.core.layer.integration.Injection;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.TimeLine;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.bean.UserLine;
import com.hsiconsultoria.business.ITwitterFacade;
import com.hsiconsultoria.persistence.IFollowshipDAO;
import com.hsiconsultoria.persistence.ITimeLineDAO;
import com.hsiconsultoria.persistence.ITweetDAO;
import com.hsiconsultoria.persistence.IUserDAO;
import com.hsiconsultoria.persistence.IUserLineDAO;

@SuppressWarnings("serial")
public class TwitterFacade implements ITwitterFacade {

	@Injection
	private IUserDAO userDAO;
	
	@Injection
	private IFollowshipDAO followshipDAO;
	
	@Injection
	private ITweetDAO tweetDAO;
	
	@Injection
	private IUserLineDAO userlineDAO;
	
	@Injection
	private ITimeLineDAO timelineDAO;

	private static final int TWEETS_DEFAULT_COUNT = 40;

	public TwitterFacade() {
//		userDAO = new UserDAO();
//		followshipDAO = new FollowshipDAO();
//		tweetDAO = new TweetDAO();
//		userlineDAO = new UserLineDAO();
//		timelineDAO = new TimeLineDAO();
	}
	
	public User createUser(String login, String name, String password) {
		
		User user = new User();
		user.setLogin(login);
		user.setName(name);
		user.setPassword(password);
		
		userDAO.save(user);
		
		return user;
	}

	public void removeUser(String login) {
		userDAO.delete(new User(login));
	}

	public User findUserByLogin(String login) {
		User user = userDAO.findByLogin(login);
		return user;
	}

	public List<User> findUserFollowings(String login) {
		
		List<String> ids = followshipDAO.findFollowingsLogins(login);
		
		if (ids == null || ids.isEmpty())
			return null;
		
		List<User> users = userDAO.findByLogins(ids);
		
		return users;
	}

	public List<User> findUserFollowers(String login) {
		
		List<String> ids = followshipDAO.findFollowersLogins(login);

		if (ids == null || ids.isEmpty())
			return null;
		
		List<User> users = userDAO.findByLogins(ids);
		
		return users;
	}

	public Followship followUser(String login, String followed) {

		Followship followship = new Followship();
		followship.setFollower(login);
		followship.setFollowed(followed);
		
		followshipDAO.save(followship);

		return followship;
	}

	public void unfollowUser(String login, String followed) {

		Followship followship = new Followship();
		followship.setFollower(login);
		followship.setFollowed(followed);
		
		followshipDAO.delete(followship);
	}

	public Tweet postTweet(String login, String text) {

		final long id = (long) (Math.random() * 1E10);

		Tweet tweet = new Tweet();
		tweet.setId(id);
		tweet.setUser(login);
		tweet.setText(text);
		
		tweetDAO.save(tweet);
		
		final long timestamp = System.currentTimeMillis();

		UserLine userline = new UserLine();
		userline.setUser(login);
		userline.setTime(timestamp);
		userline.setTweet(id);
		userlineDAO.save(userline);

		List<String> logins = followshipDAO.findFollowersLogins(login);
		if (logins != null) {
			TimeLine timeline = null;
			for (String follower : logins) {
				timeline = new TimeLine();
				timeline.setUser(follower);
				timeline.setTime(timestamp);
				timeline.setTweet(id);
				timelineDAO.save(timeline);
			}
		}

		return tweet;
	}

	public void removeTweet(Long id) {
		Tweet tweet = tweetDAO.findById(id);
		tweetDAO.delete(tweet);
	}

	public Tweet findTweet(Long id) {
		Tweet tweet = tweetDAO.findById(id);
		return tweet;
	}

	public List<Tweet> findUserLastTweets(String login, int count) {

		List<Long> ids1 = userlineDAO.findUserLine(login);
		List<Long> ids2 = timelineDAO.findTimeLine(login);
		
		List<Long> ids = new ArrayList<Long>();
		if (ids1 != null)
			ids.addAll(ids1);
		if (ids2 != null)
			ids.addAll(ids2);
		
		if (ids == null || ids.isEmpty())
			return null;
		
		List<Tweet> tweets = tweetDAO.findByIds(ids);

		// TODO: ordernar pelo timestamp e limitar a quantidade de ocorrências (count)
		
		return tweets;
	}

	public List<Tweet> findUserLastTweets(String login) {
		return findUserLastTweets(login, TWEETS_DEFAULT_COUNT);
	}

}