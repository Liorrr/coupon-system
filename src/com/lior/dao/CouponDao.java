package com.lior.dao;

import com.lior.model.Coupon;
import com.lior.util.db.JDBCUtil;

import java.sql.*;

public class CouponDao {
    public long createCoupon(Coupon coupon) throws Exception {
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();

            String sqlStatement = "INSERT INTO `coupon_system`.`coupons`(`company_id`,`category_id`,`title`,`description`,`start_date`,`end_date`,`amount`,`price`,`image`) VALUES(?,?,?,?,?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, coupon.getCompanyID());
            preparedStatement.setInt(2, coupon.getCategoryID());
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, coupon.getDateStartDate());
            preparedStatement.setDate(6, coupon.getDateEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (!generatedKeys.next()) {
                throw new Exception();
            }

            return generatedKeys.getLong(1);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Failed to created a new coupon " + coupon, e);
        } finally {
            JDBCUtil.closeResources(connection, preparedStatement);
        }
    }

    //update coupon by couponId
    public void updateCoupon(Coupon coupon) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();

            String sqlStatement = "UPDATE `coupon_system`.`coupons` SET `company_id` = ?, `category_id` = ?, `title` = ?, `description` = ?, `start_date` = ?, `end_date` = ?, `amount` = ?, `price` = ?, `image` = ? WHERE `id` = ?;";

            preparedStatement = connection.prepareStatement(sqlStatement);

            preparedStatement.setInt(1, coupon.getCompanyID());
            preparedStatement.setInt(2, coupon.getCategoryID());
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5, coupon.getDateStartDate());
            preparedStatement.setDate(6, coupon.getDateEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.setLong(10, coupon.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResources(connection, preparedStatement);
        }
    }

    //delete coupon by couponId
    public void deleteCoupon(Coupon coupon) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            String sqlStatement = "DELETE FROM `coupon_system`.`coupons` WHERE id = ?;";
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, coupon.getId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            JDBCUtil.closeResources(connection, preparedStatement);
        }
    }

    //Get coupon by couponId
    public Coupon getCoupon (long couponId) throws Exception {
        Coupon coupon = new Coupon();
        

        return coupon;
    }

}

