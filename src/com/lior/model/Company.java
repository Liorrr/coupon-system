package com.lior.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
/**
 * Company Java Bean - should include: id, name, email, password, coupon array.
 */
public class Company {
    private Long id;
    private String name;
    private String email;
    private int password;
    private ArrayList<Coupon> coupons = new ArrayList<Coupon>();

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password.hashCode();

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPassword() {
        return password;
    }

    public void setNewPassword(String password) {
        this.password = password.hashCode();
    }

    public void setPassword(int password) { this.password = password; }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }
}
