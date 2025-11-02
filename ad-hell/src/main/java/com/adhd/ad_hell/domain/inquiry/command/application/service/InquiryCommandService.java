package com.adhd.ad_hell.domain.inquiry.command.application.service;


import com.adhd.ad_hell.domain.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.domain.category.command.domain.repository.CategoryRepository;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import com.adhd.ad_hell.domain.inquiry.command.application.dto.request.InquiryCreateRequest;
import com.adhd.ad_hell.domain.inquiry.command.domain.aggregate.Inquiry;
import com.adhd.ad_hell.domain.inquiry.command.domain.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryCommandService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /**
     * 문의 등록
     */
    @Transactional
    public void createInquiry(InquiryCreateRequest req) {

        // FK 조회 (없으면 비즈니스 예외)
        //
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // 단순 필드 매핑 (title, content)
        Inquiry inquiry = modelMapper.map(req, Inquiry.class);

        // 연관관계 주입 (엔티티에 아래 두 메서드가 있어야 함)
        // public void linkUser(User user) { this.user = user; }
        // public void linkCategory(Category category) { this.category = category; }
        inquiry.linkUser(user);
        inquiry.linkCategory(category);

        inquiryRepository.save(inquiry);

    }

}
