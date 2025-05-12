# https://www.erdcloud.com/d/PsDTmWwCgyxQgbSxe
DROP DATABASE IF EXISTS tableup;
CREATE DATABASE tableup;
USE tableup;

DROP TABLE IF EXISTS `ScoreType`;

CREATE TABLE `ScoreType` (
	`ST_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`ST_CATEGORY`	VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS `BusinessHour`;

CREATE TABLE `BusinessHour` (
	`bh_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`bh_start`	DATETIME	NOT NULL,
	`bh_end`	DATETIME	NULL,
	`bh_seat_MAX`	INT	NULL,
	`bh_seat_current`	INT	NULL,
	`bh_table_MAX`	INT	NULL,
	`bh_table_current`	INT	NULL,
	`bh_state`	BOOLEAN	NULL,
	`bh_rt_num`	INT	NULL
);

DROP TABLE IF EXISTS `UserCoupon`;

CREATE TABLE `UserCoupon` (
	`UC_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`UC_STATE`	BOOLEAN NOT NULL,
	`UC_ReC_NUM`	INT	NOT NULL,
	`UC_us_num`	INT	NULL
);

DROP TABLE IF EXISTS `BusinessDate`;

CREATE TABLE `BusinessDate` (
	`BD_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`BD_DATE`	DATE NOT NULL,
	`bd_rt_num`	INT	NULL,
	`BD_OFF`	BOOLEAN NOT NULL,
	`bd_open`	DATETIME	NULL,
	`bd_close`	DATETIME	NULL,
	`bd_brstart`	DATETIME	NULL,
	`bd_brend`	DATETIME	NULL,
	`bd_loam`	DATETIME	NULL,
	`bd_lopm`	DATETIME	NULL
);

DROP TABLE IF EXISTS `ResNews`;

CREATE TABLE `ResNews` (
	`RN_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`rn_rt_num`	INT	NULL,
	`rn_content`	LONGTEXT NOT NULL,
	`rn_state`	BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
	`cart_num`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`cart_MN_NUM`	INT	NOT NULL,
	`cart_res_num`	INT	NULL
);

DROP TABLE IF EXISTS `RestaurantFacility`;

CREATE TABLE `RestaurantFacility` (
	`rf_num`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`rf_rt_num`	INT	NULL,
	`rf_FD_NUM`	INT	NOT NULL
);

DROP TABLE IF EXISTS `RestaurantTag`;

CREATE TABLE `RestaurantTag` (
	`Key`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`rt_num`	INT	NULL,
	`tag_num`	INT	NULL
);

DROP TABLE IF EXISTS `FoodCategory`;

CREATE TABLE `FoodCategory` (
	`fc_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`fc_main`	VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS `Tag`;

CREATE TABLE `Tag` (
	`tag_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`tag_name`	VARCHAR(50) NOT NULL,
	`TAG_TT_NUM`	INT	NOT NULL
);

DROP TABLE IF EXISTS `Reservation`;

CREATE TABLE `Reservation` (
	`res_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`res_us_id`	INT	NULL,
	`res_rt_num`	INT	NULL,
	`res_time`	DATETIME NOT NULL,
	`res_person`	INT NOT NULL,
	`res_request`	TEXT NOT NULL,
	`res_created`	DATETIME NOT NULL,
	`res_state`	INT NOT NULL
);

DROP TABLE IF EXISTS `Restaurant`;

CREATE TABLE `Restaurant` (
	`rt_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`rt_name`	VARCHAR(100) NOT NULL,
	`rt_closed_days`	VARCHAR(100)	NULL,
	`rt_price_lunch`	VARCHAR(20)	NULL,
	`rt_price_dinner`	VARCHAR(20)	NULL,
	`rt_accept`	ENUM('Y', 'N') NOT NULL,
	`rt_dfc_num`	INT	NULL,
	`rt_dreg_num`	INT	NULL,
	`rt_description`	VARCHAR(255)	NULL
);

DROP TABLE IF EXISTS `DetailFoodCategory`;

CREATE TABLE `DetailFoodCategory` (
	`dfc_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`dfc_sub`	VARCHAR(50) NOT NULL,
	`dfc_fc_num`	INT	NULL
);

DROP TABLE IF EXISTS `RestaurantDate`;

CREATE TABLE `RestaurantDate` (
	`rd_num`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`rd_rt_num`	INT	NULL,
	`rd_day`	VARCHAR(50)	NULL,
	`rd_time`	VARCHAR(50)	NULL,
	`rd_type`	ENUM('오전', '오후', '브레이크타임', '라스트오더')	NULL
);

DROP TABLE IF EXISTS `MenuType`;

CREATE TABLE `MenuType` (
	`MT_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`MT_NAME`	VARCHAR(50)	NULL
);

DROP TABLE IF EXISTS `RestaurantDetail`;

CREATE TABLE `RestaurantDetail` (
	`RD_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`rd_rt_num`	INT	NULL,
	`rd_phone`	VARCHAR(255)	NULL,
	`rd_closed_days`	VARCHAR(255)	NULL,
	`rd_info`	VARCHAR(255)	NULL,
	`rd_home`	VARCHAR(255)	NULL,
	`rd_addr`	VARCHAR(255)	NULL
);

DROP TABLE IF EXISTS `Region`;

CREATE TABLE `Region` (
	`reg_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`reg_main`	VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS `UsFollow`;

CREATE TABLE `UsFollow` (
	`UF_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`uf_us_num`	INT	NULL,
	`uf_TYPE`	ENUM('REVIEW', 'RESTAURANT') NOT NULL,
	`uf_FOREIGN`	INT NOT NULL
);

DROP TABLE IF EXISTS `ResCoupon`;

CREATE TABLE `ResCoupon` (
	`ReC_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`ReC_Content`	LONGTEXT NOT NULL,
	`Rec_state`	BOOLEAN NOT NULL,
	`REC_period`	DATETIME	NULL,
	`rec_rt_num`	INT	NULL
);

DROP TABLE IF EXISTS `DefaultResTime`;

CREATE TABLE `DefaultResTime` (
	`DRT_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`drt_rt_num`	INT	NULL,
	`drt_date`		ENUM('월', '화', '수', '목', '금', '토', '일') NOT NULL,
	`drt_off`		BOOLEAN NOT NULL,
	`drt_open`	TIME	NULL,
	`drt_close`	TIME	NULL,
	`drt_brstart`	TIME	NULL,
	`drt_brend`	TIME	NULL,
	`drt_loam`	TIME	NULL,
	`drt_lopm`	TIME	NULL
);

DROP TABLE IF EXISTS `Sociallogin`;

CREATE TABLE `Sociallogin` (
	`sl_num`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`sl_us_no`	INT	NULL,
	`sl_kakao`	BOOLEAN	NULL,
	`sl_phone`	BOOLEAN	NULL,
	`sl_google`	BOOLEAN	NULL
);

DROP TABLE IF EXISTS `DetailRegion`;

CREATE TABLE `DetailRegion` (
	`dreg_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`dreg_sub`	VARCHAR(50) NOT NULL,
	`dreg_reg_num`	INT	NULL
);

DROP TABLE IF EXISTS `ReviewScore`;

CREATE TABLE `ReviewScore` (
	`rs_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`rs_rev_num`	INT	NULL,
	`rs_st_num`	INT	NOT NULL,
	`rs_score`	INT	NULL
);

DROP TABLE IF EXISTS `Review`;

CREATE TABLE `Review` (
	`rev_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`rev_us_id`	INT	NULL,
	`rev_rt_num`	INT	NULL,
	`rev_content`	LONGTEXT	NULL,
	`rev_created`	DATETIME  NOT NULL,
	`rev_updated`	DATETIME  NOT NULL,
	`rev_state`	INT NOT NULL,
	`rev_visit`	DATETIME	NULL,
	`rev_visitor`	INT	NULL
);

DROP TABLE IF EXISTS `Facility`;

CREATE TABLE `Facility` (
	`fa_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`fa_name`	VARCHAR(50)	NOT NULL,
	`fa_title`	VARCHAR(50)	NULL,
	`fa_icon`	VARCHAR(255)	NULL
);

DROP TABLE IF EXISTS `RestaurantManager`;

CREATE TABLE `RestaurantManager` (
	`rm_no`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`rm_id`	VARCHAR(50) NOT NULL,
	`rm_pw`	VARCHAR(255) NOT NULL,
	`rm_email`	VARCHAR(100) NOT NULL,
	`rm_phone`	VARCHAR(20) NOT NULL,
	`rm_name`	VARCHAR(50) NOT NULL,
	`rm_business`	VARCHAR(30) NOT NULL,
	`rm_rt_num`	INT	NULL
);

DROP TABLE IF EXISTS `Payment`;

CREATE TABLE `Payment` (
	`pay_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`pay_res_num`	INT	NULL,
	`pay_method`	VARCHAR(50)	NULL,
	`pay_amount`	INT	NULL,
	`pay_status`	ENUM('Y', 'N', 'C')	NULL,
	`pay_time`	DATETIME	NULL
);

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
	`us_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`us_id`	VARCHAR(20) NOT NULL,
	`us_pw`	VARCHAR(255) NOT NULL,
	`us_name`	VARCHAR(50) NOT NULL,
	`us_phone`	VARCHAR(20) NOT NULL,
	`us_email`	VARCHAR(50) NOT NULL,
	`us_nickname`	VARCHAR(30) NOT NULL,
	`us_sociallogin`	BOOLEAN	NULL,
	`us_created`	DATETIME NOT NULL,
	`us_state`	INT NOT NULL
);

DROP TABLE IF EXISTS `FacilityDetail`;

CREATE TABLE `FacilityDetail` (
	`FD_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`fd_fa_num`	INT	NULL,
	`fd_detail`	LONGTEXT NOT NULL
);

DROP TABLE IF EXISTS `BusinessHourTemplate`;

CREATE TABLE `BusinessHourTemplate` (
	`bhd_num`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`bhd_date`	ENUM('월', '화', '수', '목', '금', '토', '일') NOT NULL,
	`bhd_timeStart`	TIME NOT NULL,
	`bhd_timeEnd`	TIME NOT NULL,
	`bhd_seat`	INT NOT NULL,
	`bhd_table`	INT NOT NULL,
	`bhd_rt_num`	INT	NULL
);

DROP TABLE IF EXISTS `MENU`;

CREATE TABLE `MENU` (
	`MN_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`MN_NAME`	VARCHAR(50) NOT NULL,
	`MN_IMG`	VARCHAR(255)	NULL,
	`MN_PRICE`	int	NULL,
	`MN_CONTENT`	VARCHAR(50)	NULL,
	`MN_MT_NUM`	INT	NOT NULL,
	`mn_rt_num`	INT	NULL,
	`MN_DIV`	VARCHAR(50)	NULL
);

DROP TABLE IF EXISTS `TagType`;

CREATE TABLE `TagType` (
	`TT_NUM`	INT PRIMARY KEY AUTO_INCREMENT	NOT NULL,
	`TT_NAME`	VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS `File`;

CREATE TABLE `File` (
	`file_num`	INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	`file_path`	VARCHAR(255) NOT NULL,
	`file_name`	VARCHAR(255) NOT NULL,
	`File_type`	ENUM('REIVEW', 'RestaurantDetail') NOT NULL,
	`File_FOREIGN`	INT NOT NULL,
	`file_tag`	ENUM('내부', '외부', '메뉴판', '음식') NOT NULL
);

ALTER TABLE `BusinessHour` ADD CONSTRAINT `FK_Restaurant_TO_BusinessHour_1` FOREIGN KEY (
	`bh_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `UserCoupon` ADD CONSTRAINT `FK_ResCoupon_TO_UserCoupon_1` FOREIGN KEY (
	`UC_ReC_NUM`
)
REFERENCES `ResCoupon` (
	`ReC_NUM`
);

ALTER TABLE `UserCoupon` ADD CONSTRAINT `FK_User_TO_UserCoupon_1` FOREIGN KEY (
	`UC_us_num`
)
REFERENCES `User` (
	`us_num`
);

ALTER TABLE `BusinessDate` ADD CONSTRAINT `FK_Restaurant_TO_BusinessDate_1` FOREIGN KEY (
	`bd_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `ResNews` ADD CONSTRAINT `FK_Restaurant_TO_ResNews_1` FOREIGN KEY (
	`rn_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `cart` ADD CONSTRAINT `FK_MENU_TO_cart_1` FOREIGN KEY (
	`cart_MN_NUM`
)
REFERENCES `MENU` (
	`MN_NUM`
);

ALTER TABLE `cart` ADD CONSTRAINT `FK_Reservation_TO_cart_1` FOREIGN KEY (
	`cart_res_num`
)
REFERENCES `Reservation` (
	`res_num`
);

ALTER TABLE `RestaurantFacility` ADD CONSTRAINT `FK_Restaurant_TO_RestaurantFacility_1` FOREIGN KEY (
	`rf_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `RestaurantFacility` ADD CONSTRAINT `FK_FacilityDetail_TO_RestaurantFacility_1` FOREIGN KEY (
	`rf_FD_NUM`
)
REFERENCES `FacilityDetail` (
	`FD_NUM`
);

ALTER TABLE `RestaurantTag` ADD CONSTRAINT `FK_Restaurant_TO_RestaurantTag_1` FOREIGN KEY (
	`rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `RestaurantTag` ADD CONSTRAINT `FK_Tag_TO_RestaurantTag_1` FOREIGN KEY (
	`tag_num`
)
REFERENCES `Tag` (
	`tag_num`
);

ALTER TABLE `Tag` ADD CONSTRAINT `FK_TagType_TO_Tag_1` FOREIGN KEY (
	`TAG_TT_NUM`
)
REFERENCES `TagType` (
	`TT_NUM`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_User_TO_Reservation_1` FOREIGN KEY (
	`res_us_id`
)
REFERENCES `User` (
	`us_num`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Restaurant_TO_Reservation_1` FOREIGN KEY (
	`res_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `Restaurant` ADD CONSTRAINT `FK_DetailFoodCategory_TO_Restaurant_1` FOREIGN KEY (
	`rt_dfc_num`
)
REFERENCES `DetailFoodCategory` (
	`dfc_num`
);

ALTER TABLE `Restaurant` ADD CONSTRAINT `FK_DetailRegion_TO_Restaurant_1` FOREIGN KEY (
	`rt_dreg_num`
)
REFERENCES `DetailRegion` (
	`dreg_num`
);

ALTER TABLE `DetailFoodCategory` ADD CONSTRAINT `FK_FoodCategory_TO_DetailFoodCategory_1` FOREIGN KEY (
	`dfc_fc_num`
)
REFERENCES `FoodCategory` (
	`fc_num`
);

ALTER TABLE `RestaurantDate` ADD CONSTRAINT `FK_Restaurant_TO_RestaurantDate_1` FOREIGN KEY (
	`rd_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `RestaurantDetail` ADD CONSTRAINT `FK_Restaurant_TO_RestaurantDetail_1` FOREIGN KEY (
	`rd_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `UsFollow` ADD CONSTRAINT `FK_User_TO_UsFollow_1` FOREIGN KEY (
	`uf_us_num`
)
REFERENCES `User` (
	`us_num`
);

ALTER TABLE `ResCoupon` ADD CONSTRAINT `FK_Restaurant_TO_ResCoupon_1` FOREIGN KEY (
	`rec_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `DefaultResTime` ADD CONSTRAINT `FK_Restaurant_TO_DefaultResTime_1` FOREIGN KEY (
	`drt_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `Sociallogin` ADD CONSTRAINT `FK_User_TO_Sociallogin_1` FOREIGN KEY (
	`sl_us_no`
)
REFERENCES `User` (
	`us_num`
);

ALTER TABLE `DetailRegion` ADD CONSTRAINT `FK_Region_TO_DetailRegion_1` FOREIGN KEY (
	`dreg_reg_num`
)
REFERENCES `Region` (
	`reg_num`
);

ALTER TABLE `ReviewScore` ADD CONSTRAINT `FK_Review_TO_ReviewScore_1` FOREIGN KEY (
	`rs_rev_num`
)
REFERENCES `Review` (
	`rev_num`
);

ALTER TABLE `ReviewScore` ADD CONSTRAINT `FK_ScoreType_TO_ReviewScore_1` FOREIGN KEY (
	`rs_st_num`
)
REFERENCES `ScoreType` (
	`ST_NUM`
);

ALTER TABLE `Review` ADD CONSTRAINT `FK_User_TO_Review_1` FOREIGN KEY (
	`rev_us_id`
)
REFERENCES `User` (
	`us_num`
);

ALTER TABLE `Review` ADD CONSTRAINT `FK_Restaurant_TO_Review_1` FOREIGN KEY (
	`rev_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `RestaurantManager` ADD CONSTRAINT `FK_Restaurant_TO_RestaurantManager_1` FOREIGN KEY (
	`rm_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `Payment` ADD CONSTRAINT `FK_Reservation_TO_Payment_1` FOREIGN KEY (
	`pay_res_num`
)
REFERENCES `Reservation` (
	`res_num`
);

ALTER TABLE `FacilityDetail` ADD CONSTRAINT `FK_Facility_TO_FacilityDetail_1` FOREIGN KEY (
	`fd_fa_num`
)
REFERENCES `Facility` (
	`fa_num`
);

ALTER TABLE `BusinessHourTemplate` ADD CONSTRAINT `FK_Restaurant_TO_BusinessHourTemplate_1` FOREIGN KEY (
	`bhd_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `MENU` ADD CONSTRAINT `FK_MenuType_TO_MENU_1` FOREIGN KEY (
	`MN_MT_NUM`
)
REFERENCES `MenuType` (
	`MT_NUM`
);

ALTER TABLE `MENU` ADD CONSTRAINT `FK_Restaurant_TO_MENU_1` FOREIGN KEY (
	`mn_rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);
