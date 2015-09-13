package org.wjj.cms.dao;


import org.springframework.stereotype.Repository;
import org.wjj.basic.dao.BaseDao;
import org.wjj.cms.dao.IUserDao;
import org.wjj.cms.model.Group;
import org.wjj.cms.model.Role;
import org.wjj.cms.model.RoleType;
import org.wjj.cms.model.User;
import org.wjj.cms.model.UserGroup;
import org.wjj.cms.model.UserRole;

@SuppressWarnings("unchecked")
@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {

	
	@Override
	public java.util.List<Role> listUserRoles(int userId) {
		String hql = "select ur.role from UserRole ur where ur.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public java.util.List<Integer> ListUserRoleIds(int userId) {
		String hql = "select ur.role.id from UserRole ur where ur.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public java.util.List<Group> listUserGroups(int userId) {
		String hql = "select ug.Goup from UserGroup ug where ug.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public java.util.List<Integer> listUserGroupIds(int userId) {
		String hql = "select ug.group.id from UserGroup ug where ug.user.id=?";
		return this.getSession().createQuery(hql).setParameter(0, userId).list();
	}

	@Override
	public UserRole loadUserRole(int userId, int roleId) {
		String hql = "select ur from UserRole ur where ur.user.id=? and ur.roel.id=?";
		return (UserRole) this.getSession().createQuery(hql).setParameter(0, userId).setParameter(1, roleId).uniqueResult();
	}

	@Override
	public UserGroup loadUserGroup(int userId, int groupId) {
		String hql = "select ug from UserGroup ug where ug.user.id=? and ug.group.id=?";
		return (UserGroup) this.getSession().createQuery(hql).setParameter(0, userId).setParameter(1, groupId).uniqueResult();
	}

	@Override
	public User loadByUsername(String username) {
		String hql = "from User where username=?";
		return (User) this.list(hql, username);
	}

	@Override
	public java.util.List<User> listRoleUsers(int roleId) {
		String hql = "select ur.user from UserRole ur where ur.role.id=?";
		return this.list(hql, roleId);
	}

	@Override
	public java.util.List<User> listRoleUsers(RoleType roleType) {
		String hql = "select ur.user from UserRole ur where ur.role.roleSn=?";
		return this.list(hql, roleType);
	}

	@Override
	public java.util.List<User> listGroupUsers(int groupId) {
		String hql = "select ug.user from UserGroup ug where ug.group.id=?";
		return this.list(hql, groupId);
	}

	@Override
	public void addUserRole(User user, Role role) {
		UserRole ur = this.loadUserRole(user.getId(), role.getId());
		if(ur!=null) return;
		ur = new UserRole();
		ur.setRole(role);
		ur.setUser(user);
		this.getSession().save(ur);
	}

	@Override
	public void addUserGroup(User user, Group group) {
		UserGroup ug = this.loadUserGroup(user.getId(), group.getId());
		if(ug!=null) return;
		ug = new UserGroup();
		ug.setGroup(group);
		ug.setUser(user);
		this.getSession().save(ug);
	}

	@Override
	public void deleteUserRoles(int id) {
		String hql = "delete  UserRole ur where ur.user.id=?";
		this.updateByHql(hql, id);
	}

	@Override
	public void deleteUserGroups(int id) {
		String hql = "delete UserGroup ug where ug.user.id=?";
		this.updateByHql(hql, id);
		
	}

	@Override
	public void deleteUserRole(int uid, int rid) {
		String hql = "delete UserRole ur where ur.user.id=? and ur.role.id=?";
		this.updateByHql(hql, new Object[]{uid, rid});
	}

	@Override
	public void deleteUserGroup(int uid, int gid) {
		String hql = "delete UserGroup ug where ug.user.id=? and ug.group.id=?";
		this.updateByHql(hql, new Object[]{uid, gid});
	}

	
}
