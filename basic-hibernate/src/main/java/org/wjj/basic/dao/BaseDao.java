package org.wjj.basic.dao;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.wjj.basic.model.Pager;
import org.wjj.basic.model.SystemContext;

@SuppressWarnings("unchecked")
public class BaseDao<T> implements IBaseDao<T> {
	
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFacory() {
		return sessionFactory;
	}
	/**
	 * 创建一个Class对象来获取泛型的class
	 * 
	 */
	private Class<T> clz;
	public Class<T> getClz(){
		if(clz==null){
			//获取泛型的class对象
			clz = ((Class<T>)
					(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
		}
		return clz;
	}
	
	@Inject
	public void setSessionFacory(SessionFactory sessionFacory) {
		this.sessionFactory = sessionFacory;
	}
	
	
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public T add(T t) {
		getSession().save(t);
		return t;
	}
	@Override
	public void update(T t) {
		getSession().update(t);
		
	}
	@Override
	public void delete(int id) {
		getSession().delete(this.load(id));
		
	}

	@Override
	public T load(int id) {
		return (T) getSession().load(getClz(), id);
	} 
	@Override
	public java.util.List<T> list(String hql, Object[] args) {
		
		return this.list(hql, args, null);
	}
	@Override
	public java.util.List<T> list(String hql, Object args) {
		return this.list(hql, new Object[]{args});
	}
	@Override
	public java.util.List<T> List(String hql) {
		return this.list(hql, null);
	}
	
	private String initSort(String hql){
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		if(sort!=null&&!"".equals(sort.trim())){
			hql+=" order by "+sort;
			if(!"desc".equals(order)) hql+=" asc";
			else hql+=" desc";
		}
		return hql;
	}
	
	@SuppressWarnings("rawtypes")
	private void setAliasParameter(Query query, Map<String, Object> alias){
		if(alias!=null){
			Set<String> keys = alias.keySet();
			for(String key : keys){
				Object val = alias.get(key);
				if(val instanceof Collection){
					//查询条件是列表
					query.setParameterList(key, (Collection)val);
				} else {
					query.setParameter(key, val);
				}
			}
		}
	}
	
	private void setParameter(Query query, Object[] args){
		if(args!=null && args.length>0){
			int index = 0;
			for(Object arg : args){
				query.setParameter(index++, arg);
			}
		}
	}
	
	@Override
	public java.util.List<T> list(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);
		Query query = getSession().createQuery(hql);
		setAliasParameter(query, alias);
		setParameter(query, args);
		return query.list();
	}
	@Override
	public java.util.List<T> listByAlias(String hql, Map<String, Object> alias) {
		return this.list(hql, null, alias);
	}
	@Override
	public Pager<T> find(String hql, Object[] args) {
		return this.find(hql, args, null);
	}
	@Override
	public Pager<T> find(String hql, Object args) {
		return this.find(hql, new Object[]{args});
	}
	@Override
	public Pager<T> find(String hql) {
		return this.find(hql, null);
	}
	
	@SuppressWarnings("rawtypes")
	private void setPagers(Query query, Pager pages){
		Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageOffset == null||pageOffset<0){
			pageOffset = 0;
		}
		if(pageSize == null || pageSize<0) {
			pageSize = 15;
		}
		pages.setOffset(pageOffset);
		pages.setSize(pageSize);
		query.setFirstResult(pageOffset).setMaxResults(pageSize);
	}
	
	private String getCountHql(String hql, boolean isHql){
		String e = hql.substring(hql.indexOf("from"));
		String count = "select count(*) "+e;
		if(isHql){
			count.replaceAll("fetch", "");
		}
		return count;
	}
	
	@Override
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);
		String cq = getCountHql(hql,true);
		Query cquery = getSession().createQuery(cq);
		Query query = getSession().createQuery(hql);
		//设置别名
		setAliasParameter(query, alias);
		setAliasParameter(cquery, alias);
		//设置参数
		setParameter(query, args);
		setParameter(cquery, args);
		//设置分页
		Pager<T> pages = new Pager<T>();
		setPagers(query, pages);
		List<T> datas = query.list();
		pages.setDatas(datas);
		long total = (Long) cquery.uniqueResult();
		pages.setTotal(total);
		return pages;
	}
	@Override
	public Pager<T> findByAlias(String hql, Map<String, Object> alias) {
		return this.find(hql, null, alias);
	}
	@Override
	public Object queryObject(String hql, Object[] args) {
		return this.queryObject(hql, args, null);
	}
	@Override
	public Object queryObject(String hql, Object args) {
		return this.queryObject(hql, new Object[]{args});
	}
	@Override
	public Object queryObject(String hql) {
		return this.queryObject(hql, null);
	}
	
	@Override
	public Object queryObject(String hql, Object[] args,Map<String, Object> alias) {
		Query query = getSession().createQuery(hql);
		setAliasParameter(query, alias);
		setParameter(query, args);
		return query.uniqueResult();
	}

	@Override
	public Object queryObjectByAlias(String hql, Map<String, Object> alias) {
		return this.queryObject(hql, null, alias);
	}
	
	@Override
	public void updateByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		setParameter(query, args);
		query.executeUpdate();
	}
	@Override
	public void updateByHql(String hql, Object args) {
		this.updateByHql(hql, new Object[]{args});
	}
	@Override
	public void updateByHql(String hql) {
		this.updateByHql(hql, null);
	}
	@Override
	public <U extends Object> List<U> listBySql(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.listBySql(sql, args, null, clz, hasEntity);
	}
	@Override
	public <U extends Object> List<U> listBySql(String sql, Object args, Class<?> clz,
			boolean hasEntity) {
		return this.listBySql(sql, new Object[]{args}, clz, hasEntity);
	}
	@Override
	public <U extends Object> List<U> listBySql(String sql, Class<?> clz,
			boolean hasEntity) {
		return this.listBySql(sql, null, clz, hasEntity);
	}
	@Override
	public <U extends Object> List<U> listBySql(String sql, Object[] args,
			Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		sql = initSort(sql);
		SQLQuery sq = getSession().createSQLQuery(sql);
		setAliasParameter(sq, alias);
		setParameter(sq, args);
		if(hasEntity){
			sq.addEntity(clz);
		}else{
			sq.setResultTransformer(Transformers.aliasToBean(clz));
		}
		return sq.list();
	}
	@Override
	public<U extends Object> List<U> listByAliasSql(String sql, Map<String, Object> alias,
			Class<?> clz, boolean hasEntity) {
		return this.listBySql(sql, null, alias, clz, hasEntity);
	}
	
	@Override
	public <U extends Object> Pager<U> findBySql(String sql, Object[] args, Class<?> clz,
			boolean hasEntity) {
		return this.findBySql(sql, args, null, clz, hasEntity);
	}
	@Override
	public <U extends Object> Pager<U> findBySql(String sql, Object args, Class<?> clz,
			boolean hasEntity) {
		return this.findBySql(sql, new Object[]{args}, clz, hasEntity);
	}
	@Override
	public <U extends Object> Pager<U> findBySql(String sql, Class<?> clz, boolean hasEntity) {
		return this.findBySql(sql, null, clz, hasEntity);
	}
	@Override
	public <U extends Object> Pager<U> findBySql(String sql, Object[] args, Map<String, Object> alias,
			Class<?> clz, boolean hasEntity) {
		sql = initSort(sql);
		String cq = getCountHql(sql,false);
		
		SQLQuery query = getSession().createSQLQuery(sql);
		SQLQuery cquery = getSession().createSQLQuery(cq);
		
		setAliasParameter(query, alias);
		setAliasParameter(cquery, alias);
		
		setParameter(query, args);
		setParameter(cquery, args);
		Pager<U> pages = new Pager<U>();
		setPagers(query, pages);
		if(hasEntity){
			query.addEntity(clz);
		} else {
			query.setResultTransformer(Transformers.aliasToBean(clz));
		}
		pages.setDatas(query.list());
		BigInteger bi =  (BigInteger) cquery.uniqueResult();
		long total = bi.longValue();
		pages.setTotal(total);
		return  pages;
	}
	@Override
	public <U extends Object> Pager<U> findAliasBySql(String sql, Map<String, Object> alias, Class<?> clz,
			boolean hasEntity) {
		return this.findBySql(sql, null, alias, clz, hasEntity);
	}

}
