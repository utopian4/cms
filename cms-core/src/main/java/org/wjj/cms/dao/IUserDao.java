package org.wjj.cms.dao;

import java.util.List;

import org.wjj.basic.dao.IBaseDao;
import org.wjj.cms.model.Group;
import org.wjj.cms.model.Role;
import org.wjj.cms.model.RoleType;
import org.wjj.cms.model.User;
import org.wjj.cms.model.UserGroup;
import org.wjj.cms.model.UserRole;

public interface IUserDao extends IBaseDao<User> {
	
	public List<Role> listUserRoles(int userId);
	
	public List<Integer> ListUserRoleIds(int userId);
	
	public List<Group> listUserGroups(int userId);
	
	public List<Integer> listUserGroupIds(int userId);
	
	public UserRole loadUserRole(int userId, int roleId);
	
	public UserGroup loadUserGroup(int userId, int groupId);
	
	public User loadByUsername(String username);
	
	public List<User> listRoleUsers(int roleId);
	
	public List<User> listRoleUsers(RoleType roleType);
	
	public List<User> listGroupUsers(int groupId);
	
	public void addUserRole(User user, Role role);
	
	public void addUserGroup(User user, Group group);
	
	public void deleteUserRoles(int id);
	
	public void deleteUserGroups(int id);
	
	public void deleteUserRole(int uid, int rid);
	
	public void deleteUserGroup(int uid, int gid);
	
	
} 
