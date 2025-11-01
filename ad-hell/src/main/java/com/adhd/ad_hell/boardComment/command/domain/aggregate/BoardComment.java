package com.adhd.ad_hell.boardComment.command.domain.aggregate;


import com.adhd.ad_hell.common.BaseTimeEntity;
import com.adhd.ad_hell.domain.user.command.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Builder
@Table(name = "board_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private BigInteger Id;

    @Column(name = "comment_content", nullable = false)
    private String content;


    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User writerId;

//    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board boardId;



}
