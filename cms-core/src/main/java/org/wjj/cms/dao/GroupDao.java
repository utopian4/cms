package org.wjj.cms.dao;


import org.springframework.stereotype.Repository;
import org.wjj.basic.dao.BaseDao;
import org.wjj.cms.model.Group;

@Repository("groupDao")
public class GroupDao extends BaseDao<Group> implements IGroupDao {

}
