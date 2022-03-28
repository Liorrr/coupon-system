package com.lior.tests;


import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.facade.AdminFacade;
import com.lior.facade.CompanyFacade;
import com.lior.facade.CustomerFacade;
import com.lior.model.Company;
import com.lior.model.Coupon;
import com.lior.model.Customer;
import com.lior.model.enums.ClientType;
import com.lior.util.LoginManager;
import com.lior.util.Texts;
import com.lior.util.db.DataBaseStarter;

import java.sql.Date;
import java.sql.SQLException;


public class Tests {
    final static AdminFacade adminFacade = AdminFacade.instance;
    final static CompanyFacade companyFacade = CompanyFacade.instance;
    final static CustomerFacade customerFacade = CustomerFacade.instance;
    final static LoginManager loginManager = LoginManager.instance;

    public static void TestAll() throws Exception, MissingGeneratedKeysException {
        /*
        * Tests for AdminFacade
        * Starting with create entities
        *
        * Creating Company - Sanity and constraints\negative tests
        */
        System.out.println(Texts.A+"'Create and Update Company' Test Suit:"+Texts.B);
        DataBaseStarter.restartDB();
        //print company details
        System.out.println(Texts.C+"Create Company Sanity Test");
        Company company = adminFacade.addCompany("test@test.com","test","test");
        //print company details
        System.out.println("-Printing Company Details:");
        System.out.println(company.toString());
        //read and print company info from DB using via AdminFacade
        Company companyFromDB = adminFacade.getCompanyByID(company.getId());
        System.out.println("-Printing Company Details using AdminFacade getCompanyByID:");
        System.out.println(companyFromDB.toString());
        //create new entities in order to verify that you can't enter used name or email to company table via AdminFacade
        //try to add company with duplicated name
        System.out.println(Texts.C+"Create Company Negative Tests");
        System.out.println("Try to use company name from Sanity test");
        adminFacade.addCompany("duplicatedName@test.com","test","test");
        System.out.println("Try to use company email from Sanity test");
        adminFacade.addCompany("test@test.com","duplicatedEmail","test");
        //Creating companies for testing
        Company company2 = adminFacade.addCompany("test1@test1.com","test1","test2");
        Company company3 = adminFacade.addCompany("test2@test2.com","test2","test");

        System.out.println(Texts.C+"Testing Update Company Details: API can only update password for the given company.");
        /*
         * update company password from: test, to: test2
         */
        adminFacade.updateCompany(company3,"blabla");
        /*
         * Create Coupons for company
         */
        System.out.println(Texts.A +"'Create Coupon' Test Suit:"+Texts.B);
        System.out.println(Texts.C +"Create coupon Sanity Test");

        Coupon coupon = new Coupon(1,2,"1st coupon","this is the first coupon",2,250.00, Date.valueOf("2022-03-17"),Date.valueOf("2022-04-17"),Texts.IMAGE);
        coupon.setId(companyFacade.addCoupon(company,coupon));
        System.out.println(Texts.C +"Verifying can't insert coupon with duplicated title");
        //try to insert the same coupon again (using the same title) - should fail
        companyFacade.addCoupon(company,coupon);
        //trying to update coupon
        System.out.println("trying to update coupon");
        coupon.setPrice(300.00);
        companyFacade.updateCoupon(coupon);

        System.out.println("trying to add more coupons");
        //add more data to db
        Coupon coupon2 = new Coupon(2,2,"2nd Coupon","This is the second coupon.",100,250.00, Date.valueOf("2022-03-17"),Date.valueOf("2022-04-17"),Texts.IMAGE);
        coupon2.setId(companyFacade.addCoupon(company,coupon2));
        Coupon coupon3 = new Coupon(1,4,"Coupon To Delete","3rd coupon, This coupon will be deleted.",100,250.00, Date.valueOf("2022-03-17"),Date.valueOf("2022-04-17"),Texts.IMAGE);
        coupon3.setId(companyFacade.addCoupon(company,coupon3));
        Coupon coupon4 = new Coupon(1,4,"Coupon out of date","4rd coupon, This coupon will be out of date.",100,250.00, Date.valueOf("2022-02-17"),Date.valueOf("2022-04-17"),Texts.IMAGE);
        coupon4.setId(companyFacade.addCoupon(company,coupon4));

        //get all coupons by company id
        companyFacade.getAllCoupons(1L);

        //get all coupons by company id and category id
        companyFacade.getAllCoupons(1L,2L);

        //get all coupons by company id and max price
        companyFacade.getAllCoupons(1L,250.00);

        //get all company details and coupons and add to coupons arraylist
        company = companyFacade.getCompanyById(1L);
        System.out.println(company.toString());

        //create customers tests
        //create via AdminFacade
        Customer customer = adminFacade.addCustomer("John","Doe","JD@email.com","123456");
        //get customer details via AdminFacade
        System.out.println(adminFacade.getCustomerByID(customer.getId()).toString());

        //create via AdminFacade - using duplicated data - email
        adminFacade.addCustomer("John","Doe","JD@email.com","123456");

        //create more users for testing
        Customer customer2 = adminFacade.addCustomer("John","Doe","JD2@email.com","123456");
        Customer customer3 = adminFacade.addCustomer("John","Doe","JD3@email.com","123456");
        Customer customer4 = adminFacade.addCustomer("John","Doe","JD4@email.com","123456");

        //perform purchases for coupons
        customerFacade.purchaseCoupon(1,1);
        //can't purchase a coupon if the customer already purchased it.
        customerFacade.purchaseCoupon(1,1);

        customerFacade.purchaseCoupon(2,1);
        //can't purchase since amount should be zero now for the first coupon.
        customerFacade.purchaseCoupon(3,1);
        //can't purchase since end-date after current date
        coupon4.setEndDate(Date.valueOf("2022-03-17"));
        companyFacade.updateCoupon(coupon4);
        customerFacade.purchaseCoupon(3,4);

        customerFacade.purchaseCoupon(1,2);
        customerFacade.purchaseCoupon(1,3);
        customerFacade.purchaseCoupon(4,2);
        customerFacade.purchaseCoupon(4,3);
        customerFacade.purchaseCoupon(3,3);
        customerFacade.purchaseCoupon(2,3);

        //get all customer  coupons by user
        System.out.println("get all customer  coupons by user");
        System.out.println(customerFacade.getCustomerCoupons(customer));

        //get all customer  coupons by user id and category id
        System.out.println("get all customer  coupons by user id and category id");
        System.out.println(customerFacade.getCustomerCoupons(1,2L));

        //get all customer coupons by user id and max amount.
        System.out.println("get all customer coupons by user id and max amount.");
        System.out.println(customerFacade.getCustomerCoupons(1,250.00));

        //Return customer's details by id.
        Customer customerFromAPI = customerFacade.getCustomerDetails(1L);

        //get all customers function
        adminFacade.getAllCustomers();
        //get all companies function
        adminFacade.getAllCompanies();

        System.out.println("Deleting entities using facade.");
        //delete functions

        companyFacade.deleteCoupon(coupon2);
        adminFacade.deleteCustomer(2);
        adminFacade.deleteCompany(2);

        loginManager.login("admin@admin.com","admin", ClientType.ADMIN);
        loginManager.login("JD@email.com","123456",ClientType.CUSTOMER);
        loginManager.login("test@test.com","test",ClientType.COMPANY);

    }

}
