package com.hsiconsultoria.bean;

import br.gov.frameworkdemoiselle.cassandra.annotation.CassandraColumn;
import br.gov.frameworkdemoiselle.cassandra.annotation.ColumnProperty;
import br.gov.frameworkdemoiselle.cassandra.annotation.KeyProperty;

@CassandraColumn(keyspace = "Twitter", columnFamily = "Following", secondaryColumnFamily = "Followers")
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

	public String toString() {
		return "Followship [follower=" + follower + ", followed=" + followed + "]";
	}

}