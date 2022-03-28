package com.lior.dao;

import com.lior.exceptions.CrudException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CrudDao<ID, Entity> {
    Long create(final Entity entity) throws CrudException, SQLException;
    Entity read(final ID id) throws CrudException, SQLException;
    ArrayList<Entity> readAll() throws CrudException, SQLException;
    void delete(final ID id) throws CrudException, SQLException;
    void update(final Entity entity) throws CrudException, SQLException;
}
