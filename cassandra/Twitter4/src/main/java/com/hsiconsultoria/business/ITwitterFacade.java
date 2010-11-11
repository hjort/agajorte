package com.hsiconsultoria.business;

import java.util.List;

import br.gov.framework.demoiselle.core.layer.IFacade;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;

/**
 * @author Rodrigo Hjort
 */
public interface ITwitterFacade extends IFacade {

	// user
	
	User createUser(String login, String name, String password);

	void removeUser(String login);

	User findUserByLogin(String login);

	// followship
	
	Followship followUser(String login, String followed);

	void unfollowUser(String login, String followed);

	/**
	 * Given a login, gets the people that the user is following.
	 * 
	 * @param login
	 * @return
	 */
	List<User> findUserFollowings(String login);

	/**
	 * Given a login, gets the people following that user.
	 * 
	 * @param login
	 * @return
	 */
	List<User> findUserFollowers(String login);

	// tweet
	
	Tweet postTweet(String login, String text);
	
	void removeTweet(Long id);

	Tweet findTweet(Long id);

	/**
	 * @param login
	 * @param count
	 * @return
	 */
	List<Tweet> findUserLastTweets(String login, int count);

	/**
	 * @param login
	 * @return
	 */
	List<Tweet> findUserLastTweets(String login);

}
