package com.example.mymall;

public class CartItemModel {

    public static final int CART_ITEM =0;
    public static final int TOTAL_AMOUNT =1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /////////cart item
    private String productID;
    private String productImage;
    private String productTitle;
    private Long freeCoupen;
    private String productprice;
    private String cuttedPrice;
    private Long productQuantity;
    private Long offerApplied;
    private Long coupenApplied;
    private boolean inStock;

    public CartItemModel(int type,String productID, String productImage, String productTitle, Long freeCoupen, String productprice, String cuttedPrice, Long productQuantity, Long offerApplied, Long coupenApplied,boolean inStock) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupen = freeCoupen;
        this.productprice = productprice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        this.offerApplied = offerApplied;
        this.coupenApplied = coupenApplied;
        this.inStock = inStock;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }
    public void setProductID(String productID) {
        this.productID = productID;
    }
    public String getProductImage() {
        return productImage;
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public Long getFreeCoupen() {
        return freeCoupen;
    }
    public void setFreeCoupen(Long freeCoupen) {
        this.freeCoupen = freeCoupen;
    }
    public String getProductprice() {
        return productprice;
    }
    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }
    public String getCuttedPrice() {
        return cuttedPrice;
    }
    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }
    public Long getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }
    public Long getOfferApplied() {
        return offerApplied;
    }
    public void setOfferApplied(Long offerApplied) {
        this.offerApplied = offerApplied;
    }
    public Long getCoupenApplied() {
        return coupenApplied;
    }
    public void setCoupenApplied(Long coupenApplied) {
        this.coupenApplied = coupenApplied;
    }

    /////////cart item

    /////////cart total
    private int totalItems,totalItemPrice,totalAmount,savedAmount;
    private String deliveryPrice;
    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(int totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
    /////////cart total
}
