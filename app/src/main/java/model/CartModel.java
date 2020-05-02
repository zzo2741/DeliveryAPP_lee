package model;

public class CartModel {
    public String product_name;
    public String product_price;
    public String product_count;
    public String total_price;

    public CartModel(String product_name, String product_price, String product_count, String total_price) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_count = product_count;
        this.total_price = total_price;
    }

    public CartModel() {
    }
}
