-- drop database tableup;
-- 1. 스키마(데이터베이스) 생성
CREATE DATABASE IF NOT EXISTS tableup DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2. 사용할 스키마 선택
USE tableup;

-- 3. member 테이블 생성
CREATE TABLE member (
  me_id VARCHAR(50) PRIMARY KEY,           -- 아이디 (기본키)
  me_pw VARCHAR(255) NOT NULL,             -- 비밀번호 (암호화 대비 넉넉하게)
  me_email VARCHAR(50) NOT NULL,          -- 이메일
  me_authority VARCHAR(20) DEFAULT 'USER', -- 권한 (예: USER, ADMIN)
  me_cookie VARCHAR(255),                  -- 로그인 유지 쿠키값
  me_limit DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE post (
  po_num INT AUTO_INCREMENT PRIMARY KEY,    -- 글 번호 (기본키, 자동 증가)
  po_title VARCHAR(255) NOT NULL,           -- 제목
  po_content LONGTEXT,                      -- 내용
  po_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- 작성일 (기본값: 현재시간)
  po_view INT DEFAULT 0,                    -- 조회수
  po_up INT DEFAULT 0,                      -- 추천수
  po_down INT DEFAULT 0,                    -- 비추천수
  po_me_id VARCHAR(15) NOT NULL,            -- 작성자 ID (외래키 후보)
  po_bo_num INT NOT NULL,                   -- 게시판 번호 (외래키 후보)
  po_del CHAR(1) DEFAULT 'N'                -- 삭제 여부 (Y/N)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
