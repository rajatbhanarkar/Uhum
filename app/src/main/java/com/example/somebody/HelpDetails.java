package com.example.somebody;

public class HelpDetails {
    private String UserID, HelpPicLink, HelpContact, HelpAddress, Category, Description;
    int Nop;

    public HelpDetails() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getHelpPicLink() {
        return HelpPicLink;
    }

    public void setHelpPicLink(String helpPicLink) {
        HelpPicLink = helpPicLink;
    }

    public String getHelpContact() {
        return HelpContact;
    }

    public void setHelpContact(String helpContact) {
        HelpContact = helpContact;
    }

    public String getHelpAddress() {
        return HelpAddress;
    }

    public void setHelpAddress(String helpAddress) {
        HelpAddress = helpAddress;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getNop() {
        return Nop;
    }

    public void setNop(int nop) {
        Nop = nop;
    }
}
