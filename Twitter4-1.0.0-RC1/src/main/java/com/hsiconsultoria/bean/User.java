package com.hsiconsultoria.bean;

import java.util.ArrayList;
import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.annotation.CassandraEntity;
import br.gov.frameworkdemoiselle.cassandra.annotation.KeyProperty;
import br.gov.frameworkdemoiselle.cassandra.annotation.Transient;

/**
 * @author Rodrigo Hjort
 */
@CassandraEntity(keyspace = "Twitter", columnFamily = "Users")
public class User {

	@KeyProperty
	private String login;
	
	private String name;
	private String password;

	@Transient
	private List<User> following;
	
	@Transient
	private List<User> followers;

	public User() {
		super();
	}

	public User(String login) {
		this.login = login;
	}

	public User(String login, String name, String password) {
		this.login = login;
		this.name = name;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public void addFollower(User follower) {
		if (this.followers == null)
			this.followers = new ArrayList<User>();
		this.followers.add(follower);
	}

	public String toString() {
		return "User [login=" + login + ", name=" + name + ", password=" + password + "]";
	}

}
