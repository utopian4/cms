package org.wjj.basic.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.wjj.basic.model.Pager;
import org.wjj.basic.model.SystemContext;
import org.wjj.basic.model.User;
import org.wjj.basic.test.util.AbstractDbUnitTestCase;
import org.wjj.basic.test.util.EntitiesHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    	DirtiesContextTestExecutionListener.class,
    	TransactionalTestExecutionListener.class })
public class TestUserDao extends AbstractDbUnitTestCase {
	@Inject
	private SessionFactory sessionFactory;
	@Inject
	private IUserDao userDao;
	
	@Before
	public void setUp() throws DataSetException, SQLException, IOException{
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
		this.backupAllTable();
	}
	
	@Test
	public void testLoad() throws IOException, DatabaseUnitException, SQLException {
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		User u = userDao.load(1);
		EntitiesHelper.assertUser(u);
	}
	
	@Test(expected=ObjectNotFoundException.class)
	public void testDelete() throws IOException, DatabaseUnitException, SQLException {
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		userDao.delete(1);
		User u = userDao.load(1);
		System.out.println(u.getUsername());
	}
	
	@Test
	public void testList() throws IOException, DatabaseUnitException, SQLException {
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		SystemContext.setOrder("desc");
		SystemContext.setSort("id");
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(1,2,3,5,6,7,8,9,10));
		String hql = "from User u where u.id>? and u.id<? and u.id in (:ids)";
		List<User> actual = userDao.list(hql, new Object[]{1,5}, alias);
		List<User> expected = Arrays.asList(new User(3,"admin3"), new User(2, "admin2"));
		EntitiesHelper.assertUser(expected, actual);
	}
	
	@Test
	public void testPager() throws IOException, DatabaseUnitException, SQLException {
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		SystemContext.removeOrder();
		SystemContext.removeSort();
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(3);
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(1,2,4,5,6,7,8,10));
		String hql = "from User u where u.id>? and u.id<? and u.id in (:ids)";
		Pager<User> actual =  userDao.find(hql, new Object[]{1,10}, alias);
		List<User> expected = Arrays.asList(new User(2,"admin2"), new User(4, "admin4"), new User(5, "admin5"));
		EntitiesHelper.assertUser(expected, actual.getDatas());
	}
	
	@Test
	public void testListBySQL() throws IOException, DatabaseUnitException, SQLException {
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		SystemContext.setOrder("desc");
		SystemContext.setSort("id");
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(1,2,3,5,6,7,8,9,10));
		String sql = "select * from t_user u where u.id>? and u.id<? and u.id in (:ids)";
		List<User> actual = userDao.listBySql(sql, new Object[]{1,5}, alias,User.class,true);
		List<User> expected = Arrays.asList(new User(3,"admin3"), new User(2, "admin2"));
		EntitiesHelper.assertUser(expected, actual);
	}
	
	@Test
	public void testPagerBySQL() throws IOException, DatabaseUnitException, SQLException {
		IDataSet ds = createDateSet("t_user");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitCon, ds);
		SystemContext.removeOrder();
		SystemContext.removeSort();
		SystemContext.setPageOffset(0);
		SystemContext.setPageSize(3);
		Map<String, Object> alias = new HashMap<String, Object>();
		alias.put("ids", Arrays.asList(1,2,4,5,6,7,8,10));
		String sql = "select * from t_user u where u.id>? and u.id<? and u.id in (:ids)";
		Pager<User> actual =  userDao.findBySql(sql, new Object[]{1,10}, alias,User.class, true);
		List<User> expected = Arrays.asList(new User(2,"admin2"), new User(4, "admin4"), new User(5, "admin5"));
		EntitiesHelper.assertUser(expected, actual.getDatas());
	}
	
	@After
	public void tearDown() throws DatabaseUnitException, SQLException, IOException{
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		Session s = holder.getSession(); 
		s.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		this.resumeTable();
		
	}
}
