package com.example.TheFit.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public abstract class User {
    @Column(nullable = false)
    public String name;
    @Column(unique = true, nullable = false)
    public String email;
//    @Column(nullable = false)
    public String password;
    public int cmHeight;
    public int kgWeight;
    @Column(unique = true, nullable = false)
    public String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Role role;
    public String profileImage;
    @Builder.Default
    public String delYn="N";
    @CreationTimestamp
    public LocalDateTime createdTime;
    @UpdateTimestamp
    public LocalDateTime updatedTime;
}
