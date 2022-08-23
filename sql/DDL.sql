-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema gachicoding
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `gachicoding` ;

-- -----------------------------------------------------
-- Schema gachicoding
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gachicoding` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `gachicoding` ;

-- -----------------------------------------------------
-- Table `gachicoding`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`user` (
  `user_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '유저번호',
  `user_name` VARCHAR(255) NOT NULL COMMENT '유저이름',
  `user_nick` VARCHAR(255) NOT NULL COMMENT '유저별명',
  `user_email` VARCHAR(255) NOT NULL COMMENT '이메일',
  `user_password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
  `user_created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  `user_locked` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성상태',
  `user_enabled` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '인증여부',
  `user_role` VARCHAR(32) NOT NULL DEFAULT 'ROLE_USER',
  PRIMARY KEY (`user_idx`),
  UNIQUE INDEX `UIX_user` (`user_email` ASC, `user_nick` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '유저';


-- -----------------------------------------------------
-- Table `gachicoding`.`gachi_agora`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`gachi_agora` (
  `agora_idx` BIGINT UNSIGNED NOT NULL COMMENT '아고라번호',
  `user_idx` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT '작성자번호',
  `agora_title` VARCHAR(255) NOT NULL COMMENT '제목',
  `agora_contents` TEXT NOT NULL COMMENT '본문',
  `agora_thumbnail` VARCHAR(255) NOT NULL COMMENT '썸네일',
  `agora_startdate` DATETIME NOT NULL COMMENT '시작일',
  `agora_enddate` DATETIME NOT NULL COMMENT '종료일',
  `agora_regdate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  `agora_views` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '조회수',
  PRIMARY KEY (`agora_idx`),
  INDEX `FK_user_TO_gachi_agora` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_user_TO_gachi_agora`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '아고라';


-- -----------------------------------------------------
-- Table `gachicoding`.`agora_vote`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`agora_vote` (
  `vote_idx` BIGINT UNSIGNED NOT NULL COMMENT '투표번호',
  `agora_idx` BIGINT UNSIGNED NOT NULL COMMENT '아고라번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '투표자번호',
  `vote_agree` TINYINT(1) NOT NULL COMMENT '찬반',
  PRIMARY KEY (`vote_idx`),
  INDEX `FK_gachi_agora_TO_agora_vote` (`agora_idx` ASC) VISIBLE,
  INDEX `FK_user_TO_agora_vote` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_gachi_agora_TO_agora_vote`
    FOREIGN KEY (`agora_idx`)
    REFERENCES `gachicoding`.`gachi_agora` (`agora_idx`),
  CONSTRAINT `FK_user_TO_agora_vote`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '투표현황';


-- -----------------------------------------------------
-- Table `gachicoding`.`auth`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`auth` (
  `auth_token` BINARY(16) NOT NULL COMMENT '토큰',
  `auth_email` VARCHAR(255) NOT NULL COMMENT '이메일',
  `auth_regdate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `auth_expdate` DATETIME NOT NULL COMMENT '만료일시',
  `expired` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '만료여부',
  PRIMARY KEY (`auth_token`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '인증';


-- -----------------------------------------------------
-- Table `gachicoding`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`board` (
  `board_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '게시판번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '작성자번호',
  `board_title` VARCHAR(255) NOT NULL COMMENT '제목',
  `board_contents` TEXT NOT NULL COMMENT '본문',
  `board_views` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '조회수',
  `board_category` VARCHAR(20) NULL DEFAULT NULL COMMENT '유형\\\\\\\\\\\\\\\\n아직 미정 (난중에 정해야 함)',
  `board_locked` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성상태',
  `created_at` DATETIME NOT NULL COMMENT '작성일',
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`board_idx`),
  INDEX `FK_user_TO_board` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_user_TO_board`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '게시판';


-- -----------------------------------------------------
-- Table `gachicoding`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`comment` (
  `comm_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '댓글번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '작성자번호',
  `comm_contents` VARCHAR(1000) NOT NULL COMMENT '댓글내용',
  `comm_regdate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
  `comm_activated` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성여부',
  `article_category` VARCHAR(255) NOT NULL COMMENT '게시글분류',
  `article_idx` BIGINT UNSIGNED NOT NULL COMMENT '글번호',
  PRIMARY KEY (`comm_idx`),
  INDEX `FK_user_TO_comment` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_user_TO_comment`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '댓글';


-- -----------------------------------------------------
-- Table `gachicoding`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`file` (
  `file_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '파일번호',
  `article_idx` BIGINT UNSIGNED NOT NULL COMMENT '게시글번호',
  `article_category` VARCHAR(20) NOT NULL COMMENT '게시판카테고리',
  `origin_filename` VARCHAR(255) NOT NULL COMMENT '원본파일이름',
  `save_filename` VARCHAR(255) NOT NULL COMMENT '저장파일이름',
  `file_size` INT UNSIGNED NOT NULL COMMENT '파일크기',
  `file_ext` VARCHAR(20) NOT NULL COMMENT '파일확장자',
  `file_path` TEXT NOT NULL COMMENT '파일경로',
  `file_activated` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성상태',
  `file_regdate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  PRIMARY KEY (`file_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '파일';


-- -----------------------------------------------------
-- Table `gachicoding`.`gachi_q`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`gachi_q` (
  `qs_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '가치고민번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '작성자번호',
  `qs_title` VARCHAR(255) NOT NULL COMMENT '가치고민제목',
  `qs_contents` TEXT NOT NULL COMMENT '가치고민내용',
  `qs_error` TEXT NULL DEFAULT NULL COMMENT '가치고민에러',
  `qs_category` VARCHAR(30) NOT NULL COMMENT '카테고리',
  `qs_solve` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '해결여부',
  `qs_regdate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  `qs_activated` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성여부',
  PRIMARY KEY (`qs_idx`),
  INDEX `FK_user_TO_gachi_q` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_user_TO_gachi_q`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '가치고민(질문)';


-- -----------------------------------------------------
-- Table `gachicoding`.`gachi_a`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`gachi_a` (
  `as_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '가치해결번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '작성자번호',
  `qs_idx` BIGINT UNSIGNED NOT NULL COMMENT '가치고민번호',
  `as_contents` TEXT NOT NULL COMMENT '가치해결내용',
  `as_select` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '채택여부',
  `as_regdate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
  `as_activated` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성여부',
  PRIMARY KEY (`as_idx`),
  INDEX `FK_user_TO_gachi_a` (`user_idx` ASC) VISIBLE,
  INDEX `FK_gachi_q_TO_gachi_a` (`qs_idx` ASC) VISIBLE,
  CONSTRAINT `FK_gachi_q_TO_gachi_a`
    FOREIGN KEY (`qs_idx`)
    REFERENCES `gachicoding`.`gachi_q` (`qs_idx`),
  CONSTRAINT `FK_user_TO_gachi_a`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '가치해결(답변)';


-- -----------------------------------------------------
-- Table `gachicoding`.`notice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`notice` (
  `not_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '게시판번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '작성자번호',
  `not_title` VARCHAR(255) NOT NULL COMMENT '제목',
  `not_contents` TEXT NOT NULL COMMENT '본문',
  `not_views` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT '조회수',
  `not_pin` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '고정',
  `created_at` DATETIME NOT NULL COMMENT '작성일',
  `updated_at` DATETIME NOT NULL,
  `not_locked` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '활성상태',
  PRIMARY KEY (`not_idx`),
  INDEX `FK_user_TO_board` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_user_TO_board0`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '게시판';


-- -----------------------------------------------------
-- Table `gachicoding`.`social`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`social` (
  `social_idx` BIGINT UNSIGNED NOT NULL COMMENT '소셜번호',
  `user_idx` BIGINT UNSIGNED NOT NULL COMMENT '유저번호',
  `social_type` VARCHAR(20) NOT NULL COMMENT '소셜유형',
  `social_id` VARCHAR(255) NOT NULL COMMENT '소셜아이디',
  `social_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '인증일시',
  PRIMARY KEY (`social_idx`),
  INDEX `FK_user_TO_social` (`user_idx` ASC) VISIBLE,
  CONSTRAINT `FK_user_TO_social`
    FOREIGN KEY (`user_idx`)
    REFERENCES `gachicoding`.`user` (`user_idx`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '소셜';


-- -----------------------------------------------------
-- Table `gachicoding`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`tag` (
  `tag_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '태그번호',
  `tag_keyword` VARCHAR(255) NOT NULL COMMENT '키워드',
  PRIMARY KEY (`tag_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '태그';


-- -----------------------------------------------------
-- Table `gachicoding`.`tag_rel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gachicoding`.`tag_rel` (
  `rel_idx` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '관계태그번호',
  `tag_idx` BIGINT UNSIGNED NOT NULL COMMENT '태그번호',
  `article_idx` BIGINT UNSIGNED NOT NULL COMMENT '게시판번호',
  `article_category` VARCHAR(20) NOT NULL COMMENT '게시판유형',
  PRIMARY KEY (`rel_idx`),
  INDEX `FK_tag_TO_tag_rel` (`tag_idx` ASC) VISIBLE,
  CONSTRAINT `FK_tag_TO_tag_rel`
    FOREIGN KEY (`tag_idx`)
    REFERENCES `gachicoding`.`tag` (`tag_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '관계태그';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
