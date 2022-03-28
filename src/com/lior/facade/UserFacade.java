package com.lior.facade;

import com.lior.dao.CompanyDao;
import com.lior.dao.CustomerDoa;
import com.lior.exceptions.CrudException;
import com.lior.exceptions.LoginException;
import com.lior.model.enums.ClientType;

import java.sql.SQLException;

public abstract class UserFacade {
    public static final CompanyDao companyDAO = CompanyDao.instance;
    public static final CustomerDoa customerDAO = CustomerDoa.instance;

    //abstract for Facade classes that will require login function
    protected abstract boolean login(String email, String password, ClientType clientType) throws LoginException, CrudException, SQLException;

}
