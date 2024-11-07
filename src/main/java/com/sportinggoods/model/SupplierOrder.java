package src.main.java.com.sportinggoods.model;

import java.time.LocalDate;

public class SupplierOrder {
    private String orderId;
    private String supplierId;
    private String productDetails;
    private int quantity;
    private double totalPrice;
    private LocalDate orderDate;
    private String status; // Pending, Confirmed, Delivered

    // Constructors
    public SupplierOrder(String orderId, String supplierId, String productDetails, int quantity, double totalPrice, LocalDate orderDate, String status) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.productDetails = productDetails;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public SupplierOrder() {}

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
	
	public double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toCSV method for CSV representation
    public String toCSV() {
        return orderId + "," + supplierId + "," + productDetails + "," + quantity + "," + totalPrice + "," + orderDate + "," + status;
    }

    // Create SupplierOrder from CSV
    public static SupplierOrder fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 7) {
            return null;
        }
        return new SupplierOrder(
            tokens[0],
            tokens[1],
            tokens[2],
            Integer.parseInt(tokens[3]),
            Double.parseDouble(tokens[4]),
            LocalDate.parse(tokens[5]),
            tokens[6]
        );
    }
}
