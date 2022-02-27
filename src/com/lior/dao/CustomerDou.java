package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.exceptions.EmptyResultSetException;
import com.lior.model.Customer;
import com.lior.util.DBtoObject;
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
            final String sqlStatement = "SELECT * FROM coupon_system.customers";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customers.add(DBtoObject.customerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } try {
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
        } try {
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
        } try {
            final String sqlStatement = "UPDATE coupon_system.customers SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setInt(4, customer.getPassword());
            preparedStatement.setLong(5,customer.getId());
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
                throw new EmptyResultSetException();
            }
            return DBtoObject.customerFromResultSet(resultSet);
        } catch (SQLException | EmptyResultSetException e) {
            throw new RuntimeException(e.getCause());

        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public boolean isExists(String email) throws CrudException {
        return readByEmail(email) != null;
    }

    @Override
    public Customer read(Long id) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } try {
            final String sqlStatement = "SELECT * FROM coupon_system.customers WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1,id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                return null;
            }
            return DBtoObject.customerFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        finally {
         connectionPool.returnConnection(connection);
        }
    }
}
