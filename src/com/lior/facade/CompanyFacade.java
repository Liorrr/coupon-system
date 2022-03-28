package com.lior.facade;

import com.lior.dao.CompanyDao;
import com.lior.dao.CouponDao;
import com.lior.exceptions.CrudException;
import com.lior.exceptions.LoginException;
import com.lior.exceptions.MissingGeneratedKeysException;
import com.lior.model.Company;
import com.lior.model.Coupon;
import com.lior.model.enums.ClientType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.ArrayList;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyFacade extends UserFacade {
    public static final CompanyDao companyDAO = CompanyDao.instance;
    public static final CouponDao couponDao = CouponDao.instance;

    public static final CompanyFacade instance = new CompanyFacade();

    /*
    Login method that verify usertype is company, email exist in the system, create an object of the company
    Run hashcode on the password string from input of login process and compare with password int in DB\entity
    */
    @Override
    public boolean login(String email, String password, ClientType clientType) throws LoginException, CrudException, SQLException {
        if (!clientType.equals(ClientType.COMPANY) && !companyDAO.isExists(email)) {
            System.out.println("");
            return false;
        }
        Company company = companyDAO.readByEmail(email);
        if (password.hashCode() == company.getPassword()) {
            return true;
        }
        else {
            System.out.println("email or password are incorrect");
            return false;
        }
    }

    //add coupon only if title not already exist under the same  company id
    public Long addCoupon(Company company,Coupon coupon) throws SQLException, CrudException, SQLException {
        if(couponDao.isExist(company.getId(),coupon.getTitle())) {
            System.out.println("Coupon title already exist. try another one.");
            return null;
        }
        else {
         return couponDao.create(coupon);
        }
    }

    //update coupon - cannot change id & company id
    public void updateCoupon(Coupon coupon) throws CrudException, SQLException {
        couponDao.update(coupon);
    }

    //delete coupon and coupon history;
    public void deleteCoupon(Coupon coupon) throws CrudException, SQLException {
        couponDao.deleteAllPurchasesByCoupon(coupon.getId());
        couponDao.delete(coupon.getId());
    }

    //get all coupons by companyId
    public ArrayList<Coupon> getAllCoupons(final long companyId) throws CrudException, SQLException {
        return couponDao.readAllFromCompany(companyId);
    }

    //get all coupons by company id and category id
    public ArrayList<Coupon> getAllCoupons(final Long companyId,final long categoryId) throws CrudException, SQLException {
        return couponDao.readFromCompanyByCategory(companyId,categoryId);
    }

    //get all coupons by company id and max price
    public ArrayList<Coupon> getAllCoupons(final Long companyId,final double maxPrice) throws CrudException, SQLException {
        return couponDao.readFromCompanyMaxPrice(companyId,maxPrice);
    }

    //get all company details by id including coupons (if available).
    public Company getCompanyById(Long id) throws MissingGeneratedKeysException, Exception {
        Company company = companyDAO.read(id);
        company.setCoupons(couponDao.readAllFromCompany(id));
        return company;
    }

    //


}
