package com.hsiconsultoria.bean;

import org.thiesen.helenaorm.annotations.ColumnProperty;
import org.thiesen.helenaorm.annotations.HelenaColumnBean;
import org.thiesen.helenaorm.annotations.KeyProperty;

@HelenaColumnBean(keyspace = "Twitter", columnFamily = "Following", secondaryColumnFamily = "Followers")
public class Followship {

	@KeyProperty
	private String follower;

	@ColumnProperty
	private String followed;

	public Followship() {
		super();
	}

	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}

	public String getFollowed() {
		return followed;
	}

	public void setFollowed(String followed) {
		this.followed = followed;
	}

	@Override
	public String toString() {
		return "Followship [follower=" + follower + ", followed=" + followed + "]";
	}

}