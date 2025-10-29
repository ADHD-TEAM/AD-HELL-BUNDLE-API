package com.adhd.ad_hell.domain.advertise.command.entity;

import com.adhd.ad_hell.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="ad_comment")
public class AdComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adCommentId;

    private Long userId;
    private Long adId;
    private String content;
}
