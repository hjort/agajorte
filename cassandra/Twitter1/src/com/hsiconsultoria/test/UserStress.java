package com.hsiconsultoria.test;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.business.ITwitterFacade;
import com.hsiconsultoria.business.impl.TwitterFacade;

public class UserStress {

	private static ITwitterFacade facade;

	private static final int MAX_COUNT = 10000;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserStress app = new UserStress();
		app.startup();
		
		app.createUsers();
//		app.loadUsers();
//		app.removeUsers();
		
		app.shutdown();
	}

	public void startup() {
		facade = new TwitterFacade();
		((TwitterFacade) facade).startup();
	}
	
	public void shutdown() {
		((TwitterFacade) facade).shutdown();
		facade = null;
	}
	
	public void createUsers() {
		for (int i = 1; i <= MAX_COUNT; i++) {
			String pass = String.valueOf(Math.random() * 1e5);
			
			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("HH:MM:ss");
			String name = "User " + df.format(cal.getTime());
			
			User user =	facade.createUser("user" + i, name, pass);
		}
	}
	
	public void loadUsers() {
		for (int i = 1; i <= MAX_COUNT; i++) {
			User user =	facade.findUserByLogin("user" + i);
//			System.out.println(user);
		}
	}
	
	public void removeUsers() {
		for (int i = 1; i <= MAX_COUNT; i++) {
			facade.removeUser("user" + i);
		}
	}
	
}
