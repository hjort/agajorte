package com.hsiconsultoria.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IBaseDAO;
import com.hsiconsultoria.dao.IUserDAO;
import com.hsiconsultoria.dao.impl.UserDAO;

public class UserDAOTest {

	private static IUserDAO dao;

	private static final String USER_LOGIN = "agajorte";
	private static final String USER_NAME = "Rodrigo Hjort";
	private static final String USER_PASS = "strongpass18";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new UserDAO();
		((IBaseDAO) dao).startup();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		((IBaseDAO) dao).shutdown();
		dao = null;
	}

	@Test
	public void testSave() {

		System.out.println("UserDAOTest.testSave()");

		User user = new User();
		user.setLogin(USER_LOGIN);
		user.setName(USER_NAME);
		user.setPassword(USER_PASS);

		System.out.println("Saving user: " + user);
		dao.save(user);
		Assert.assertTrue(true);

		User retrieved = dao.findByLogin(USER_LOGIN);
		System.out.println("Retrieved user by id: " + retrieved);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(USER_LOGIN, retrieved.getLogin());
		Assert.assertEquals(USER_NAME, retrieved.getName());
		Assert.assertEquals(USER_PASS, retrieved.getPassword());
		
		retrieved = dao.findByLogin(USER_LOGIN);
		System.out.println("Retrieved user by name: " + retrieved);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(USER_LOGIN, retrieved.getLogin());
		Assert.assertEquals(USER_NAME, retrieved.getName());
		Assert.assertEquals(USER_PASS, retrieved.getPassword());
	}

	@Test
	public void testRetrieve() {

		System.out.println("UserDAOTest.testRetrieve()");

		int startId = 1;
		
		System.out.println("Saving users");
		for (int i = 1; i <= 10; i++) {
			User user = new User();
			user.setLogin("user" + (startId + i));
			user.setName(USER_NAME + i);
			user.setPassword(USER_PASS + i);
			dao.save(user);
		}

		for (int i = 1; i <= 5; i++) {
			String login = "user" + (startId + i);
			System.out.print("Retrieving user with login '" + login + "': ");
			User user = dao.findByLogin(login);
			System.out.println(user);
		}
		
		List<String> ids = new ArrayList<String>(5);
		for (int i = 4; i <= 8; i++) {
			String login = "user" + (startId + i);
			ids.add(login);
		}
		System.out.println("Retrieving users with logins: " + ids);
		List<User> users = dao.findByLogins(ids);
		Assert.assertNotNull(users);
		Assert.assertFalse(users.isEmpty());
		Assert.assertTrue(users.size() >= 5);
		for (User user : users) {
			System.out.println("- " + user);
//			Assert.assertTrue(user.getId() >= startId + 4 && user.getId() <= startId + 8);
		}
	}

	@Test
	public void testRemove() {

		System.out.println("UserDAOTest.testRemove()");

		User user = new User();
		user.setLogin(USER_LOGIN);
		user.setName(USER_NAME);
		user.setPassword(USER_PASS);

		System.out.println("Saving user: " + user);
		dao.save(user);

		System.out.println("Removing user: " + user);
		dao.remove(user);
		Assert.assertTrue(true);

		User retrieved = dao.findByLogin(USER_LOGIN);
		System.out.println("Retrieved user: " + retrieved);
		Assert.assertNull(retrieved);
	}
	
}
