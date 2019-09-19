package com.example.updateEmail.entities;

import java.util.Objects;

public class User {

    private UserId userId;
    private EmailAddress emailAddress;

    public User(UserId userId, EmailAddress emailAddress) {
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(emailAddress, user.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, emailAddress);
    }
}
