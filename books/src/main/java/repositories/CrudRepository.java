package repositories;

import java.util.List;

public interface CrudRepository<T> {

    // найти всех
    List<T> findAll();

    // добавить
    void save(T model);

    // найти по id
    T findById(int id);

    // изменить
    void update(T model);

    // удалить
    void remove(int id);

}