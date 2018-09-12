package app.repositories;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.*;

public class HibernateRepository<T> implements GenericRepository<T> {

	private final SessionFactory sessionFactory;
	private Class<T> entityClass;
	
	public HibernateRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<T> getAll() {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.entityClass);
		criteriaQuery.from(this.entityClass);
		
		List<T> entities = session.createQuery(criteriaQuery).getResultList();
		
		transaction.commit();
		session.close();
		
		return entities;
	}

	@Override
	public T getById(int id) {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		T entity = session.get(this.entityClass, id);
		
		transaction.commit();
		session.close();
		
		return entity;
	}

	@Override
	public T save(T entity) {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(entity);
		
		transaction.commit();
		session.close();
		
		return entity;
	}
	
	@Override
	public T update(T entity) {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		session.update(entity);
		
		transaction.commit();
		session.close();
		
		return entity;
	}

	@Override
	public void setEntityClass(Class<T> clazz) {
		this.entityClass = clazz;
	}

	@Override
	public T delete(T entity) {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		session.delete(entity);
		
		transaction.commit();
		session.close();
		
		return entity;
	}

	@Override
	public T deleteById(int id) {
		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		T entity = session.get(this.entityClass, id);
		
		session.delete(entity);
		
		transaction.commit();
		session.close();
		
		return entity;
	}
	
	
}
