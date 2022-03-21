package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.model.Coupon;
import com.lior.model.Customer;
import com.lior.util.ObjectExtractor;
import com.lior.util.db.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDoa extends UserDAO<Long, Customer> {
    public final static CustomerDoa instance = new CustomerDoa();
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
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setInt(4, customer.getPassword());
            preparedStatement.executeUpdate();
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new RuntimeException("No new keys have returned");
            }
            return resultSet.getLong(1);
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public ArrayList<Customer> readAll() throws CrudException {
        Connection connection = null;
        final ArrayList<Customer> customers = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.coupons";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(ObjectExtractor.customerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return customers;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public void delete(Long id) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "DELETE FROM coupon_system.customers WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public void update(Customer customer) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "UPDATE coupon_system.customers SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setInt(4, customer.getPassword());
            preparedStatement.setLong(5, customer.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Customer readByEmail(String email) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.customers WHERE email = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new MissingGeneratedKeysException();
            }
            return ObjectExtractor.customerFromResultSet(resultSet);
        } catch (SQLException | MissingGeneratedKeysException e) {
            throw new RuntimeException(e.getCause());

        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public boolean isExists(String email) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT COUNT(email) AS isExist FROM coupon_system.customers WHERE email = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            int numberFromCount = resultSet.getInt(1);
            if (numberFromCount == 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return false in case of exception in the first try catch case.
        try {
            return false;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Customer read(Long id) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.customers WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return ObjectExtractor.customerFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    //get all coupons related to the customer from customer_vs_coupons table
    public void getAllCoupons(Customer customer) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT coupons.* FROM coupons, customers_vs_coupons WHERE coupons.id = customers_vs_coupons.coupon_id AND customers_vs_coupons.customer_id = ?;";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, customer.getId());
            final ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Coupon> coupons = null;
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
            customer.setCoupons(coupons);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.returnConnection(connection);
        }
    }



}


