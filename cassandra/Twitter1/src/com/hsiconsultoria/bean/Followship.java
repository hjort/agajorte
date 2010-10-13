package com.hsiconsultoria.bean;

public class Followship {

	private User followed;
	private User follower;
	
	public Followship() {
	}

	public User getFollowed() {
		return followed;
	}

	public void setFollowed(User followed) {
		this.followed = followed;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}

	@Override
	public String toString() {
		return "Followship [followed=" + followed + ", follower=" + follower + "]";
	}
	
}
