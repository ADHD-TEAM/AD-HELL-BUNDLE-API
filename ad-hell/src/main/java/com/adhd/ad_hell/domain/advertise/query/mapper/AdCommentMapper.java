package com.adhd.ad_hell.domain.advertise.query.mapper;

import com.adhd.ad_hell.domain.advertise.query.dto.request.AdCommentSearchRequest;
import com.adhd.ad_hell.domain.advertise.query.dto.response.AdCommentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdCommentMapper {
    AdCommentDto selectCommentById(Long adCommentId);
    List<AdCommentDto> selectCommentsByAdId(AdCommentSearchRequest adCommentSearchRequest);
    long countComments(AdCommentSearchRequest adCommentSearchRequest);
    Boolean existsById(Long adCommentId);
}
