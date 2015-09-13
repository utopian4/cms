package org.wjj.basic.dao;

import java.util.List;
import java.util.Map;

import org.wjj.basic.model.Pager;

public interface IBaseDao<T> {
	
	//添加对象
	public T add(T t);
	
	//更新对象
	public void update(T t);
	
	//根据id删除对象
	public void delete(int id);
	
	//根据id加载对象
	public T load(int id);
	
	//不分页列表对象
	public List<T> list(String hql, Object[] args);
	public List<T> list(String hql, Object args);
	public List<T> List(String hql);
	
	
	//基于别名和查询参数的混合列表对象
	public List<T> list(String hql, Object[] args, Map<String, Object> alias);
	public List<T> listByAlias(String hql, Map<String,Object> alias);
	
	//分页列表对象
	public Pager<T> find(String hql, Object[] args);
	public Pager<T> find(String hql, Object args);
	public Pager<T> find(String hql);
		
		
	//基于别名和查询参数的混合列表对象
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias);
	public Pager<T> findByAlias(String hql, Map<String,Object> alias);
	
		
	//根据hql查询对象	
	public Object queryObject(String hql, Object[] args);
	public Object queryObject(String hql, Object args);
	public Object queryObject(String hql);
	public Object queryObject(String hql, Object[] args, Map<String, Object> alias);
	public Object queryObjectByAlias(String hql, Map<String, Object> alias);
	
	//根据hql更新对象
	public void updateByHql(String hql, Object[] args);
	public void updateByHql(String hql, Object args);
	public void updateByHql(String hql);
	
	//根据sql查询列表,不包含关联对象
	//该对象是否是一个hibernate所管理的实体，如果不是，需要所使用setResultTransform查询
	public <U extends Object> List<U> listBySql(String sql, Object[] args, Class<?> clz, boolean hasEntity);
	public <U extends Object> List<U> listBySql(String sql, Object args, Class<?> clz, boolean hasEntity);
	public <U extends Object> List<U> listBySql(String sql, Class<?> clz, boolean hasEntity);
	
	public <U extends Object> List<U> listBySql(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity);
	public <U extends Object> List<U> listByAliasSql(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity);

	//根据sql查询列表,不包含关联对象
		//该对象是否是一个hibernate所管理的实体，如果不是，需要所使用setResultTransform查询
		public <U extends Object> Pager<U> findBySql(String sql, Object[] args, Class<?> clz, boolean hasEntity);
		public <U extends Object> Pager<U> findBySql(String sql, Object args, Class<?> clz, boolean hasEntity);
		public <U extends Object> Pager<U> findBySql(String sql, Class<?> clz, boolean hasEntity);
		
		public <U extends Object> Pager<U> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity);
		public <U extends Object> Pager<U> findAliasBySql(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity);
	
}
