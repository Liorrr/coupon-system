package com.lior.dao;

import com.lior.exceptions.CrudException;
import com.lior.model.Coupon;
import com.lior.util.ObjectExtractor;
import com.lior.util.db.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponDao implements CrudDao<Long, Coupon> {
    public static final CouponDao instance = new CouponDao();
    private ConnectionPool connectionPool = null;

    //create a coupon.. I got nothing to add to it. whop whop!
    @Override
    public Long create(final Coupon coupon) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "INSERT INTO `coupon_system`.`coupons`(`company_id`,`category_id`,`title`,`description`,`start_date`,`end_date`,`amount`,`price`,`image`) VALUES(?,?,?,?,?,?,?,?,?)";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, coupon.getCompanyID());
            preparedStatement.setInt(2, coupon.getCategoryID());
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, coupon.getStartDate());
            preparedStatement.setDate(6, coupon.getEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.execute();
            final ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                return null;
            }
            return resultSet.getLong(1);

        } catch (final SQLException e) {
            //need to add exception that will get what type of entity and what type of operation been done here
            //example: (EntityType.COMPANY, CrudOp.CREATE)
            throw new RuntimeException("There was an issue with the query");
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public Coupon read(final Long id) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            String sqlStatement = "SELECT  * FROM `coupon_system`.`coupons` WHERE `id` = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return ObjectExtractor.couponFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public ArrayList<Coupon> readAll() throws CrudException, SQLException {
        Connection connection = null;
        final ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            String sqlStatement = "SELECT  * FROM `coupon_system`.`coupons`";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void delete(final Long id) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "DELETE FROM `coupon_system`.`coupons` WHERE id = ?;";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    //
    @Override
    public void update(Coupon coupon) throws CrudException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "UPDATE `coupon_system`.`coupons` SET `category_id` = ?, `title` = ?, `description` = ?, `start_date` = ?, `end_date` = ?, `amount` = ?, `price` = ?, `image` = ? WHERE `id` = ?;";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, coupon.getCategoryID());
            preparedStatement.setString(2, coupon.getTitle());
            preparedStatement.setString(3, coupon.getDescription());
            preparedStatement.setDate(4, coupon.getStartDate());
            preparedStatement.setDate(5, coupon.getEndDate());
            preparedStatement.setInt(6, coupon.getAmount());
            preparedStatement.setDouble(7, coupon.getPrice());
            preparedStatement.setString(8, coupon.getImage());
            preparedStatement.setLong(9, coupon.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    // read all coupons by company id
    public ArrayList<Coupon> readAllFromCompany(final long id) throws CrudException, SQLException {
        Connection connection = null;
        ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT coupons.* FROM coupon_system.coupons WHERE company_id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public ArrayList<Coupon> readFromCompanyByCategory(final long companyId, final long categoryId) throws CrudException, SQLException {
        Connection connection = null;
        ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT coupons.* FROM coupon_system.coupons WHERE company_id = ? AND category_id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, companyId);
            preparedStatement.setLong(2, categoryId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }


    public ArrayList<Coupon> readAllFromCustomer(final long id) throws CrudException, SQLException {
        Connection connection = null;
        ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT `coupons`.*\n" +
                    "FROM `coupon_system`.`coupons`, `coupon_system`.`customers_vs_coupons`\n" +
                    "WHERE  `coupon_system`.`coupons`.`id` = `coupon_system`.`customers_vs_coupons`.`coupon_id`\n" +
                    "AND `coupon_system`.`customers_vs_coupons`.`customer_id`=  ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    //get coupon id and return list of clients id that purchased the coupon.
    public ArrayList<Long> couponPurchaseHistory(long id) throws SQLException {
        Connection connection = null;
        ArrayList<Long> clients = new ArrayList<Long>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT customer_id FROM customers_vs_coupons where coupon_id = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clients.add(resultSet.getLong(1));
            }
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        try {
            return clients;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public void deleteAllPurchasesByCompany(final long id) throws SQLException {
        Connection connection = null;
        List<Coupon> coupons = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "DELETE FROM customers_vs_coupons WHERE coupon_id in (SELECT id FROM coupons WHERE company_id = ?);";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public void deleteAllPurchasesByCustomer(final long id) throws SQLException {
        Connection connection = null;
        List<Coupon> coupons = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "DELETE FROM customers_vs_coupons WHERE customer_id = ?;";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }


    public void deleteAllPurchasesByCoupon(final long id) throws SQLException {
        Connection connection = null;
        List<Coupon> coupons = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "DELETE FROM customers_vs_coupons WHERE coupon_id = ?;";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public void deleteAllCouponsByCompany(final long id) throws SQLException {
        Connection connection = null;
        List<Coupon> coupons = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "DELETE FROM coupons WHERE company_id = ?;";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public Boolean isExist(final long id, final String title) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT COUNT(title) AS isExist FROM coupon_system.coupons WHERE company_id = ? AND title = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, title);
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
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }


    public ArrayList<Coupon> readFromCompanyMaxPrice(final long companyId, final double price) throws SQLException {
        Connection connection = null;
        ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT coupons.* FROM coupon_system.coupons WHERE company_id = ? AND price <=?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, companyId);
            preparedStatement.setDouble(2, price);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return coupons;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public void addPurchase(final long customerId, final long couponId) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "INSERT INTO `coupon_system`.`customers_vs_coupons` (`customer_id`, `coupon_id`) VALUES (?,?);";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1,customerId);
            preparedStatement.setLong(2,couponId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }

    }


    public ArrayList<Coupon> readCustomerCouponsByMaxPrice(final long customerId, final double maxPrice) throws CrudException, SQLException {
        Connection connection = null;
        ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT `coupons`.* FROM `coupon_system`.`coupons`, `coupon_system`.`customers_vs_coupons` WHERE `coupon_system`.`customers_vs_coupons`.`coupon_id` = `coupon_system`.`coupons`.`id` AND `coupon_system`.`customers_vs_coupons`.`customer_id` =  ? AND `coupon_system`.`coupons`.`price` >= ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setDouble(2, maxPrice);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }


    public ArrayList<Coupon> readCustomerCouponsByCategory(final long customerId, final long categoryId) throws CrudException, SQLException {
        Connection connection = null;
        ArrayList<Coupon> coupons = new ArrayList<Coupon>();
        try {
            connection = connectionPool.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        try {
            final String sqlStatement = "SELECT `coupons`.* FROM `coupon_system`.`coupons`, `coupon_system`.`customers_vs_coupons` WHERE `coupon_system`.`customers_vs_coupons`.`coupon_id` = `coupon_system`.`coupons`.`id` AND `coupon_system`.`customers_vs_coupons`.`customer_id` =  ? AND `coupon_system`.`coupons`.`category_id` = ?";
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, categoryId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {
                coupons.add(ObjectExtractor.couponFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return coupons;
        } finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

}

