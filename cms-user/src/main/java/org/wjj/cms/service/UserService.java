package org.wjj.cms.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;
import org.wjj.basic.model.Pager;
import org.wjj.cms.dao.IGroupDao;
import org.wjj.cms.dao.IRoleDao;
import org.wjj.cms.dao.IUserDao;
import org.wjj.cms.model.CmsException;
import org.wjj.cms.model.Group;
import org.wjj.cms.model.Role;
import org.wjj.cms.model.User;

@Service("userService")
public class UserService implements IUserService{
	
	private IUserDao userDao;
	private IRoleDao roleDao;
	private IGroupDao groupDao;
	
	
	public IUserDao getUserDao() {
		return userDao;
	}
	@Inject
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IRoleDao getRoleDao() {
		return roleDao;
	}
	@Inject
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public IGroupDao getGroupDao() {
		return groupDao;
	}
	@Inject
	public void setGroupDao(IGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public void add(User user, Integer[] rids, Integer[] gids) {
		User u = userDao.loadByUsername(user.getUsername());
		if(u!=null) throw new CmsException("添加的用户已经存在");
		userDao.add(user);
		for(Integer rid : rids){
			//1. 检查角色对象是否存在
			Role role = roleDao.load(rid);
			if(role==null) throw new CmsException("要添加的用户角色 不存在");
			userDao.addUserRole(user, role);
		}
		
		for(Integer gid : gids){
			Group group = groupDao.load(gid);
			if(group==null) throw new CmsException("要添加的组对象不存在");
			userDao.addUserGroup(user, group);
		}
	}

	@Override
	public void delete(int id) {
		
		//TODO 需要进行用户是否有文章的判断
		userDao.deleteUserGroups(id);
		
		userDao.deleteUserRoles(id);
		
		userDao.delete(id);
	}

	@Override
	public void update(User user, Integer[] rids, Integer[] gids) {
		// 1. 获取用户已经存在的组id和角色id
		List<Integer> erids = userDao.ListUserRoleIds(user.getId());
		List<Integer> egids = userDao.ListUserRoleIds(user.getId());
		//判断如果erids不存在rids 进行添加；
		for(Integer rid : rids){
			if(!erids.contains(rid)){
				Role role = roleDao.load(rid);
				if(role==null) throw new CmsException("要添加的用户角色 不存在");
				userDao.addUserRole(user, role);
			}
		}
		for(Integer gid : gids){
			if(!egids.contains(gid)){
				Group group = groupDao.load(gid);
				if(group==null) throw new CmsException("要添加的组 不存在");
				userDao.addUserGroup(user, group);
			}
		}
		//3. 进行删除
		for(Integer erid : erids){
			if(ArrayUtils.contains(rids, erids)){
				userDao.deleteUserRole(user.getId(), erid);
			}
		}
		for(Integer egid : egids){
			if(ArrayUtils.contains(rids, egids)){
				userDao.deleteUserGroup(user.getId(), egid);
			}
		}
	}

	@Override
	public void updateStatus(int id) {
		User u = userDao.load(id);
		if(u==null) throw new CmsException("修改状态的用户不存在");
		if(u.getStatus()==0) u.setStatus(1);
		else u.setStatus(0);
		userDao.update(u);
	}

	@Override
	public Pager<User> findUser() {
		return userDao.find("from User");
	}

	@Override
	public User load(int id) {
		return userDao.load(id);
	}

	@Override
	public List<Role> listUserRoles(int id) {
		return userDao.listUserRoles(id);
	}

	@Override
	public List<Group> ListUserGroups(int id) {
		return userDao.listUserGroups(id);
	}

}
