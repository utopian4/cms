package org.wjj.cms.dao;


import org.springframework.stereotype.Repository;
import org.wjj.basic.dao.BaseDao;
import org.wjj.cms.model.Role;

@Repository("roleDao")
public class RoleDao extends BaseDao<Role> implements IRoleDao {

}
