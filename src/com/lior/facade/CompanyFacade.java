package com.lior.facade;

import com.lior.dao.CompanyDao;
import com.lior.dao.CouponDao;
import com.lior.exceptions.CrudException;
import com.lior.exceptions.LoginException;
import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.model.Company;
import com.lior.model.Coupon;
import com.lior.model.enums.ClientType;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.ArrayList;

@RequiredArgsConstructor
public class CompanyFacade extends UserFacade {
    public static final CompanyDao companyDAO = CompanyDao.instance;
    public static final CouponDao couponDao = CouponDao.instance;

    /*
    Login method that verify usertype is company, email exist in the system, create an object of the company
    Run hashcode on the password string from input of login process and compare with password int in DB\entity
    */
    @Override
    public boolean login(String email, String password, ClientType clientType) throws LoginException, CrudException {
        Company company = null;
        if (!clientType.equals(ClientType.COMPANY) && !companyDAO.isExists(email)) {
            return false;
        }
        company = companyDAO.readByEmail(email);
        if (password.hashCode() == company.getPassword()) {
            return true;
        }
        return false;
    }

    //add coupon only if title not already exist under the same  company id
    public void addCoupon(Company company,Coupon coupon) throws SQLException, CrudException {
        if(couponDao.isExist(company.getId(),coupon.getTitle())) {
            System.out.println("Coupon title already exist. try another one.");
        }
        else {
            couponDao.create(coupon);
        }
        return;
    }

    //update coupon - cannot change id & company id
    public void updateCoupon(Coupon coupon) throws CrudException {
        couponDao.update(coupon);
    }

    //delete coupon and coupon history;
    public void deleteCoupon(Coupon coupon) throws CrudException {
        couponDao.deleteAllPurchasesByCoupon(coupon.getId());
        couponDao.delete(coupon.getId());
    }

    //get all coupons by companyId
    public ArrayList<Coupon> getAllCoupons(final long companyId) throws CrudException {
        return couponDao.readAllFromCompany(companyId);
    }

    //get all coupons by company id and category id
    public ArrayList<Coupon> getAllCoupons(final Long companyId,final long categoryId) throws CrudException {
        return couponDao.readFromCompanyByCategory(companyId,categoryId);
    }

    //get all coupons by company id and max price
    public ArrayList<Coupon> getAllCoupons(final Long companyId,final double maxPrice) throws CrudException {
        return couponDao.readFromCompanyMaxPrice(companyId,maxPrice);
    }

    //get all company details by id
    public Company getCompanyById(Long id) throws MissingGeneratedKeysException, Exception {
        return companyDAO.read(id);
    }

    //


}
