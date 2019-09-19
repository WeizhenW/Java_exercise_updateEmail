package com.example.updateEmail.entities;

import java.util.Objects;

public class UserId {

    private int userId;

    public UserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId1 = (UserId) o;
        return userId == userId1.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
