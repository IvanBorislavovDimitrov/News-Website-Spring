package app.repositories;

import java.util.List;

public interface GenericRepository<T> {

    List<T> getAll();

    T getById(int id);

    T save(T entity);

    void setEntityClass(Class<T> clazz);

    T delete(T entity);

    T deleteById(int id);

    T update(T entity);
}
