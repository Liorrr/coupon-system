package com.lior.jobs;

import com.lior.dao.CouponDao;
import com.lior.exceptions.CrudException;
import com.lior.model.Coupon;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

//daily job that will delete expired coupons
//deleting coupon require deleting purchase history as well.

@RequiredArgsConstructor
public class CouponExpirationDailyJob implements Runnable {
    private CouponDao couponDao;
    // 86400000 is day in milliseconds, if job running fast enough it should work daily.
    // to improve: check how to implement fixed execution time for not 'dragging' the execution time little by little.
    private final int SLEEP = 86400000;

    @Override
    public void run() {
        try  {
            while(true){
                ArrayList<Coupon> coupons = couponDao.readAll();
                System.out.println("Job: created list of available coupons");
                for (Coupon coupon: coupons){
                    System.out.println("Checking coupon id: "+coupon.getId());
                    //if expiration time is after now then delete
                    if (coupon.getEndDate().toLocalDate().isAfter(LocalDate.now())){
                        System.out.println("Deleting coupon id: "+coupon.getId());
                        couponDao.deleteAllPurchasesByCoupon(coupon.getId());
                        System.out.println("Deleted purchase history for coupon id: "+coupon.getId());
                        couponDao.delete(coupon.getId());
                        System.out.println("Deleted coupon id: "+coupon.getId());
                    }
                }
                Thread.sleep(SLEEP);
            }
        } catch (CrudException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }

}
