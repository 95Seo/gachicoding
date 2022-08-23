//package org.deco.gachicoding.unit.post.notice.domain;
//
//import org.deco.gachicoding.post.notice.domain.Notice;
//import org.deco.gachicoding.post.notice.domain.repository.NoticeRepository;
//import org.deco.gachicoding.user.domain.User;
//import org.deco.gachicoding.user.domain.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class NoticeRepositoryTest {
//
//    @Autowired
//    NoticeRepository noticeRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    User testUser;
//
//    String notTitle = "공지사항 테스트 제목";
//    String notContent = "공지사항 테스트 내용";
//    Boolean notPin = false;
//    Boolean notActivated = true;
//
//    @BeforeEach
//    private void before() {
//        // Factory를 이용한 생성 패턴으로 변경
//        User user = User.builder()
//                .userEmail("test111@test.com")
//                .userPassword("test1234")
//                .userName("테스트")
//                .userNick("testMachine")
//                .build();
//
//        testUser = userRepository.save(user);
//    }
//
//    private Notice createNoticeMock() {
//        Notice entity = Notice.builder()
//                .writer(testUser)
//                .notTitle(notTitle)
//                .notContent(notContent)
//                .notPin(notPin)
//                .notActivated(notActivated)
//                .build();
//
//        return noticeRepository.save(entity);
//    }
//
//    @Test
//    @DisplayName("공지사항을 저장한다.")
//    void save_savedNotice_Success() {
//        // given
//
//
//        // when
//
//
//        // then
//
//
//    }
//
//    @Test
//    void 인덱스로_공지사항_조회() {
//        Notice testNotice = createNoticeMock();
//        Long noticeIdx = testNotice.getNotIdx();
//
//        Optional<Notice> notice = noticeRepository.findById(noticeIdx);
//        assertTrue(notice.isPresent());
//        assertEquals("공지사항 테스트 제목", notice.get().getNotTitle());
//        assertEquals("공지사항 테스트 내용", notice.get().getNotContent());
//    }
//
////    @Test
////    public void 공지사항_목록_조회() {
////        String notTitle = "공지사항 목록 테스트 제목 (고양이)";
////        String notContent = "공지사항 목록 테스트 내용";
////        Boolean notPin = false;
////        Boolean notActivated = true;
////        for(int i = 0; i < 10; i++) {
////            createNoticeMock(notTitle, notContent, notPin, notActivated);
////        }
////
////        String keyword = "(고양이)";
////
////        Page<Notice> notice = noticeRepository.findByNotContentContainingIgnoreCaseAndNotActivatedTrueOrNotTitleContainingIgnoreCaseAndNotActivatedTrueOrderByNotIdxDesc(keyword, keyword, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "notIdx")));
////
////        // NumberOfElements 요청 페이지에서 조회 된 데이터의 갯수
////        assertEquals(10, notice.getTotalElements());
////    }
////
////    @Test
////    public void 인덱스로_공지사항_삭제() {
////        String notTitle = "공지사항 테스트 제목";
////        String notContent = "공지사항 테스트 내용";
////        Boolean notPin = false;
////        Boolean notActivated = true;
////        Long noticeIdx = createNoticeMock(notTitle, notContent, notPin, notActivated);
////
////        Optional<Notice> notice = noticeRepository.findById(noticeIdx);
////
////        assertTrue(notice.isPresent());
////
////        noticeRepository.deleteById(noticeIdx);
////
////        notice = noticeRepository.findById(noticeIdx);
////
////        assertTrue(notice.isEmpty());
////    }
////
////    @Test
////    public void 검색어로_공지사항_검색_리스트() {
////        String notTitle = "공지사항 테스트 제목 고양이 병아리";
////        String notContent = "공지사항 테스트 내용 강아지 병아리";
////        Boolean notPin = false;
////        Boolean notActivated = true;
////        Long noticeIdx = createNoticeMock(notTitle, notContent, notPin, notActivated);
////
////        Optional<Notice> notice = noticeRepository.findById(noticeIdx);
////
////        assertTrue(notice.isPresent());
////
////        String toFindKeyword = "고양이";
////
////        Page<Notice> search_notice = noticeRepository.findByNotContentContainingIgnoreCaseAndNotActivatedTrueOrNotTitleContainingIgnoreCaseAndNotActivatedTrueOrderByNotIdxDesc(toFindKeyword, toFindKeyword, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "notIdx")));
////
////        for (Notice n : search_notice) {
////            assertEquals(n.getNotTitle(),notTitle);
////            assertEquals(n.getNotContent(),notContent);
////            assertEquals(n.getNotPin(),notPin);
////            assertEquals(n.getNotActivated(),notActivated);
////        }
////    }
//
//}
