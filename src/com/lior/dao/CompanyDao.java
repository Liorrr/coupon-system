package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.model.Company;
import com.lior.util.ObjectExtractor;
import com.lior.util.db.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyDao extends UserDAO<Long, Company> {
    //Create eager singleton for companyDAO
    public static final CompanyDao instance = new CompanyDao();
    private ConnectionPool connectionPool = null;


    @Override
    public Company readByEmail(String email) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.companies WHERE email = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new MissingGeneratedKeysException();
            }
            return ObjectExtractor.companyFromResultSet(resultSet);
        } catch (SQLException | MissingGeneratedKeysException e) { throw new RuntimeException("something went wrong while retrieving data"); }

        finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }
    /*
     is company existing by email return true\false, query counting number of companies with the same email,
     in case email exist returned number will be 1 since email is unique → return true  , if it's 0 → return true.
    */
    @Override
    public boolean isExists(String email) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        } try {
            final String sqlStatement = "SELECT COUNT(`coupon_system`.`companies`.`email`) AS isExist FROM `coupon_system`.`companies` WHERE email = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,email);
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


    /*
 is company existing by email return true\false, query counting number of companies with the same email,
 in case email exist returned number will be 1 since email is unique → return true  , if it's 0 → return true.
*/

    public boolean isCompanyNameExists(String companyName) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        } try {
            final String sqlStatement = "SELECT COUNT(name) AS isExist FROM coupon_system.companies WHERE name = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,companyName);
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
    public Long create(final Company company) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | NullPointerException | SQLException e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            final String sqlStatement = "INSERT INTO coupon_system.companies(name,email,password) VALUES(?,?,?)";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setInt(3,company.getPassword());
            preparedStatement.executeUpdate();
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                return null;
            }
            return resultSet.getLong(1);

        } catch (final SQLException e) {
            //need to add exception that will get what type of entity and what type of operation been done here
            //example: (EntityType.COMPANY, CrudOp.CREATE)
            throw new RuntimeException("There was an issue with the query");
        }
         finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public Company read(final Long id) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) { throw new RuntimeException("failed to get connection to db"); }

        try {
            final String sqlStatement = "SELECT * FROM coupon_system.companies WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }
            return ObjectExtractor.companyFromResultSet(resultSet);
        } catch (final SQLException e) { throw new CrudException();}
            //need to add exception that will get what type of entity and what type of operation been done here
            //example: (EntityType.COMPANY, CrudOp.CREATE)
        finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public ArrayList<Company> readAll() throws CrudException, SQLException {
        Connection connection = null;
        final ArrayList<Company> companies = new ArrayList<Company>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            throw new RuntimeException("failed to get connection to db");
        }
        try {
            final String sqlStatement = "SELECT * FROM coupon_system.companies";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                companies.add(ObjectExtractor.companyFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return companies;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void delete(final Long id) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) { throw new RuntimeException("failed to get connection to db"); }

        try {
            final String sqlStatement = "DELETE FROM coupon_system.coupons WHERE ID = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
         finally {
        connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void update(Company company) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) { throw new RuntimeException("failed to get connection to db"); }
        try {
            final String sqlStatement = "UPDATE coupon_system.companies SET password = ? WHERE id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, company.getPassword());
            preparedStatement.setLong(2, company.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            connectionPool.getInstance().returnConnection(connection);
        }

    }
}
