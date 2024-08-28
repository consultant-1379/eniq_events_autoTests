package com.ericsson.eniq.events.ui.selenium.events.login;

public class UserInfo {
   
	 public UserInfo() {
	    }

	    public UserInfo(final String userID, final String password, final String confirmpassword, final String firstName, final String lastName,
	            final String email, final String phone, final String organization, final String roleNames) {
	        super();
	        this.userID = userID;
	        this.password = password;
	        this.confirmPassword = confirmpassword;
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.email = email;
	        this.phone = phone;
	        this.organization = organization;
	        setRoles(roleNames);
	    }

	    @Override
	    public String toString() {

	        return "UserID=" + userID + " Password=" + password + "ConfirmPassword=" +confirmPassword + " FirstName=" + firstName + " LastName=" + lastName
	                + " Email=" + email + " Phone=" + phone + " Organization=" + organization + " Roles="
	                + getRolesAsString();
	    }

	    public String[] getRoles() {
	        return roles;
	    }

	    public String getRolesAsString() {
	        if (roles == null)
	            return null;
	        String roleString = "";
	        boolean firstElement = true;
	        for (final String roleElement : roles) {
	            if (firstElement) {
	                roleString = roleString + roleElement;
	                firstElement = false;
	                continue;
	            }
	            roleString = roleString + "," + roleElement;

	        }
	        return roleString;
	    }

	    public void setRoles(final String roleNames) {
	        roles = roleNames.split(",");
	    }

	    public String getUserID() {
	        return userID;
	    }

	    public void setUserID(final String userID) {
	        this.userID = userID;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(final String password) {
	        this.password = password;
	    }

	    public String getConfirmPassword() {
	        return confirmPassword;
	    }

	    public void setConfirmPassword(final String confirmPassword) {
	        this.confirmPassword = confirmPassword;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(final String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(final String lastName) {
	        this.lastName = lastName;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(final String email) {
	        this.email = email;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(final String phone) {
	        this.phone = phone;
	    }

	    public String getOrganization() {
	        return organization;
	    }

	    public void setOrganization(final String organization) {
	        this.organization = organization;
	    }

	    private String userID;

	    private String password;

	    private String confirmPassword;

	    private String firstName;

	    private String lastName;

	    private String email;

	    private String phone;

	    private String organization;

	    private String[] roles;

	}

	

