package rs.ac.uns.ftn.svt.dto;

public class UserTypeResponse {
    private String userType;

    public UserTypeResponse() {
    }

    public UserTypeResponse(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
