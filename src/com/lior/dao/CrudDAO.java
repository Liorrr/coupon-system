package com.lior.dao;

import com.lior.exceptions.CrudException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CrudDAO<ID, Entity> {
    Long create(final Entity entity) throws CrudException, SQLException;
    Entity read(final ID id) throws CrudException;
    List<Entity> readAll(final ArrayList<Entity> entities) throws CrudException;
    void delete(final ID id) throws CrudException;
    void update(final Entity entity) throws CrudException;
}
