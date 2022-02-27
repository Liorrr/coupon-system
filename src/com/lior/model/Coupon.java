package com.lior.model;

import com.lior.model.enums.Category;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;


@NoArgsConstructor
/**
 * Coupon Java Bean - should include: id, companyID, amount, Category, title, description, price, startDate, endDate image.
 */
public class Coupon {
    private Long id;
    private int companyID;
    private Category category;
    private String title;
    private String description;
    private int amount;
    private double price;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private String image;

    public Coupon(int id, int companyID, Category category, String title, String description, int amount, double price, LocalDate startDate, LocalDate endDate, String image) {
        this.id = id;
        this.companyID = companyID;
        this.category = category;
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
                ", category=" + category +
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
        return id == coupon.id && companyID == coupon.companyID && amount == coupon.amount && Double.compare(coupon.price, price) == 0 && category == coupon.category && Objects.equals(title, coupon.title) && Objects.equals(description, coupon.description) && Objects.equals(startDate, coupon.startDate) && Objects.equals(endDate, coupon.endDate) && Objects.equals(image, coupon.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyID, category, title, description, amount, price, startDate, endDate, image);
    }

    public int getId() {
        return id;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Date getDateStartDate() {
        return Date.valueOf(startDate);
    }

    public void setDateEndDate(Date endDate) {
        this.endDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setDateStartDate(Date startDate) {
        this.startDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Date getDateEndDate() {
        return Date.valueOf(endDate);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategoryID() {
        return Category.valueOf(String.valueOf(category)).ordinal();
    }

    public void setId(int id) { this.id = id;
    }
}


