package com.lms.lmssystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    @Column(updatable = false)
    private LocalDateTime createTime;
    @Column
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate(){
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateTime = LocalDateTime.now();
    }
}
