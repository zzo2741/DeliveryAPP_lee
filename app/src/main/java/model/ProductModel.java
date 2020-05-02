package model;

import java.io.Serializable;

public class ProductModel implements Serializable{

    public String product_name;
    public String product_price;
    public String product_description;

    public ProductModel(){

    }

    public ProductModel(String product_name, String product_price, String product_description){
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_description = product_description;
    }
}
