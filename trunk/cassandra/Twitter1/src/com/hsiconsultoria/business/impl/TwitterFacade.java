package com.hsiconsultoria.business.impl;

import java.util.ArrayList;
import java.util.List;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.business.ITwitterFacade;
import com.hsiconsultoria.dao.IBaseDAO;
import com.hsiconsultoria.dao.IFollowshipDAO;
import com.hsiconsultoria.dao.ITweetDAO;
import com.hsiconsultoria.dao.IUserDAO;
import com.hsiconsultoria.dao.impl.FollowshipDAO;
import com.hsiconsultoria.dao.impl.TweetDAO;
import com.hsiconsultoria.dao.impl.UserDAO;

public class TwitterFacade implements ITwitterFacade {

	private IUserDAO userDAO;
	private IFollowshipDAO followshipDAO;
	private ITweetDAO tweetDAO;

	private static final int TWEETS_DEFAULT_COUNT = 40;
	
	public void startup() {
		userDAO = new UserDAO();
		((IBaseDAO) userDAO).startup();
		followshipDAO = new FollowshipDAO();
		((IBaseDAO) followshipDAO).startup();
		tweetDAO = new TweetDAO();
		((IBaseDAO) tweetDAO).startup();
	}
	
	public void shutdown() {
//		((IBaseDAO) userDAO).shutdown();
		userDAO = null;
		followshipDAO = null;
		tweetDAO = null;
	}
	
	@Override
	public User createUser(String login, String name, String password) {
		
		User user = new User();
		user.setLogin(login);
		user.setName(name);
		user.setPassword(password);
		
		userDAO.save(user);
		
		return user;
	}

	@Override
	public void removeUser(String login) {
		
		User user = userDAO.findByLogin(login);
		userDAO.remove(user);
	}

	@Override
	public User findUserByLogin(String login) {
		
		User user = userDAO.findByLogin(login);
		
		return user;
	}

	@Override
	public List<User> findUserFollowings(String login) {
		
		User user = userDAO.findByLogin(login);
		List<String> logins = followshipDAO.findFollowingsLogins(user);
		List<User> users = userDAO.findByLogins(logins);
		
		return users;
	}

	@Override
	public List<User> findUserFollowers(String login) {
		
		User user = userDAO.findByLogin(login);
		List<String> ids = followshipDAO.findFollowersLogins(user);
		List<User> users = userDAO.findByLogins(ids);
		
		return users;
	}

	@Override
	public Followship followUser(String login, String followed) {
		
		User followerUser = userDAO.findByLogin(login);
		User followedUser = userDAO.findByLogin(followed);
		
		Followship followship = new Followship();
		followship.setFollowed(followedUser);
		followship.setFollower(followerUser);
		followshipDAO.save(followship);
		
		return followship;
	}

	@Override
	public void unfollowUser(String login, String followed) {

		User followerUser = userDAO.findByLogin(login);
		User followedUser = userDAO.findByLogin(followed);
		
		Followship followship = new Followship();
		followship.setFollowed(followedUser);
		followship.setFollower(followerUser);

		followshipDAO.remove(followship);
	}

	@Override
	public Tweet postTweet(String login, String text) {
		
		User user = userDAO.findByLogin(login);
		
		List<String> logins = followshipDAO.findFollowingsLogins(user);
		if (logins != null) {
			for (String l : logins) {
				User follower = new User();
				follower.setLogin(l);
				user.addFollower(follower);
			}
		}

		Tweet tweet = new Tweet();
		tweet.setId((long) (Math.random() * 1E10));
		tweet.setUser(user);
		tweet.setText(text);
				
		tweetDAO.save(tweet);
		
		return tweet;
	}

	@Override
	public void removeTweet(Long id) {

		Tweet tweet = tweetDAO.findById(id);

		tweetDAO.remove(tweet);
	}

	@Override
	public List<Tweet> findUserLastTweets(String login, int count) {

		User user = userDAO.findByLogin(login);
		
		List<Long> ids1 = tweetDAO.findTimeLine(user);
		List<Long> ids2 = tweetDAO.findUserLine(user);

		List<Long> ids = new ArrayList<Long>();
		if (ids1 != null)
			ids.addAll(ids1);
		if (ids2 != null)
			ids.addAll(ids2);
		
		List<Tweet> tweets = tweetDAO.findByIds(ids);

		// TODO: ordernar pelo timestamp e limitar a quantidade de ocorrências (count)
		
		return tweets;
	}

	public List<Tweet> findUserLastTweets(String login) {
		return findUserLastTweets(login, TWEETS_DEFAULT_COUNT);
	}

}
