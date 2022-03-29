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
            connection = connectionPool.getInstance().getConnection();
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
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public ArrayList<Customer> readAll() throws CrudException, SQLException {
        Connection connection = null;
        final ArrayList<Customer> customers = new ArrayList<Customer>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.customers";
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
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void delete(Long id) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
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
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void update(Customer customer) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
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
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public Customer readByEmail(String email) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
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
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public boolean isExists(String email) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT COUNT(email) AS isExist FROM coupon_system.customers WHERE email = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                if (resultSet.getInt(1)==0){
                    return false;
                }
                if (resultSet.wasNull()){
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("There was an issue with the query");
        }
        // return false in case of exception in the first try catch case.
        finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public Customer read(Long id) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
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
            connectionPool.getInstance().returnConnection(connection);
        }
    }



}


