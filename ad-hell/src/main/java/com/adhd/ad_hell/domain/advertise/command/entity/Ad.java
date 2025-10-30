package com.adhd.ad_hell.domain.advertise.command.entity;


import com.adhd.ad_hell.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="ad")
public class Ad extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long categoryId;
    @Column(nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.DEACTIVATED;

    private int like_count;

    private int bookmark_count;

    private int comment_count;

    private int view_count;
}
