package com.example.TheFit.user.dto;

import com.example.TheFit.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserIdPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Role role;
    public String delYn;

    public UserIdPassword(String email, String password,String name,Role role) {
        this.email = email;
        this.password =password;
        this.name = name;
        this.role = role;
        this.delYn = "N";
    }
    public void delete(){
        this.delYn = "Y";
    }
}
