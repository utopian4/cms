package org.wjj.cms.service;

import javax.inject.Inject;

import org.junit.Test;

import static org.easymock.EasyMock.*;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wjj.cms.dao.IUserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class TestUserService {
	
	@Inject
	private IUserService userService;
	@Inject
	private IUserDao userDao;
//	@Inject
//	private IRoleDao roleDao;
//	@Inject
//	private IGroupDao groupDao;
	
	@Test
	public void testDelete(){
		reset(userDao);
		int uid = 2;
		userDao.deleteUserGroups(uid);
		expectLastCall();
		userDao.deleteUserRoles(uid);
		expectLastCall();
		userDao.delete(uid);
		expectLastCall();
		replay(userDao);
		userService.delete(uid);
		verify(userDao);
	}
}
