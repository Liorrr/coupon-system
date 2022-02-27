package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.model.Company;
import com.lior.model.Customer;
import com.lior.util.db.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDou extends UserDAO<Long, Customer> {
    public final static CustomerDou instance = new CustomerDou();
    private ConnectionPool connectionPool = null;

    @Override
    public Long create(Customer customer) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "INSERT INTO coupon_system.customers (first_name,last_name,email,password) VALUES (?,?,?,?)";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setInt(4, customer.getPassword());
            preparedStatement.executeUpdate();
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new RuntimeException("No new keys have returned");
            } return resultSet.getLong(1);
        } finally {
            connectionPool.returnConnection(connection);
        }
    }
    

    @Override
    public List<Customer> readAll(ArrayList<Customer> customers) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } try {

        }

        return customers;
    }

    @Override
    public void delete(Long aLong) throws CrudException {

    }

    @Override
    public void update(Customer customer) throws CrudException {

    }

    @Override
    public Customer readByEmail(String email) throws CrudException {
        return null;
    }

    @Override
    public Company read(Long id) throws CrudException {
        return null;
    }
}
