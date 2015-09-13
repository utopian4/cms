package org.wjj.basic.test.util;


import java.util.List;

import junit.framework.Assert;

import org.wjj.basic.model.User;

public class EntitiesHelper {
	
	 private static User baseUser = new User(1,"admin1");
	   
	  public static void assertUser(User expected,User actual) {
	    Assert.assertNotNull(expected);
	    Assert.assertEquals(expected.getId(), actual.getId());
	    Assert.assertEquals(expected.getUsername(), actual.getUsername());
	  }
	   
	  public static void assertUser(User expected) {
	    assertUser(expected, baseUser);
	  }

	public static void assertUser(List<User> expected, List<User> actual) {
		for(int i =0; i< actual.size(); i++){
			User ue = expected.get(i);
			User ua = actual.get(i);
			assertUser(ue, ua);
		}
	}
}
