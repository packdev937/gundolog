package com.gundolog.entity;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_at")
    private LocalDateTime createdDate;
    @Column(name = "updated_at")
    private LocalDateTime updatedDate;
    private String title;
    private String content;

}
