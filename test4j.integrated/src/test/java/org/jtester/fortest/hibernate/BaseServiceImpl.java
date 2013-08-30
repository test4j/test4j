package org.jtester.fortest.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseServiceImpl<T extends BaseBean> implements BaseService<T> {
	protected Class<T> claz;

	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		this.claz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;

	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public <E extends BaseBean> E getBeanById(Class<E> clazz, int id) {
		String hql = "from " + clazz.getName()
				+ " as bean where bean.id=:id and (bean.deleted=false or bean.deleted is null)";
		Query q = this.sessionFactory.getCurrentSession().createQuery(hql);
		q.setInteger("id", id);
		E bean = (E) q.uniqueResult();
		return bean;
	}

	public T getBeanById(int id) {
		return this.getBeanById(claz, id);
	}

	@SuppressWarnings("unchecked")
	public <E extends BaseBean> E save(E bean) {
		if (bean.getId() == 0) {
			Serializable pKey = this.session().save(bean);
			return (E) this.session().get(bean.getClass(), pKey);
		} else {
			this.session().saveOrUpdate(bean);
			return bean;
		}
	}

	public Session session() {
		return this.sessionFactory.getCurrentSession();
	}
}
