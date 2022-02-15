package com.lior.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
/**
 * Company Java Bean - should include: id, name, email, password, coupon array.
 */
public class Company {
    private final int id;
    private String name, email, password;
    private List<Coupon> coupons = new ArrayList<Coupon>();

    public Company(int id, String name, String email, String password, List<Coupon> coupons) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
