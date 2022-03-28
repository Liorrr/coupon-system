package com.lior.util.db;

import com.lior.util.Texts;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseStarter {
    private static ConnectionPool connectionPool = null;

    //create all tables without parameters
    public static void createTables() throws SQLException {
        Connection connection = null;
        try{
            System.out.println("Trying to create tables");
            connection = connectionPool.getInstance().getConnection();
            connection.prepareStatement(
                    "CREATE TABLE `coupon_system`.`companies` (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  `email` VARCHAR(45) NOT NULL,\n" +
                            "  `password` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                            "  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,\n" +
                            "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);").execute();
            System.out.println("Created table: companies");
            connection.prepareStatement(
                            "CREATE TABLE `coupon_system`.`customers` (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `first_name` VARCHAR(45) NOT NULL,\n" +
                            "  `last_name` VARCHAR(45) NOT NULL,\n" +
                            "  `email` VARCHAR(45) NOT NULL,\n" +
                            "  `password` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                            "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);").execute();
            System.out.println("Created table: customers");
            connection.prepareStatement(
                            "CREATE TABLE `coupon_system`.`categories` (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `name` VARCHAR(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);").execute();
            System.out.println("Created table: categories");
            connection.prepareStatement(
                            "CREATE TABLE `coupon_system`.`coupons` (\n" +
                            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                            "  `company_id` INT NOT NULL,\n" +
                            "  `category_id` INT NOT NULL,\n" +
                            "  `title` VARCHAR(45) NOT NULL,\n" +
                            "  `description` VARCHAR(45) NULL,\n" +
                            "  `start_date` DATETIME NOT NULL,\n" +
                            "  `end_date` DATETIME NOT NULL,\n" +
                            "  `amount` INT NULL,\n" +
                            "  `price` DOUBLE NOT NULL,\n" +
                            "  `image` VARCHAR(45) NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                            "  INDEX `id_idx1` (`company_id` ASC) VISIBLE,\n" +
                            "  INDEX `category_id_idx` (`category_id` ASC) VISIBLE,\n" +
                            "  CONSTRAINT `category_id`\n" +
                            "    FOREIGN KEY (`category_id`)\n" +
                            "    REFERENCES `coupon_system`.`categories` (`id`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION,\n" +
                            "  CONSTRAINT `company_id`\n" +
                            "    FOREIGN KEY (`company_id`)\n" +
                            "    REFERENCES `coupon_system`.`companies` (`id`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION);").execute();
            System.out.println("Created table: coupons");
            connection.prepareStatement(
                            "CREATE TABLE `coupon_system`.`customers_vs_coupons` (\n" +
                            "  `customer_id` INT NOT NULL,\n" +
                            "  `coupon_id` INT NOT NULL,\n" +
                            "  INDEX `customer_id_idx` (`customer_id` ASC) VISIBLE,\n" +
                            "  INDEX `coupon_id_idx` (`coupon_id` ASC) VISIBLE,\n" +
                            "  CONSTRAINT `customer_id`\n" +
                            "    FOREIGN KEY (`customer_id`)\n" +
                            "    REFERENCES `coupon_system`.`customers` (`id`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION,\n" +
                            "  CONSTRAINT `coupon_id`\n" +
                            "    FOREIGN KEY (`coupon_id`)\n" +
                            "    REFERENCES `coupon_system`.`coupons` (`id`)\n" +
                            "    ON DELETE NO ACTION\n" +
                            "    ON UPDATE NO ACTION);").execute();
            System.out.println("Created table: customers vs coupons");
            System.out.println(Texts.A+"All tables been created"+Texts.B);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }   finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    //drop tables
    public static void dropTables() throws SQLException {
        Connection connection = null;
        try{
            System.out.println("Trying to drop tables");
            connection = connectionPool.getInstance().getConnection();
            connection.prepareStatement(
                    "DROP TABLE " +
                            "`coupon_system`.`customers_vs_coupons`," +
                            "`coupon_system`.`coupons`," +
                            "`coupon_system`.`companies`," +
                            "`coupon_system`.`categories`," +
                            "`coupon_system`.`customers` ;").execute();
            System.out.println(Texts.A+"all tables been dropped"+Texts.B);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }   finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    //populate categories with values
    public static void populateCategories() throws SQLException {
        Connection connection = null;
        try{
            System.out.println("Trying to populate categories");
            connection = connectionPool.getInstance().getConnection();
            connection.prepareStatement(
                    "INSERT INTO `coupon_system`.`categories`(`name`)VALUES(\"Food\");").execute();
            connection.prepareStatement(
                    "INSERT INTO `coupon_system`.`categories`(`name`)VALUES(\"Electricity\");").execute();
            connection.prepareStatement(
                    "INSERT INTO `coupon_system`.`categories`(`name`)VALUES(\"Restaurant\");").execute();
            connection.prepareStatement(
                    "INSERT INTO `coupon_system`.`categories`(`name`)VALUES(\"Vacation\");").execute();
            connection.prepareStatement(
                    "INSERT INTO `coupon_system`.`categories`(`name`)VALUES(\"Home\");").execute();
            connection.prepareStatement(
                    "INSERT INTO `coupon_system`.`categories`(`name`)VALUES(\"Toys\");").execute();
            System.out.println(Texts.A+"All categories been created"+Texts.B);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }   finally {
            connectionPool.getInstance().returnConnection(connection);
        }
    }

    public static void restartDB() throws SQLException {
        System.out.println(Texts.A+"Restarting DB: will perform drop tables, create tables, and populate categories."+Texts.B);
        dropTables();
        createTables();
        populateCategories();
    }


}
