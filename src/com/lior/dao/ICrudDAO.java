package com.lior.dao;

import com.lior.exceptions.CrudException;

import java.util.List;

public interface ICrudDAO <ID, Entity> {
    ID create(final Entity entity) throws CrudException;
    Entity read(final ID id) throws CrudException;
    List<Entity> readAll() throws CrudException;
    void delete(final ID id) throws CrudException;
    void update(final Entity entity) throws CrudException;
}
