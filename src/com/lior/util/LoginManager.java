package com.lior.util;

import com.lior.exceptions.CrudException;
import com.lior.exceptions.LoginException;
import com.lior.facade.AdminFacade;
import com.lior.facade.CompanyFacade;
import com.lior.facade.CustomerFacade;
import com.lior.model.enums.ClientType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//login switch case function that return one of the following objects: adminFacade, companyFacade, customerFacade or null in case of failure.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginManager {
    private static LoginManager instance = new LoginManager();

    public Object login(final String email, final  String password, ClientType  clientType) throws LoginException, CrudException {
        switch  (clientType)  {
            case  ADMIN:
                AdminFacade adminFacade =  new AdminFacade();
                if (adminFacade.login(email,password,clientType)){
                    return adminFacade;
            }
            case COMPANY:
                CompanyFacade companyFacade = new CompanyFacade();
                if (companyFacade.login(email,password,clientType)){
                    return companyFacade;
                }
            case CUSTOMER:
                CustomerFacade customerFacade = new CustomerFacade();
                if (customerFacade.login(email, password, clientType)){
                    return customerFacade;
                }
        } return null;
    }

    public static LoginManager getInstance() {
        return instance;
    }

}
