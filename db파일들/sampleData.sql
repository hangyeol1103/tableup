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
	VALUES ('한식'),('일식'),('양식'),('중식'),('뷔페/무한리필'),('아시아'),('카페');

INSERT INTO `detailfoodcategory` (`dfc_sub`,`dfc_fc_num`) 
	VALUES ('찌개류',1), ('초밥',2), ('다이닝',3), ('짜장',4), ('뷔페',5), ('베트남',6), ('카페',7);

-- 메뉴분류
INSERT INTO `menutype` (MT_NAME)
	VALUES ("고기"),("찌개류"),("면류"),("음료"),("주류"),("튀김류"),("돈까스");

-- 지역 종류
INSERT INTO `region` (`reg_main`) 
	VALUES ('서울'),('경기/인천'),('경상'),('제주'),('강원'),('충청'),('전라');

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
	VALUES ('분위기');


INSERT INTO tag(tag_name,TAG_TT_NUM) 
	VALUES('차분한',1),('신나는',1),('세련된',1);
    
-- 편의시설
INSERT INTO facility(fa_name,fa_title,fa_icon)
	VALUES('무선인터넷','사용가능한 무선인터넷',null);

INSERT INTO restaurantfacility(rf_rt_num,rf_fa_num,rf_detail)
	VALUES(1,1,'와이파이 사용 가능(가게 문의)');


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
  res_us_num, res_rt_num, res_time, res_person, res_request, res_created, res_state
) VALUES (
  1,
  1,
  now(),
  2,
  '요청사항',
  now(),
  0
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


    







