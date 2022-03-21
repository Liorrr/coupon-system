package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.exceptions.MissingGeneratedKeysException;

public abstract class UserDAO<ID, Entity> implements CrudDao<ID, Entity> {
    //this function will be overwritten in company and customer login and register logic flow.
    public abstract Entity readByEmail(final String email) throws CrudException;

    public boolean isExists(final String email) throws CrudException {
        //will return true when no entity returned by the function.
        return readByEmail(email) != null;
    }

    public abstract Entity read(Long id) throws Exception, MissingGeneratedKeysException;
}
