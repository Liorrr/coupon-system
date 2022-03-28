package com.lior.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
/**
 * Coupon Java Bean - should include: id, companyID, amount, Category, title, description, price, startDate, endDate image.
 */
public class Coupon {
    private Long id;
    private int companyID;
    private int categoryID;
    private String title;
    private String description;
    private int amount;
    private double price;
    private Date startDate;
    private Date endDate;
    private String image;

    public Coupon(int companyID, int categoryID, String title, String description, int amount, double price, Date startDate, Date endDate, String image) {
        this.companyID = companyID;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyID=" + companyID +
                ", category=" + categoryID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id == coupon.id && companyID == coupon.companyID && amount == coupon.amount && Double.compare(coupon.price, price) == 0 && categoryID == coupon.categoryID && Objects.equals(title, coupon.title) && Objects.equals(description, coupon.description) && Objects.equals(startDate, coupon.startDate) && Objects.equals(endDate, coupon.endDate) && Objects.equals(image, coupon.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyID, categoryID, title, description, amount, price, startDate, endDate, image);
    }

}
