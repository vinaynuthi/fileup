package com.example.entity;

   import jakarta.persistence.*;

   @Entity
   @Table(name = "users")
   public class User {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;

       @Column(name = "username")
       private String username;

       @Column(name = "email")
       private String email;

       // Constructors
       public User() {}

       public User(String username, String email) {
           this.username = username;
           this.email = email;
       }

       // Getters and Setters
       public Long getId() { return id; }
       public void setId(Long id) { this.id = id; }

       public String getUsername() { return username; }
       public void setUsername(String username) { this.username = username; }

       public String getEmail() { return email; }
       public void setEmail(String email) { this.email = email; }
   }