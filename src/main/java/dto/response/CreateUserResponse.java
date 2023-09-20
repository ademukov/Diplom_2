package dto.response;

public class CreateUserResponse {
    private Boolean success;
    private CreateUserInfo user;
    private String accessToken;
    private String refreshToken;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public CreateUserInfo getUser() {
        return user;
    }

    public void setUser(CreateUserInfo user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
