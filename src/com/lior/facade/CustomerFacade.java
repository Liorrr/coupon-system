package com.lior.facade;

import com.lior.dao.CouponDao;
import com.lior.dao.CustomerDoa;
import com.lior.exceptions.CrudException;
import com.lior.exceptions.LoginException;
import com.lior.model.Coupon;
import com.lior.model.Customer;
import com.lior.model.enums.ClientType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerFacade extends UserFacade {
    private static final CustomerDoa customerDoa = CustomerDoa.instance;
    private static final CouponDao couponDao = CouponDao.instance;

    public static final CustomerFacade instance = new CustomerFacade();
    /*
    Login method that verify usertype is customer, email exist in the system, create an object of the customer
    Run hashcode on the password string from input of login process and compare with password int in DB\entity
    */
    @Override
    public boolean login(String email, String password, ClientType clientType) throws LoginException, CrudException, SQLException {
        if (!clientType.equals(ClientType.CUSTOMER) && !customerDoa.isExists(email)) {
            System.out.println("email doesn't exist, check address and try again OR create an account.");
            return false;
        }
        Customer customer = customerDAO.readByEmail(email);
        if (password.hashCode() == customer.getPassword()){
            return true;
        }
        else {
            System.out.println("email or password are incorrect");
            return false;
        }
    }

    /*
    Purchase coupon:
    Verify the customer doesn't have the coupon.
    Verify the coupon amount is not zero.
    Verify the coupon isn't expired
    Upon purchase → Update coupon amount -1.
    */
    public void purchaseCoupon(final long customerId, final long couponId) throws SQLException, CrudException, SQLException {
        if (!couponDao.couponPurchaseHistory(couponId).contains(customerId) &&
                couponDao.read(couponId).getAmount() > 0 &&
                couponDao.read(couponId).getEndDate().toLocalDate().isAfter(LocalDate.now())) {
                    //if all 3 conditions are true: create purchase.
                    couponDao.addPurchase(customerId,couponId);
                    //then get all coupon details for update function, and update amount to be minus 1.
                    Coupon coupon = couponDao.read(couponId);
                    coupon.setAmount(coupon.getAmount()-1);
                    couponDao.update(coupon);
                    System.out.println("Purchase complete: Customer id `"+customerId+ "` have purchase coupon id: `"+couponId+"`");
        }
        else {
            System.out.println("Coupon isn't valid, cannot complete purchase.");
        }
    }

    /*
    Return all customer's coupons.
    */
    public ArrayList<Coupon> getCustomerCoupons(Customer customer) throws CrudException, SQLException {
        return couponDao.readAllFromCustomer(customer.getId());
    }

    /*
    Return all customer's coupons from category id.
    */
    public ArrayList<Coupon> getCustomerCoupons(final long customerId, final long categoryId) throws CrudException, SQLException {
        return couponDao.readCustomerCouponsByCategory(customerId ,categoryId);
    }

    /*
    Return all customer's coupons price is lower than input amount. X<Price.
    */
    public ArrayList<Coupon> getCustomerCoupons(final long categoryId, final double maxAmount) throws CrudException, SQLException {
        return couponDao.readCustomerCouponsByMaxPrice(categoryId,maxAmount);
    }

    /*
    Return customer's details by id.
    */
    public Customer getCustomerDetails(final Long id) throws CrudException, SQLException {
        return customerDoa.read(id);
    }
}
