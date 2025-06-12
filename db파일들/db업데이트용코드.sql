use tableup;

ALTER TABLE `tableup`.`usfollow` 
CHANGE COLUMN `uf_TYPE` `uf_TYPE` ENUM('REVIEW', 'RESTAURANT', 'DETAILREGION', 'DETAILFOODCATEGORY') NOT NULL ;

ALTER TABLE `tableup`.`user` 
CHANGE COLUMN `us_id` `us_id` VARCHAR(255) NOT NULL ;


-- 트리거 제거
DROP TRIGGER IF EXISTS trg_before_review_insert;
DROP TRIGGER IF EXISTS trg_after_review_insert;
DROP TRIGGER IF EXISTS trg_before_review_delete;

-- 로그 테이블 제거
DROP TABLE IF EXISTS review_trigger_log;

-- # 1예약 1리뷰 보장을 위한

-- DROP TABLE IF EXISTS `review_trigger_log`;
-- DROP TABLE IF EXISTS `revres`;

-- -- 리뷰-예약 연결 테이블
-- CREATE TABLE `revres` (
--    revres_num INT AUTO_INCREMENT PRIMARY KEY, 
--    rt_num INT NOT NULL,
--    us_num INT NOT NULL,
--    rev_num INT NOT NULL,
--    res_num INT NOT NULL,
--    UNIQUE KEY uq_resnum (res_num) 
-- );

-- -- 트리거 로그 테이블
-- CREATE TABLE `review_trigger_log` (
--    log_id INT AUTO_INCREMENT PRIMARY KEY,
--    log_type ENUM('BLOCKED', 'INSERTED', 'DELETED') NOT NULL,
--    us_num INT,
--    rt_num INT,
--    rev_num INT,
--    res_num INT,
--    log_time DATETIME DEFAULT CURRENT_TIMESTAMP,
--    log_message TEXT
-- );

-- -- 외래키 제약
-- ALTER TABLE `revres` ADD CONSTRAINT FK_Restaurant_TO_revres_1
--   FOREIGN KEY (rt_num) REFERENCES Restaurant(rt_num);

-- ALTER TABLE `revres` ADD CONSTRAINT FK_user_TO_revres_1
--   FOREIGN KEY (us_num) REFERENCES user(us_num);

-- ALTER TABLE `revres` ADD CONSTRAINT FK_review_TO_revres_1
--   FOREIGN KEY (rev_num) REFERENCES review(rev_num);

-- ALTER TABLE `revres` ADD CONSTRAINT FK_reservation_TO_revres_1
--   FOREIGN KEY (res_num) REFERENCES reservation(res_num);


-- DROP TRIGGER IF EXISTS trg_before_review_insert;
-- DROP TRIGGER IF EXISTS trg_after_review_insert;

-- -- BEFORE INSERT 트리거
-- DELIMITER $$

-- CREATE TRIGGER trg_before_review_insert
-- BEFORE INSERT ON review
-- FOR EACH ROW
-- BEGIN
--   DECLARE reservation_id INT;

--   -- 최근 예약 찾기
--   SELECT res_num INTO reservation_id
--   FROM reservation
--   WHERE res_us_num = NEW.rev_us_num
--     AND res_rt_num = NEW.rev_rt_num
--   ORDER BY res_time DESC
--   LIMIT 1;

--   -- 중복 체크
--   IF EXISTS (
--     SELECT 1 FROM revres WHERE res_num = reservation_id
--   ) THEN
--     -- 차단 로그 기록
--     INSERT INTO review_trigger_log (
--       log_type, us_num, rt_num, res_num, log_message
--     ) VALUES (
--       'BLOCKED',
--       NEW.rev_us_num,
--       NEW.rev_rt_num,
--       reservation_id,
--       '중복 리뷰 시도: 해당 예약은 이미 리뷰됨.'
--     );

--     SIGNAL SQLSTATE '45000'
--     SET MESSAGE_TEXT = '이미 이 예약에 대한 리뷰가 존재합니다.';
--   END IF;
-- END$$

-- DELIMITER ;

-- -- AFTER INSERT 트리거
-- DELIMITER $$

-- CREATE TRIGGER trg_after_review_insert
-- AFTER INSERT ON review
-- FOR EACH ROW
-- BEGIN
--   DECLARE reservation_id INT;

--   -- 최근 예약 찾기
--   SELECT res_num INTO reservation_id
--   FROM reservation
--   WHERE res_us_num = NEW.rev_us_num
--     AND res_rt_num = NEW.rev_rt_num
--   ORDER BY res_time DESC
--   LIMIT 1;

--   -- revres에 매핑 정보 삽입
--   INSERT INTO revres (rt_num, us_num, rev_num, res_num)
--   VALUES (NEW.rev_rt_num, NEW.rev_us_num, NEW.rev_num, reservation_id);

--   -- 성공 로그 기록
--   INSERT INTO review_trigger_log (
--     log_type, us_num, rt_num, rev_num, res_num, log_message
--   ) VALUES (
--     'INSERTED',
--     NEW.rev_us_num,
--     NEW.rev_rt_num,
--     NEW.rev_num,
--     reservation_id,
--     '리뷰 등록됨.'
--   );
-- END$$

-- DELIMITER ;

-- DROP TRIGGER IF EXISTS trg_before_review_delete;

-- DELIMITER $$

-- CREATE TRIGGER trg_before_review_delete
-- BEFORE UPDATE ON review
-- FOR EACH ROW
-- BEGIN
--   DECLARE target_res_num INT;

--   -- 삭제 조건 확인
--   IF OLD.rev_state != -1 AND NEW.rev_state = -1 THEN
-- 	IF EXISTS (SELECT 1 FROM revres WHERE rev_num = NEW.rev_num) THEN
--     -- 삭제 전 관련 예약 번호 확보
-- 		SELECT res_num INTO target_res_num
-- 		FROM revres
-- 		WHERE rev_num = NEW.rev_num
-- 		LIMIT 1;

-- 		-- 로그 기록
-- 		INSERT INTO review_trigger_log (
-- 		  log_type, us_num, rt_num, rev_num, res_num, log_message
-- 		) VALUES (
-- 		  'DELETED',
-- 		  OLD.rev_us_num,
-- 		  OLD.rev_rt_num,
-- 		  OLD.rev_num,
-- 		  target_res_num,
-- 		  '리뷰가 삭제되어 revres에서 제거됨.'
-- 		);

-- 		-- 매핑 정보 제거
-- 		DELETE FROM revres WHERE rev_num = OLD.rev_num;

-- 	  END IF;
--   END IF;
-- END$$

-- DELIMITER ;
