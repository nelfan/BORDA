package com.softserve.borda.dto;


import java.util.Arrays;

public class UpdateUserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private byte[] avatar;

    public UpdateUserDTO(String username, String email, String firstName, String lastName, byte[] avatar) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public UpdateUserDTO() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UpdateUserDTO{");
        sb.append("username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", avatar=").append(Arrays.toString(avatar));
        sb.append('}');
        return sb.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
