package com.hsiconsultoria.bean;

import br.gov.frameworkdemoiselle.cassandra.annotation.CassandraColumn;
import br.gov.frameworkdemoiselle.cassandra.annotation.ColumnProperty;
import br.gov.frameworkdemoiselle.cassandra.annotation.KeyProperty;
import br.gov.frameworkdemoiselle.cassandra.annotation.ValueProperty;

/**
 * @author Rodrigo Hjort
 */
@CassandraColumn(keyspace = "Twitter", columnFamily = "Timeline")
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

	public String toString() {
		return "TimeLine [user=" + user + ", time=" + time + ", tweet=" + tweet + "]";
	}
	
}
