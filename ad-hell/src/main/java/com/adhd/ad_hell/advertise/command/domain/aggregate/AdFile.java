package com.adhd.ad_hell.advertise.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="ad_file")
public class AdFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    private Long rewardId;

    @Column(nullable = false, length = 200)
    private String fileTitle;
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    private String filePath;
    private String fileUrl;

    @Builder
    private AdFile(String fileTitle,
                   FileType fileType, String filePath) {
        this.fileTitle = fileTitle;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public static AdFile of(String adUrl, String fileName) {
        AdFile f = new AdFile();
        f.fileUrl = adUrl;
        f.fileTitle = fileName;
        return f;
    }


    public void changeAdUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
