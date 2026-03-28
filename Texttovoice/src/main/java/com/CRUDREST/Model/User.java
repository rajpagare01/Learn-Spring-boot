package com.CRUDREST.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Entity(name = "userCRUD")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NonNull
    private  String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String emailId;
    @NonNull
    private Date createdAt;
    @NonNull
    private String createdBy;
    @NonNull
    private Date updatedAt;
    @NonNull
    private String updatedby;

}
