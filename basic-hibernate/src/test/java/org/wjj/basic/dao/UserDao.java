package org.wjj.basic.dao;

import org.springframework.stereotype.Repository;
import org.wjj.basic.model.User;

@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {
	

}
