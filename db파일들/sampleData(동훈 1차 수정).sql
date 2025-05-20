# 샘플데이터 



-- 일단 유저 state는 0이면 미삭제, 소셜계정 0이면 미연동으로 놓고 하겠습니다
-- 123 123 입니다
INSERT INTO user (
  us_id, us_pw, us_name, us_phone, us_email, us_nickname, us_sociallogin, us_created, us_state
) VALUES (
  '123',
  '$2a$10$qkMPBfY9Sk27YypLJHTyhu/fip4WOSxtnVA71aMQy3d0pzmZ3rrmu',
  '홍길동',
  '010-1234-5678',
  'hong@example.com',
  '길동이',
  0,
  NOW(),
  0 
);

-- 리뷰 태그
INSERT INTO ScoreType(ST_CATEGORY) 
	VALUES ("청결도"), ("맛"), ("분위기"), ("친절도");

-- 음식 종류
INSERT INTO `foodcategory` (`fc_main`) 
	VALUES ('한식'),('일식'),('양식'),('중식'),('뷔페/무한리필'),('아시아'),('카페');

INSERT INTO `detailfoodcategory` (`dfc_sub`,`dfc_fc_num`) 
	VALUES ('찌개류',1), ('초밥',2), ('다이닝',3), ('짜장',4), ('뷔페',5), ('베트남',6), ('카페',7);


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
	VALUES ("디폴트 매장","화요일","10000","20000","Y",1,1,"테스트용 매장");

														                                                        
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

-- 메뉴구분 종류
INSERT INTO `menutype`(mt_name)
		VALUES("메인"),("사이드"),("식사류"),("음료/주류");




