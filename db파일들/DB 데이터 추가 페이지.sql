insert into `region`(reg_main)
values("서울"),("인천"),("광주"),("부산"),("경기"); 

insert into detailregion(dreg_sub,dreg_reg_num)
values("강남","1"),("서초","1"),("역삼","1"),("송도","2"),("광산","3"),("해운대","4"),("해운대","5");

SELECT  dreg_sub, reg_main FROM detailregion
join `region` on dreg_reg_num=reg_num;

insert into `foodcategory`(fc_main,fc_sub)
values("양식","스테이크"), ("일식","초밥/회"), ("중식","면류"),("뷔페","호텔뷔페"),("아시아","쌀국수");