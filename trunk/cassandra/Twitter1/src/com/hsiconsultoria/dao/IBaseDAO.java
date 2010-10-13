package com.hsiconsultoria.dao;

public interface IBaseDAO {

	static final String KEYSPACE = "Twitter";

	static final String COLUMN_FAMILY_USERS = "Users";

	static final String COLUMN_FAMILY_FOLLOWING = "Following";
	static final String COLUMN_FAMILY_FOLLOWERS = "Followers";

	static final String COLUMN_FAMILY_TWEETS = "Tweets";
	static final String COLUMN_FAMILY_USERLINE = "Userline";
	static final String COLUMN_FAMILY_TIMELINE = "Timeline";

	static final String ENCODING = "utf-8";

	void startup();
	void shutdown();

}
