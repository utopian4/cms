package org.wjj.cms.service;

import java.util.List;

import org.wjj.basic.model.Pager;
import org.wjj.cms.model.Group;
import org.wjj.cms.model.Role;
import org.wjj.cms.model.User;

public interface IUserService {
	
	
	public void add(User user, Integer[] rids, Integer[] gids);
	
	public void delete(int id);
	
	public void update(User user, Integer[] rids, Integer[] gids);
	
	public void updateStatus(int id);
	
	public Pager<User> findUser();
	
	public User load(int id);
	
	public List<Role> listUserRoles(int id);
	
	public List<Group> ListUserGroups(int id);
}
