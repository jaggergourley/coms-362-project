package com.sportinggoods.model;

public class Supplier {
    private String supplierId;
    private String name;
    private String contactInfo;
    private String relationshipStatus;
    private String followUpAction;

    // Constructors
    public Supplier(String supplierId, String name, String contactInfo, String relationshipStatus, String followUpAction) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.relationshipStatus = relationshipStatus;
        this.followUpAction = followUpAction;
    }

    public Supplier() {}

    // Getters and Setters
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getFollowUpAction() {
        return followUpAction;
    }

    public void setFollowUpAction(String followUpAction) {
        this.followUpAction = followUpAction;
    }

    // toCSV method for CSV representation
    public String toCSV() {
        return supplierId + "," + name + "," + contactInfo + "," + relationshipStatus + "," + followUpAction;
    }

    // Create Supplier from CSV
    public static Supplier fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",");
        if (tokens.length != 5) {
            return null;
        }
        return new Supplier(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
    }
}
