package com.lms.lmssystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private int chapterOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    @Column(updatable = false)
    private LocalDateTime createdTime;
    @Column
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate(){
        createdTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateTime = LocalDateTime.now();
    }

}
