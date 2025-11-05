package com.adhd.ad_hell.domain.advertise.command.domain.aggregate;

import com.adhd.ad_hell.domain.board.command.domain.aggregate.Board;
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


    // 수정한 부분
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")  // FK 컬럼명 유지
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    private Long rewardId;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
    private String filePath;
    private String fileName;
    private String originFileName;

    @Builder
    private AdFile(String fileName, FileType fileType, String filePath) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public static AdFile of(String fileName, String originFileName, String filePath) {
        AdFile f = new AdFile();
        f.fileName = fileName;
        f.originFileName = originFileName;
        f.filePath = filePath;
        return f;
    }


    public void changeAdUrl(String filePath) {
        this.filePath = filePath;
    }

}