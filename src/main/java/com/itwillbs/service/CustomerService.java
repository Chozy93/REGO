package com.itwillbs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.FaqVO;
import com.itwillbs.domain.NoticeVO;
import com.itwillbs.dto.InquiryRequestDTO;
import com.itwillbs.entity.Faq;
import com.itwillbs.entity.Inquiry;
import com.itwillbs.entity.Notice;
import com.itwillbs.entity.User;
import com.itwillbs.entity.enumtype.InquiryType;
import com.itwillbs.mapper.CustomerMapper;
import com.itwillbs.repository.FaqRepository;
import com.itwillbs.repository.InquiryRepository;
import com.itwillbs.repository.NoticeRepository;
import com.itwillbs.repository.UserRepository;
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
	private final UserRepository userRepository;
	
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

    
    
    // ------------ 1:1 문의 페이지 
    // 1:1 문의 저장
    @Transactional
    public void registerInquiry(InquiryRequestDTO dto, String username) {
        // 1. 유저 조회 (로그인한 유저가 DB에 있는지 확인)
        User user = userRepository.findByEmail(username) // 프로젝트에 따라 findByLoginId 등 사용
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 엔티티 생성 
        Inquiry inquiry = Inquiry.create(
                user,
                dto.getInquiryType(),
                dto.getTitle(),
                dto.getContent(),
                dto.getOrderId()
        );

        // 3. 저장
        inquiryRepository.save(inquiry);
    }
    

    
    // 1:1 문의 리스트 가져오기
    public Page<Inquiry> findMyInquiries(String email, String typeStr, Pageable pageable) {
    	InquiryType type = null;
        
        // 1. 문자열로 들어온 타입을 Enum으로 안전하게 변환
        if (typeStr != null && !typeStr.isEmpty() && !"전체 문의 유형".equals(typeStr)) {
            try {
                type = InquiryType.valueOf(typeStr); 
            } catch (IllegalArgumentException e) {
                // 변환 실패 시(잘못된 값이 올 경우) null로 두어 전체 검색이 되게 함
                type = null;
            }
        }
        
        // 2. Repository 호출
        return inquiryRepository.findMyInquiries(email, type, pageable);
    }
    
    // 문의 상세내역
    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다. id=" + id));
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
