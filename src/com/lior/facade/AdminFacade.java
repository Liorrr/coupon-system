package com.lior.facade;

import com.lior.dao.CompanyDao;
import com.lior.dao.CouponDao;
import com.lior.dao.CustomerDoa;
import com.lior.exceptions.CrudException;
import com.lior.exceptions.LoginException;
import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.model.Company;
import com.lior.model.Customer;
import com.lior.model.enums.ClientType;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.ArrayList;

@RequiredArgsConstructor
public class AdminFacade extends UserFacade {

    private final CompanyDao companyDao = CompanyDao.instance;
    private final CustomerDoa customerDao = CustomerDoa.instance;
    private final CouponDao couponDao = CouponDao.instance;

    /*
    Admin login  method that verify usertype = admin, email and password is hardcoded
    */
    //TODO - make the hardcoded values STATIC_PARAMETERS
    @Override
    public boolean login(String email, String password, ClientType clientType) throws LoginException {
        return (email.equals("admin@admin.com") && password.equals("admin") && clientType.equals(ClientType.ADMIN));
    }
    /*
    Creates company if the email and name aren't in use.
    */

    public Company createCompany(String email, String name, String password) throws CrudException, SQLException {
        Company company = new Company(name, email, password);
        if (companyDao.isExists(email)) {
            System.out.println("Email exist, try other email address.");
            return null;
        }
        if (companyDao.isCompanyNameExists(name)){
            System.out.println("Name already exist in system.");
            return null;
        }
        else {
        company.setId(companyDao.create(company));
        return company; }
    }

    public Customer createCustomer(String fName, String lName, String email, String password) throws CrudException, SQLException {
        Customer customer = new Customer(fName,lName,email,password);
        if (customerDao.isExists(email)){
            System.out.println("Email exist, try other email address.");
            return null;
        }
        else {
            customer.setId(customerDao.create(customer));
            return customer;
        }
    }

    //update company will only be able to change the password
    public void updateCompany(Company company,String password) throws CrudException {
        company.setNewPassword(password);
        companyDao.update(company);
    }

    //delete company will require 3 steps, delete purchase history, delete coupons, and only then delete company
    public void deleteCompany(final long id) throws CrudException {
        couponDao.deleteAllPurchasesByCompany(id);
        couponDao.deleteAllCouponsByCompany(id);
        companyDao.delete(id);
    }

    public void deleteCustomer(final long id) throws CrudException {
        couponDao.deleteAllPurchasesByCustomer(id);
        customerDao.delete(id);
    }

    public ArrayList<Company> getAllCompanies() throws CrudException {
        return companyDao.readAll();
    }

    public Company getCompanyByID(final Long id) throws MissingGeneratedKeysException, Exception {
        return companyDao.read(id);
    }

    public ArrayList<Customer> getAllCustomers() throws CrudException {
        return customerDao.readAll();
    }

    public Customer getCustomerByID(Long id) throws CrudException {
        return customerDao.read(id);
    }






}
