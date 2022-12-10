package com.gg.sellers;

public class Post
{
    public Post()
    {

    }

    private String SellerID;
    private String ProductName;
    private String ProductQuantity;
    private String ProductQuality;
    private String ProductHarvestTime;
    private String ProductDispatchTime;
    private String ProductPrice;
    private String SellerPhoneNumber;
    private String SellerAddress;
    private String UploadDate;
    private String SellerName;

    //Constructor

    public Post(String sellerID, String productName, String productQuantity, String productQuality, String productHarvestTime, String productDispatchTime, String productPrice, String sellerPhoneNumber, String sellerAddress, String uploadDate, String sellerName) {
        SellerID = sellerID;
        ProductName = productName;
        ProductQuantity = productQuantity;
        ProductQuality = productQuality;
        ProductHarvestTime = productHarvestTime;
        ProductDispatchTime = productDispatchTime;
        ProductPrice = productPrice;
        SellerPhoneNumber = sellerPhoneNumber;
        SellerAddress = sellerAddress;
        UploadDate = uploadDate;
        SellerName = sellerName;
    }


    //Constructor

    //Getter & Setter
    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductQuality() {
        return ProductQuality;
    }

    public void setProductQuality(String productQuality) {
        ProductQuality = productQuality;
    }

    public String getProductHarvestTime() {
        return ProductHarvestTime;
    }

    public void setProductHarvestTime(String productHarvestTime) {
        ProductHarvestTime = productHarvestTime;
    }

    public String getProductDispatchTime() {
        return ProductDispatchTime;
    }

    public void setProductDispatchTime(String productDispatchTime) {
        ProductDispatchTime = productDispatchTime;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getSellerPhoneNumber() {
        return SellerPhoneNumber;
    }

    public void setSellerPhoneNumber(String sellerPhoneNumber) {
        SellerPhoneNumber = sellerPhoneNumber;
    }

    public String getSellerAddress() {
        return SellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        SellerAddress = sellerAddress;
    }

    public String getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    //Getter & Setter
}
