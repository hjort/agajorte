package com.hsiconsultoria.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IBaseDAO;
import com.hsiconsultoria.dao.IFollowshipDAO;
import com.hsiconsultoria.dao.impl.FollowshipDAO;

public class FollowshipDAOTest {

	private static IFollowshipDAO dao;

	private static final String FROM_USER_LOGIN = "agajorte18";
	private static final String FROM_USER_NAME = "Agajorte 18";

	private static final String TO_USER_LOGIN = "agajorte22";
	private static final String TO_USER_NAME = "Agajorte 22";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new FollowshipDAO();
		((IBaseDAO) dao).startup();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		((IBaseDAO) dao).shutdown();
		dao = null;
	}

	@Test
	public void testSave() {

		System.out.println("FollowshipDAOTest.testSave()");
		
		User fromUser = new User(FROM_USER_LOGIN, FROM_USER_NAME, null);
		User toUser = new User(TO_USER_LOGIN, TO_USER_NAME, null);

		Followship followship = new Followship();
		followship.setFollower(fromUser);
		followship.setFollowed(toUser);
		
		System.out.println("Saving followship: " + followship);
		dao.save(followship);
		Assert.assertTrue(true);

		/*
		User retrieved = dao.findById(FROM_USER_ID);
		System.out.println("Retrieved user by id: " + retrieved);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(FROM_USER_ID, retrieved.getId());
		Assert.assertEquals(FROM_USER_NAME, retrieved.getUsername());
		Assert.assertEquals(USER_PASS, retrieved.getPassword());
		*/
	}

	@Test
	public void testRetrieveFollowees() {

		System.out.println("FollowshipDAOTest.testRetrieveFollowees()");
		
		User fromUser = new User(FROM_USER_LOGIN, FROM_USER_NAME, null);
		int startId = 1;
		
		System.out.println("User is about to follow some people");
		for (int i = 1; i <= 10; i++) {
			User toUser = new User("user" + (startId + i), TO_USER_NAME + i, null);
			Followship followship = new Followship();
			followship.setFollower(fromUser);
			followship.setFollowed(toUser);
			dao.save(followship);
			Assert.assertTrue(true);
		}
		
		System.out.print("Retrieving people user is following: ");
		List<String> ids = dao.findFollowingsLogins(fromUser);
		System.out.println(ids);
		Assert.assertNotNull(ids);
		Assert.assertFalse(ids.isEmpty());
		Assert.assertTrue(ids.size() >= 10);
	}

	@Test
	public void testRetrieveFollowings() {

		System.out.println("FollowshipDAOTest.testRetrieveFollowings()");
		
		User toUser = new User(TO_USER_LOGIN, TO_USER_NAME, null);
		int startId = 1;
		
		System.out.println("User is about to be followed by some people");
		for (int i = 1; i <= 5; i++) {
			User fromUser = new User("user" + (startId + i), FROM_USER_NAME + i, null);
			Followship followship = new Followship();
			followship.setFollower(fromUser);
			followship.setFollowed(toUser);
			dao.save(followship);
			Assert.assertTrue(true);
		}
		
		System.out.print("Retrieving people followed by the user: ");
		List<String> ids = dao.findFollowersLogins(toUser);
		System.out.println(ids);
		Assert.assertNotNull(ids);
		Assert.assertFalse(ids.isEmpty());
		Assert.assertTrue(ids.size() >= 5);
	}

	/*
	@Test
	public void testRemove() {

		System.out.println("UserDAOTest.testRemove()");

		User user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_NAME);
		user.setPassword(USER_PASS);

		System.out.println("Saving user: " + user);
		dao.save(user);

		System.out.println("Removing user: " + user);
		dao.remove(user);
		Assert.assertTrue(true);

		User retrieved = dao.findById(USER_ID);
		System.out.println("Retrieved user: " + retrieved);
		Assert.assertNull(retrieved);
	}
	*/
	
}
