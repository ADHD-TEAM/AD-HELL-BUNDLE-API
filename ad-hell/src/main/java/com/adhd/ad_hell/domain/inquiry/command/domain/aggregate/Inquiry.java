package com.adhd.ad_hell.domain.inquiry.command.domain.aggregate;

import com.adhd.ad_hell.common.BaseTimeEntity;
import com.adhd.ad_hell.domain.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.domain.user.command.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Builder
@Table(name = "inquiry")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "answerd_at")
    private LocalDateTime answerdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    // 관리자 답변 (nullable)
    @Column(name = "response")
    private String response;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    /** 관리자 답변/수정 */
    public void answer(String response) {
        this.response = response;
        this.answeredAt = LocalDateTime.now();
    }

    // 연관 관계 주입
    public void linkUser(User user) {
        this.user = user;
    }

    public void linkCategory(Category category) {
        this.category = category;
    }





}
