package com.hsiconsultoria.bean;

import org.thiesen.helenaorm.annotations.ColumnProperty;
import org.thiesen.helenaorm.annotations.HelenaColumnBean;
import org.thiesen.helenaorm.annotations.KeyProperty;
import org.thiesen.helenaorm.annotations.ValueProperty;

@HelenaColumnBean(keyspace = "Twitter", columnFamily = "Timeline")
public class TimeLine implements TweetLine {

	@KeyProperty
	private String user;

	@ColumnProperty
	private Long time;
	
	@ValueProperty
	private Long tweet;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getTweet() {
		return tweet;
	}

	public void setTweet(Long tweet) {
		this.tweet = tweet;
	}

	@Override
	public String toString() {
		return "TimeLine [user=" + user + ", time=" + time + ", tweet=" + tweet + "]";
	}
	
}
