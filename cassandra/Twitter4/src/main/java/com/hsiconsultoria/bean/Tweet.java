package com.hsiconsultoria.bean;

import br.gov.frameworkdemoiselle.cassandra.annotation.CassandraEntity;
import br.gov.frameworkdemoiselle.cassandra.annotation.KeyProperty;

@CassandraEntity(keyspace = "Twitter", columnFamily = "Tweets")
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

	public String toString() {
		return "Tweet [id=" + id + ", user=" + user + ", text=" + text + "]";
	}

}
