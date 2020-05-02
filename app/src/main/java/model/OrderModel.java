package model;

public class OrderModel {
    public String order_date;
    public String total_price;
    public String used_mileage;
    public String total_pay;
    public String payment_plan;


    public OrderModel(String order_date, String total_price, String used_mileage, String total_pay, String payment_plan) {
        this.order_date = order_date;
        this.total_price = total_price;
        this.total_pay = total_pay;
        this.payment_plan = payment_plan;
        this.used_mileage = used_mileage;
    }

    public OrderModel() {
    }
}
