package com.yan.webapp.dto;

import java.util.Date;
import java.util.Objects;

public class AccountDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Date accountCreated;
    private Date accountUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(Date accountCreated) {
        this.accountCreated = accountCreated;
    }

    public Date getAccountUpdated() {
        return accountUpdated;
    }

    public void setAccountUpdated(Date accountUpdated) {
        this.accountUpdated = accountUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(accountCreated, that.accountCreated) && Objects.equals(accountUpdated, that.accountUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, accountCreated, accountUpdated);
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountCreated=" + accountCreated +
                ", accountUpdated=" + accountUpdated +
                '}';
    }
}
