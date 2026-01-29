package com.itwillbs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.FaqVO;
import com.itwillbs.domain.NoticeVO;
import com.itwillbs.entity.Inquiry;
import com.itwillbs.entity.Notice;
import com.itwillbs.entity.ProductOrder;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.InquiryType;
import com.itwillbs.mapper.CustomerMapper;
import com.itwillbs.repository.InquiryRepository;
import com.itwillbs.entity.Faq;

import com.itwillbs.repository.FaqRepository;
import com.itwillbs.repository.NoticeRepository;
import com.itwillbs.repository.ProductOrderRepository;
import com.itwillbs.security.util.SecurityUtil;
import com.itwillbs.view.MyOrderSelectViewVO;
import com.itwillbs.view.condition.FaqCreateConditionVO;
import com.itwillbs.view.condition.InquiryCreateConditionVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
	private final NoticeRepository noticeRepository;
	private final InquiryRepository inquiryRepository;
	private final CustomerMapper customerMapper;

	private final FaqRepository faqRepository; 
	
	// -------- 공지사항 쓰기
    public void register(Long writerId, NoticeVO vo) {
        // 엔티티 생성 (우리가 만든 생성자 사용)
        Notice notice = new Notice(writerId, vo);
        notice.activate();
        // DB 저장
        noticeRepository.save(notice);
    }
    
 // ------- 공지사항 목록 조회
    @Transactional(readOnly = true)
    public Page<NoticeVO> getNoticeList(Pageable pageable) {
        return noticeRepository.findByIsActiveTrue(pageable).map(NoticeVO::new);
    }

    // -------- 공지사항 고정글 조회
    @Transactional(readOnly = true)
    public List<NoticeVO> getPinnedNotices() {
        return noticeRepository.findByIsPinnedTrueOrderByCreatedAtDesc()
                .stream().map(NoticeVO::new).toList();
    }
    
    // 공지사항 고정글로 등록
    
    
    // 공지사항 상세페이지 
    public NoticeVO getNoticeDetail(Long id) {
        // 1. ID로 조회 (없으면 예외 발생)
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다. id=" + id));
        
        // 2. 조회수 증가
        notice.increaseViewCount(); 
        
        // 3. VO로 변환해서 반환
        return new NoticeVO(notice);
    }
    
    // 공지사항 게시글 보기
    public NoticeVO getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return notice.toVO();
    }

    // 공지사항 수정
    public void updateNotice(NoticeVO vo) {
        Notice notice = noticeRepository.findById(vo.getNoticeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + vo.getNoticeId()));
        
        // 엔티티 내부에 만들어두신 update 메서드 활용
        notice.update(vo);
    }
    
    
    public void inquiriesRegister(InquiryCreateConditionVO conditionVO) {

        InquiryType inquiryType = InquiryType.from(conditionVO.getInquiryType());

        validateByType(inquiryType, conditionVO);
        
        User currentUser = SecurityUtil.getCurrentUser();
        
        Inquiry inquiry = Inquiry.create(
                currentUser,
                inquiryType,
                conditionVO.getTitle(),
                conditionVO.getContent(),
                conditionVO.getOrderId()
          
        );

        inquiryRepository.save(inquiry);
    }
    
    //타입에 맞는 추가 구성요소가 왔는지 검증
    private void validateByType(
            InquiryType inquiryType,
            InquiryCreateConditionVO condition
    ) {

        switch (inquiryType) {
            case PAYMENT -> {
                if (condition.getOrderId() == null) {
                    throw new IllegalArgumentException("결제 문의에는 주문 정보가 필요합니다.");
                }
            }

            case ACCOUNT, SYSTEM, ETC -> {
                // 추가 검증 없음
            }
        }
    }
    
    
 // 문의를 위한 내 결제/거래 내역 가져오기
    public List<MyOrderSelectViewVO> getMyOrdersForInquiry(Long userId) {

        if (userId == null) {
            throw new IllegalArgumentException("로그인 사용자 정보가 없습니다.");
        }

        return customerMapper.selectMyOrdersForInquiry(userId);
    }


    
    // --------------------- faq 페이지
    
    // faq list 가져오기
    public List<FaqVO> getActiveFaqList() {
        return faqRepository
                .findByIsActiveTrueOrderByFaqCategoryAscCreatedAtDesc()
                .stream()
                .map(Faq::toVO)
                .toList();
    }
    
    // faq 작성하기
    
    @Transactional
    public void registerFaq(FaqCreateConditionVO vo) {
        Faq faq = new Faq(vo);
        // 작성 시 기본적으로 활성화하고 싶다면
        faq.activate(); 
        faqRepository.save(faq);
    }
    
}
