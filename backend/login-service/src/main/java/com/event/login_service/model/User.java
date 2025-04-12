package com.event.login_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String fullName;
    
    @Column(unique = true)
    private String email;
    
    private String password;
    
    private String mobile;
    
    @Enumerated(EnumType.STRING)
    private Role role;
}