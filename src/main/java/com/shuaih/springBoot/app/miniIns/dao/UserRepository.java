package com.shuaih.springBoot.app.miniIns.dao;
import com.shuaih.springBoot.app.miniIns.entities.*;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <User, Integer>{


    /*
        <S extends T> S save(S entity);
                                                                                                                           (2)
        T findOne(ID primaryKey);
                                                                                                                           (3)
        Iterable<T> findAll();

        Long count();
                                                                                                                           (4)
        void delete(T entity);
                                                                                                                           (5)
        boolean exists(ID primaryKey);
     */
}
