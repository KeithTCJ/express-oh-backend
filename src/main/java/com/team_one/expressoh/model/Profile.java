package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Profile {

    // The users id here in profile matches that from users table
    // User of id:1, his profile is userId:1
    @Id
    @Column(name = "users_id")
    private Integer usersId;

    // This is required - otherwise, when a Users instance is created
    // JPA thinks the user's profile already exist when it isn't yet
    @Version
    private Integer version;

    @NotBlank(message = "First name cannot be blank.")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;

   // Bidirectional association (to navigate from Profile to User)
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId                         // Indicates that primary key of this entity (usersId) is populated by primary key of associated entity (Users)
    @JoinColumn(name = "users_id")  // The foreign key column in the 'profile' table
    private Users users;

    @Column
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDateTime;

    @Column
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastModifiedDateTime;

    private String phone;
    private String address;
    private String cardName;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;

    // Default constructor required by JPA
    public Profile() {
    }

    public Profile(String firstName, String lastName, String phone, String address,
                   String cardName, String cardNumber, String cardExpiry, String cardCvv) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCvv = cardCvv;
    }

    public Integer getUsersId() {
        return usersId;
    }

    public void setUsersId(Integer usersId) {
        this.usersId = usersId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Integer getVersion() {               // add getter for version
        return version;
    }

    public void setVersion(Integer version) {   // setter is optional, JPA manages it, but added for completeness
        this.version = version;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }
}