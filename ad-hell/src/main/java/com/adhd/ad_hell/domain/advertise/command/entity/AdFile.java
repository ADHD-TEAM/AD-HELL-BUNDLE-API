package com.adhd.ad_hell.domain.advertise.command.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="ad_file")
public class AdFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private Long boardId;
    private Long adId;
    private Long rewardId;

    @Column(nullable = false, length = 200)
    private String fileTitle;
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    private String filePath;
}
