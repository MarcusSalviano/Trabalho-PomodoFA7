package br.edu.fa7.pomodorofa7.persistence;

import java.util.List;

/**
 * Created by MF on 06/06/16.
 */
public interface IDatabase<T> {
    void insert(T obj);

    void update(T obj);

    void delete(T obj);

    T find(Integer id);

    List<T> findAll();
}
