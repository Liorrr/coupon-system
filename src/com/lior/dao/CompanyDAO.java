package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.exceptions.EmptyResultSetException;
import com.lior.model.Company;
import com.lior.util.DBtoObject;
import com.lior.util.db.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO extends UserDAO<Long, Company> {
    //Create eager singleton for companyDAO
    public static final CompanyDAO instance = new CompanyDAO();
    private ConnectionPool connectionPool = null;

    public CompanyDAO() { }

    @Override
    public Company readByEmail(String email) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.companies WHERE email = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new EmptyResultSetException();
            }
            return DBtoObject.companyFromResultSet(resultSet);
        } catch (SQLException | EmptyResultSetException e) { throw new RuntimeException("something went wrong while retrieving data"); }

        finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public boolean isExists(String email) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (Exception e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            return super.isExists(email);
        } finally {
            connectionPool.returnConnection(connection);
        }

    }

    @Override
    public Long create(final Company company) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            final String sqlStatement = "INSERT INTO coupon_system.companies(name,email,password) VALUES(?,?,?)";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setInt(3,company.getPassword());
            preparedStatement.executeUpdate();
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new RuntimeException("No new keys have returned");
            }
            return resultSet.getLong(1);

        } catch (final SQLException e) {
            //need to add exception that will get what type of entity and what type of operation been done here
            //example: (EntityType.COMPANY, CrudOp.CREATE)
            throw new RuntimeException("There was an issue with the query");
        }
         finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Company read(final Long id) throws Exception, EmptyResultSetException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) { throw new RuntimeException("failed to get connection to db"); }

        try {
            final String sqlStatement = "SELECT * FROM coupon_system.companies WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new EmptyResultSetException();
            }
            return DBtoObject.companyFromResultSet(resultSet);
        } catch (final SQLException e) { throw new Exception();}
            //need to add exception that will get what type of entity and what type of operation been done here
            //example: (EntityType.COMPANY, CrudOp.CREATE)
        finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public List readAll(final ArrayList<Company> companies) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) {
            throw new RuntimeException("failed to get connection to db");
        }

        try {
            final String sqlStatement = "SELECT * FROM coupon_system.companies";
            final PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sqlStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            return companies;
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public void delete(final Long id) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) { throw new RuntimeException("failed to get connection to db"); }

        try {
            final String sqlStatement = "DELETE FROM coupon_system.coupons WHERE ID = ?";
            final PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         finally {
        connectionPool.returnConnection(connection);
        }
    }

    @Override
    public void update(Company company) throws CrudException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (InterruptedException e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            final String sqlStatement = "UPDATE coupon_system.companies SET name = ?, email = ?, password = ? WHERE id = ?";
            final PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sqlStatement);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setInt(3, company.getPassword());
            preparedStatement.setLong(4, company.getId());
            final ResultSet resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            connectionPool.returnConnection(connection);
        }

    }
}
