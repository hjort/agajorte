package com.hsiconsultoria.bean;

/**
 * @author Rodrigo Hjort
 */
public interface TweetLine {

	String getUser();
	void setUser(String user);

	Long getTime();
	void setTime(Long time);

	Long getTweet();
	void setTweet(Long tweet);
	
}
