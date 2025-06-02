ALTER TABLE `tableup`.`usfollow` 
CHANGE COLUMN `uf_TYPE` `uf_TYPE` ENUM('REVIEW', 'RESTAURANT', 'DETAILREGION', 'DETAILFOODCATEGORY') NOT NULL ;


DROP TABLE IF EXISTS `revres`;
CREATE TABLE `revres` (
   revres_num INT AUTO_INCREMENT PRIMARY KEY, 
   rt_num int,
   us_num int,
   rev_num int,
   res_num int
);


ALTER TABLE `revres` ADD CONSTRAINT `FK_Restaurant_TO_revres_1` FOREIGN KEY (
	`rt_num`
)
REFERENCES `Restaurant` (
	`rt_num`
);

ALTER TABLE `revres` ADD CONSTRAINT `FK_user_TO_revres_1` FOREIGN KEY (
	`us_num`
)
REFERENCES `user` (
	`us_num`
);
ALTER TABLE `revres` ADD CONSTRAINT `FK_review_TO_revres_1` FOREIGN KEY (
	`rev_num`
)
REFERENCES `review` (
	`rev_num`
);
ALTER TABLE `revres` ADD CONSTRAINT `FK_reservation_TO_revres_1` FOREIGN KEY (
	`res_num`
)
REFERENCES `reservation` (
	`res_num`
);