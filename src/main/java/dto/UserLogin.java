package dto;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {
    private String email;
    private String password;
    private String name;

    public UserLogin(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserLogin() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}