package com.lms.lmssystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String content;
    private int lessonOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
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
