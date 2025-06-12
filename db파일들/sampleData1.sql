# 샘플데이터 

USE tableup;

-- 일단 유저 state는 0이면 미삭제, 소셜계정 0이면 미연동으로 놓고 하겠습니다
-- 123 123 입니다
INSERT INTO `user` (
  us_id, us_pw, us_name, us_phone, us_email, us_nickname, us_sociallogin, us_created, us_state
) VALUES (
  '123',
  '$2a$10$juSqcXcXvHzpTdW1Ml/yk.IaLl7uPP77lksZan0T83ZH8Dq4ns7MG',
  '홍길동',
  '010-1234-5678',
  'hong@example.com',
  '길동이',
  0,
  NOW(),
  0 
);


INSERT INTO `admin` (
  ad_id, ad_pw
) VALUES (
  'a123',
  '$2a$10$juSqcXcXvHzpTdW1Ml/yk.IaLl7uPP77lksZan0T83ZH8Dq4ns7MG'
);


-- 음식 종류
INSERT INTO `foodcategory` (`fc_main`) 
	VALUES ('한식'),('일식'),('양식'),('중식'),('뷔페/무한리필'),
		   ('카페'),('주점/다이닝바'),('아시아'),("패스트 푸드"),('기타');

INSERT INTO `detailfoodcategory` (`dfc_sub`,`dfc_fc_num`) 
	VALUES 
    -- 1. 한식
    ("백반,가정식",1),("찌개,전골,국",1),("한식다이닝",1),
	-- 2. 일식
    ("돈까스",2),("초밥,스시,오마카세",2),("라멘",2),
    -- 3. 양식 
    ("스테이크",3),("바베큐",3), ("파인다이닝",3),
    -- 4. 중식
    ("중식",4),("딤섬",4),("중식 코스 요리",4),
    -- 5. 뷔페, 무한리필
    ("호텔뷔페",5),("뷔페,시푸드뷔페",5),
    -- 6. 카페 & 베이커리
    ("커피,차,음료",6),("빵,베이커리",6),("디저트,케이크",6),
    -- 7. 주류
    ("와인,전통주",7),("칵테일,위스키",7),("소주,맥주",7),
    -- 8. 아시아
    ("쌀국수,분짜",8),("커리",8),("팟타이",8),
    -- 9. 패스트 푸드
    ("피자",9),("햄버거",9),("튀김",9),
    -- 10. 기타
    ("브런치",10),("분식",10),("비건",10),("면류",10);

-- 메뉴분류
INSERT INTO `menutype` (MT_NAME)
	VALUES 
    -- 고기류 
    ("스테이크"),("한우오마카세"),("바베큐"),("족발,보쌈"),("곱창, 막창"),("양고기"),("소고기구이"),("돼지고기구이"),("돈까스"),("닭고기"),("오리"),
	-- 생선, 해물 요리
    ("회,사시미"),("초밥, 스시"),("장어 요리"),("참치회"),("갑각류(게, 랍스터)"),
    ("해물(탕/찜/볶음)"),("조개류(굴, 조개구이)"),("스시 오마카세"),("가이세키오마카세"),
    -- 주류
    ("와인"),("전통주"),("칵테일/위스키"),("맥주,호프"),("소주"),("주류"),
    -- 카페 & 베이커리
    ("커피"),("디카페인"),("빵,베이커리"),("디저트"),("케이크"),("차"),("음료"),
    -- 나머지 
    ("면류"),("요리류"),("다이닝 코스"),
    ("찌개,전골"),("튀김"),("파스타"),("피자"),("햄버거"),
    ("비건"),("샤브샤브"),("라멘"),("냉면"),("국수"),
    ("백반/가정식"),("분식"),("기타 오마카세"),("팝업"),("브런치"),
    ("쌀국수"),("사이드"),("종합");

-- 지역 종류
INSERT INTO `region` (`reg_main`) 
	VALUES ('서울'),('경기'),('경상'),('제주'),('강원'),('충청'),('전라');

INSERT INTO `detailregion` (`dreg_sub`,`dreg_reg_num`) VALUES 	('강남/서초',1),('잠실/송파/강동',1),('여의도/영등포/강서',1),('건대/성수/왕십리',1),('종로/중구',1),('홍대/합정/마포',1),('용산/이태원/한남',1),('성북/노량/중랑',1),('구로/관악/동작',1),('기타',1),
																('경기 북부',2),('의왕/안양/군포',2),('용인/화성/평택',2),('부천/시흥/안산',2),('성남/하남',2),('수원',2),('인천',2),('기타',2),
                                                                ('부산',3),('울산',3),('경남',3),('대구',3),('경북',3),('울산',3),('기타',3),
                                                                ('제주시',4),('서귀포',4),('기타',4),
                                                                ('춘천',5),('원주',5),('강릉/속초',5),('기타',5),
                                                                ('대전',6),('세종',6),('충남',6),('충북',6),('기타',6),
                                                                ('광주',7),('전주',7),('전남',7),('전북',7),('기타',7);
                                   
-- 매장							
INSERT INTO `restaurant` (rt_name,rt_closed_days,rt_price_lunch,rt_price_dinner,rt_accept,rt_dfc_num,rt_dreg_num,rt_description) 
	VALUES ("디폴트 매장","화요일","10000","20000","Y",1,1,"테스트용 매장"),
		 ("디폴트 매장2","수요일","20000","30000","Y",2,2,"테스트용 매장2");

														                                                        
-- 태그                                                       
INSERT INTO tagtype(TT_NAME) 
	VALUES ('분위기'),('테이블타입'),('음료');--,('편의시설'),('가격');


INSERT INTO tag(tag_name,TAG_TT_NUM) 
	VALUES
			--  분위기
			('차분한',1),('신나는',1),('세련된',1),('전통적인',1),('트렌디한',1)
			,('조용한',1),('모던한',1),('비즈니스미팅',1),('뷰',1)
            ,('기념일',1),('데이트',1),('연회,단체회식',1)
			--  테이블타입
            ,('룸',2),('바',2),('홀',2),('테라스',2),('창가',2),('대관',2)
            ,('주류O',3),('주류x',3),('와인',3),('위스키',3)
            ;

            --  편의시설
            -- ,('주차가능',3),('발렛가능',3),('콜키지',3),('콜키지프리',3),('레터링',3)
--             ,('웰컴키드존',3),('아기의자',3),('반려동물 동반',3),('자동결제',3),('예약금 0원',3)
--             ,('위스키 주문',3),('와인주문',3),('미쉐린',3),('단체 이용가능',3),('대관 가능',3)
--             ,('노키드존',3),('전문 소믈리에',3),('유아시설 제공',3),('장애인 편의시설',3),('무선인터넷',3);
    
-- 편의시설
INSERT INTO facility(fa_name,fa_title,fa_icon)
	VALUES('무선인터넷','사용가능한 무선인터넷',null);

# 아이콘 마다 위치 정해서 값을 넣어놔야 함.
INSERT INTO facility(fa_name,fa_title,fa_icon)
	VALUES('단체석','단체석 이용시 문의','-925px -220px'),
		  ('네이버페이','네이버페이','-80px -420px'),
          ('무선인터넷','무선인터넷','-257px -220px'),
          ('카드','카드','-80px -220px'),
          ('주차','주차','-988px -413px'),
          ('칵테일/주류 제공','칵테일/주류 제공',null),
          ('장애인 편의시설','장애인 편의시설',null),
          ('와인/칵테일 제공','와인/칵테일 제공',null),
          ('프리미엄 와인/칵테일룸','프리미엄 와인/칵테일룸',null),
          ('전기차 충전소','전기차 충전소',null),
          ('반려동물 동반','반려동물 동반',null),
          ('노키드존','노키드존',null),
          ('라운지 대기공간','라운지 대기공간',null);

INSERT INTO restaurantfacility(rf_rt_num,rf_fa_num,rf_detail)
	VALUES(1,1,'와이파이 사용 가능(가게 문의)');

INSERT INTO restauranttag(rt_num,tag_num)
	VALUES(1,1),(1,2);

-- 샘플 점주 m123 123
INSERT INTO restaurantmanager (
  rm_id, rm_pw, rm_email, rm_phone, rm_name, rm_business, rm_rt_num
) VALUES (
  'm123',
  '$2a$10$qkMPBfY9Sk27YypLJHTyhu/fip4WOSxtnVA71aMQy3d0pzmZ3rrmu',
  'rm@example.com',
  '010-2345-6789',
  '디폴트점주',
  '000-00-00000',
  1
);

-- 디폴트매장의 매장상세
INSERT INTO restaurantdetail (
  rd_rt_num, rd_phone, rd_closed_days, rd_info, rd_home, rd_addr
) VALUES (
  1,
  '010-2345-6789',
  '화요일 휴무',
  '디폴트 안내및 유의사항',
  '디폴트 url',
  '디폴트 주소'
),
(
  2,
  '010-2345-6700',
  '수요일 휴무',
  '디폴트 안내및 유의사항',
  'www.naver.com',
  '디폴트 주소2'
);

-- 디폴트 예약 state=0 (미확정)review
INSERT INTO reservation (
  res_us_num, res_rt_num, res_time, res_person,
  res_request, res_created, res_state
)
VALUES (
  1, 1, now(), 2,
  '요청사항', now(), 0   -- DATE_ADD(now(), INTERVAL 2 HOUR) -- 예: 2시간 후 종료
);

-- 디폴트 리뷰
INSERT INTO review (
  rev_us_num, rev_rt_num, rev_content, rev_created, rev_updated, rev_state, rev_visit,rev_visitor
) VALUES (
	1,
	1,
	'리뷰 내용',
	now(),
	now(),
	0,
	now(),
    2
);

-- 리뷰 태그
INSERT INTO ScoreType(ST_CATEGORY) 
	VALUES ("청결도"), ("맛"), ("분위기"), ("친절도");

-- 리뷰 점수
INSERT INTO reviewscore(rs_rev_num,rs_st_num,rs_score) 
	VALUES (1,1,5), (1,2,4), (1,3,5), (1,4,3);

-- 디폴트 메뉴 등록
INSERT INTO menu(MN_NAME,MN_PRICE,MN_CONTENT,MN_MT_NUM,mn_rt_num) 
	VALUES ("디폴트",10000,"디폴트",1,1);


-- 장바구니
INSERT INTO cart(cart_MN_NUM,cart_res_num) 
	VALUES (1,1);
    
-- 첨부파일
INSERT INTO FILE(file_path,file_name,File_type,File_FOREIGN,file_tag) 
	VALUES ("","디폴트파일","REVIEW",1,"내부"),("","디폴트파일","RESTAURANTDETAIL",1,"내부"),("","디폴트파일","MENU",1,"내부");
    

-- 매장 소식 state가 0이면 공개중
INSERT INTO resnews(rn_rt_num,rn_content,rn_state) 
	VALUES (1,"매장소식",0);
    

-- 매장 쿠폰 state가 0이면 사용가능
INSERT INTO rescoupon(ReC_Content,Rec_state,REC_period, rec_rt_num) 
	VALUES ("쿠폰설명",0,now()+100000000,1);	-- 한달뒤까지

-- 보유중인 쿠폰 state가 0이면 사용가능
INSERT INTO usercoupon(UC_STATE,UC_ReC_NUM,UC_us_num) 
	VALUES (0,1,1);	
    
    
INSERT INTO usfollow(uf_us_num, uf_TYPE, uf_FOREIGN)
	VALUES (1,"RESTAURANT", 1),(1,"REVIEW", 1),(1,"RESTAURANT", 2);

INSERT INTO defaultrestime(drt_rt_num, drt_date, drt_off, drt_open, drt_close, drt_lopm)
	VALUES (1,"수", 0, '16:00:00', '23:00:00', '22:00:00');
    







