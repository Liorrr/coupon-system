package com.lior.util;

import com.lior.model.Company;
import com.lior.model.Coupon;
import com.lior.model.Customer;
import com.lior.model.enums.Category;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectExtractor {

    //Create a Company object out of database result set
    public static Company companyFromResultSet(ResultSet result) throws SQLException {
        Company company = new Company();
        company.setId(result.getLong("id"));
        company.setName(result.getString("name"));
        company.setPassword(result.getInt("password"));
        company.setEmail(result.getString("email"));
        return company;
    }

    //Create a coupon object out of database result set
    public static Coupon couponFromResultSet(ResultSet result) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(result.getLong("id"));
        coupon.setTitle(result.getString("title"));
        coupon.setCompanyID(result.getInt("company_id"));
        coupon.setStartDate(result.getDate("start_date"));
        coupon.setEndDate(result.getDate("end_date"));
        coupon.setAmount(result.getInt("amount"));
        coupon.setDescription(result.getString("description"));
        coupon.setPrice(result.getFloat("price"));
        coupon.setImage(result.getString("image"));
        coupon.setCategoryID(result.getInt("category_id"));
        return coupon;
    }

    //Create a coupon object out of database result set
    public static Customer customerFromResultSet(ResultSet result) throws SQLException {
        Customer customer = new Customer();
        customer.setId(result.getLong("id"));
        customer.setFirstName(result.getString("first_name"));
        customer.setLastName(result.getString("last_name"));
        customer.setEmail(result.getString("email"));
        customer.setPassword(result.getInt("password"));
        return customer;
    }


}