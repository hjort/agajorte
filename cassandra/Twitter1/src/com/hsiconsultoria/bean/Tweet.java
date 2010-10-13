package com.hsiconsultoria.bean;

public class Tweet {

	private Long id;
	private User user;
	private String text;
	
	public Tweet() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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
