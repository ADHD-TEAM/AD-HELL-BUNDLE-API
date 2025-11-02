package com.adhd.ad_hell.domain.board.command.domain.aggregate;

import com.adhd.ad_hell.common.BaseTimeEntity;
import com.adhd.ad_hell.domain.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.domain.user.command.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Table(name = "board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Column(name = "board_title", nullable = false)
    private String title;
    @Column(name = "board_content", nullable = false)
    private String content;
    @Column(name = "board_status", nullable = false)
    private String status = "Y";
    private String productImageUrl;
    // User FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User writer;

    // Category FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @Column(name = "image_url")
    private String ImageUrl;

    // 조회 수
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    // 조회 수 증가 메서드
    public void increaseViewCount() {
        this.viewCount++;
    }

    // 이미지 URL 수정 (업로드 후 변경 시 사용)
    public void changeBoardImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;

    }

    //게시글 수정 (Service에서 사용됨) *
    public void updateBoard(String title, String content, Category category, String status) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.status = status;


    }
}

