package com.hsiconsultoria.bean;

import org.thiesen.helenaorm.annotations.HelenaBean;
import org.thiesen.helenaorm.annotations.KeyProperty;

@HelenaBean(keyspace="Twitter", columnFamily="Tweets")
public class Tweet {

	@KeyProperty
	private Long id;
	
	private String user;
	private String text;
	
	public Tweet() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Tweet [id=" + id + ", user=" + user + ", text=" + text + "]";
	}

}
