package com.hsiconsultoria.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.thiesen.helenaorm.HelenaColumnDAO;
import org.thiesen.helenaorm.HelenaDAO;
import org.thiesen.helenaorm.HelenaORMDAOFactory;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.TimeLine;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.bean.UserLine;
import com.hsiconsultoria.business.ITwitterFacade;

public class TwitterFacade implements ITwitterFacade {

	private HelenaDAO<User> userDAO;
	private HelenaColumnDAO<Followship> followshipDAO;
	private HelenaDAO<Tweet> tweetDAO;
	private HelenaColumnDAO<UserLine> userlineDAO;
	private HelenaColumnDAO<TimeLine> timelineDAO;

	private static final int TWEETS_DEFAULT_COUNT = 40;

	public TwitterFacade(HelenaORMDAOFactory factory) {
		userDAO = factory.makeDaoForClass(User.class);
		followshipDAO = factory.makeColumnDaoForClass(Followship.class);
		tweetDAO = factory.makeDaoForClass(Tweet.class);
		userlineDAO = factory.makeColumnDaoForClass(UserLine.class);
		timelineDAO = factory.makeColumnDaoForClass(TimeLine.class);
	}

	@Override
	public User createUser(String login, String name, String password) {
		
		User user = new User();
		user.setLogin(login);
		user.setName(name);
		user.setPassword(password);
		
		userDAO.insert(user);
		
		return user;
	}

	@Override
	public void removeUser(String login) {
		
		userDAO.delete(login);
	}

	@Override
	public User findUserByLogin(String login) {
		
		User user = userDAO.get(login);
		
		return user;
	}

	@Override
	public List<User> findUserFollowings(String login) {
		
		List<String> ids = followshipDAO.getColumns(login);
		
		if (ids == null || ids.isEmpty())
			return null;
		
		List<User> users = userDAO.get(ids);
		
		return users;
	}

	@Override
	public List<User> findUserFollowers(String login) {
		
		List<String> ids = followshipDAO.getColumnsBySecondary(login);

		if (ids == null || ids.isEmpty())
			return null;
		
		List<User> users = userDAO.get(ids);
		
		return users;
	}

	@Override
	public Followship followUser(String login, String followed) {

		Followship followship = new Followship();
		followship.setFollower(login);
		followship.setFollowed(followed);
		
		followshipDAO.insert(followship);

		return followship;
	}

	@Override
	public void unfollowUser(String login, String followed) {

		Followship followship = new Followship();
		followship.setFollower(login);
		followship.setFollowed(followed);
		
		followshipDAO.delete(followship);
	}

	@Override
	public Tweet postTweet(String login, String text) {

		final long id = (long) (Math.random() * 1E10);

		Tweet tweet = new Tweet();
		tweet.setId(id);
		tweet.setUser(login);
		tweet.setText(text);
		
		tweetDAO.insert(tweet);
		
		final long timestamp = System.currentTimeMillis();

		UserLine userline = new UserLine();
		userline.setUser(login);
		userline.setTime(timestamp);
		userline.setTweet(id);
		userlineDAO.insert(userline);

		List<String> logins = followshipDAO.getColumnsBySecondary(login);
		if (logins != null) {
			TimeLine timeline = null;
			for (String follower : logins) {
				timeline = new TimeLine();
				timeline.setUser(follower);
				timeline.setTime(timestamp);
				timeline.setTweet(id);
				timelineDAO.insert(timeline);
			}
		}

		return tweet;
	}

	@Override
	public void removeTweet(Long id) {

		Tweet tweet = tweetDAO.get(String.valueOf(id));

		tweetDAO.delete(tweet);
	}

	@Override
	public Tweet findTweet(Long id) {

		Tweet tweet = tweetDAO.get(String.valueOf(id));

		return tweet;
	}

	@Override
	public List<Tweet> findUserLastTweets(String login, int count) {

		List<String> ids1 = userlineDAO.getValues(login);
		List<String> ids2 = timelineDAO.getValues(login);
		
		List<String> ids = new ArrayList<String>();
		if (ids1 != null)
			ids.addAll(ids1);
		if (ids2 != null)
			ids.addAll(ids2);
		
		if (ids == null || ids.isEmpty())
			return null;
		
		List<Tweet> tweets = tweetDAO.get(ids);

		// TODO: ordernar pelo timestamp e limitar a quantidade de ocorrências (count)
		
		return tweets;
	}

	public List<Tweet> findUserLastTweets(String login) {
		return findUserLastTweets(login, TWEETS_DEFAULT_COUNT);
	}

}