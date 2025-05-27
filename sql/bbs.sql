
-- 1. 테이블 생성
CREATE TABLE board (
  id           NUMBER PRIMARY KEY,         -- 게시글 ID (PK)
  title        VARCHAR2(100) NOT NULL,     -- 제목
  content      CLOB NOT NULL,              -- 내용
  writer       VARCHAR2(50) NOT NULL,      -- 작성자
  created_at   DATE DEFAULT SYSDATE,       -- 작성일 (자동 설정)
  updated_at   DATE                        -- 수정일
);

-- 2. 시퀀스 생성 (자동 번호 증가용)
CREATE SEQUENCE board_seq
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

-- 3. 트리거 생성 (INSERT 시 자동 번호 할당)
CREATE OR REPLACE TRIGGER board_before_insert
BEFORE INSERT ON board
FOR EACH ROW
BEGIN
  :NEW.id := board_seq.NEXTVAL;
END;
/

-- 4. 등록 (INSERT)
INSERT INTO board (title, content, writer)
			VALUES (board_seq.NEXTVAL, '안녕하세요! 게시글 내용입니다.', '홍길동');

INSERT INTO board (id, title, content, writer)
       VALUES (board_seq.NEXTVAL, '두 번째 글', '테스트 내용입니다.', '홍길동');

-- 5. 단건 조회
SELECT id, title, content, writer, created_at, updated_at
FROM board
WHERE id = 1;

-- 6. 전체 목록 조회
SELECT id, title, writer, created_at
FROM board
ORDER BY id DESC;

-- 7. 수정 (UPDATE)
UPDATE board
SET title = '수정된 제목',
    content = '수정된 내용',
    writer = '수정자',
    updated_at = SYSDATE
WHERE id = 1;

-- 8. 단건 삭제
DELETE FROM board
WHERE id = 1;

-- 9. 여러 건 삭제
DELETE FROM board
WHERE id IN (2, 3, 4);


SELECT * FROM board;

SELECT column_name, data_type, data_length, nullable, data_default
FROM user_tab_columns
WHERE table_name = 'BOARD';

-- writer_id 컬럼 추가
ALTER TABLE board ADD writer_id NUMBER;

-- 외래키 제약조건 추가
ALTER TABLE board
  ADD CONSTRAINT fk_board_writer
  FOREIGN KEY (writer_id)
  REFERENCES member(id);

-- MEMBER 테이블
CREATE TABLE member (
  id           NUMBER PRIMARY KEY,
  email        VARCHAR2(100) UNIQUE NOT NULL,
  password     VARCHAR2(100) NOT NULL,
  nickname     VARCHAR2(50) NOT NULL,
  created_at   DATE DEFAULT SYSDATE
);

-- COMMENTS 테이블
CREATE TABLE comments (
  id           NUMBER PRIMARY KEY,
  board_id     NUMBER NOT NULL,
  commenter_id NUMBER NOT NULL,
  content      VARCHAR2(1000) NOT NULL,
  created_at   DATE DEFAULT SYSDATE,
  updated_at   DATE,
  CONSTRAINT fk_comments_board FOREIGN KEY (board_id)
    REFERENCES board(id) ON DELETE CASCADE,
  CONSTRAINT fk_comments_writer FOREIGN KEY (commenter_id)
    REFERENCES member(id)
);

-- 시퀀스 생성
CREATE SEQUENCE member_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

--트리거 생성
-- MEMBER
CREATE OR REPLACE TRIGGER member_before_insert
BEFORE INSERT ON member
FOR EACH ROW
BEGIN
  :NEW.id := member_seq.NEXTVAL;
END;
/

-- COMMENTS
CREATE OR REPLACE TRIGGER comments_before_insert
BEFORE INSERT ON comments
FOR EACH ROW
BEGIN
  :NEW.id := comments_seq.NEXTVAL;
END;
/

-- 샘플 데이터 삽입
-- 회원
INSERT INTO member (email, password, nickname)
				VALUES ('hong@kh.com', '1234', '홍길동');

INSERT INTO member (email, password, nickname)
				VALUES ('lee@kh.com', 'abcd', '이순신');

-- 댓글 (board_id = 1, member.id = 2)
INSERT INTO comments (board_id, content, commenter_id)
				VALUES (1, '좋은 글입니다.', 2);

-- 게시글 목록 + 작성자 닉네임 조회
SELECT b.id, b.title, b.content, m.nickname AS writer_nickname,
  		 b.created_at, b.updated_at
	FROM board b LEFT JOIN member m ON b.writer_id = m.id
ORDER BY b.id DESC;

-- 특정 게시글 + 작성자 + 댓글 목록 + 댓글 작성자 조회
-- 특정 게시글 1번 기준
SELECT b.id AS board_id, b.title, b.content, bm.nickname AS board_writer,
  		 b.created_at AS board_created_at, c.id AS comment_id,
			 c.content AS comment_content, cm.nickname AS comment_writer,
  		 c.created_at AS comment_created_at
	FROM board b JOIN member bm ON b.writer_id = bm.id
					LEFT JOIN comments c ON b.id = c.board_id
					LEFT JOIN member cm ON c.commenter_id = cm.id
	WHERE b.id = 1
ORDER BY c.id;

--
SELECT c.id AS comment_id, c.board_id, c.content, m.nickname AS commenter,
  		 c.created_at, c.updated_at
	FROM comments c JOIN member m ON c.commenter_id = m.id
	WHERE c.board_id = 1
ORDER BY c.id;

-----------------------------------------
--sql데이터 베이스에서 실행하기
-----------------------------------------
-- 댓글 → 게시글 → 회원 순으로 삭제
DELETE FROM comments;
DELETE FROM board;
DELETE FROM member;
COMMIT;

-- 시퀀스 삭제
DROP SEQUENCE comments_seq;
DROP SEQUENCE board_seq;
DROP SEQUENCE member_seq;

-- 시퀀스 재생성
CREATE SEQUENCE member_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE board_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

SELECT board_seq.NEXTVAL FROM dual; -- 1이 나와야 정상

-- 회원 추가
INSERT INTO member (email, password, nickname) VALUES ('tester@kh.com', '1234', '테스터');
COMMIT;

-- 게시글 등록 (트리거에서 board_seq.NEXTVAL 자동 할당)
INSERT INTO board (title, content, writer, writer_id)
VALUES ('초기화 후 첫 글', '내용입니다', '테스터', 1);
COMMIT;

-- 결과 확인
SELECT * FROM board;

-- 다음 게시글
INSERT INTO board (title, content, writer, writer_id)
VALUES ('두 번째 글', '두 번째 내용입니다', '테스터', 1);
COMMIT;

-- 결과 확인
SELECT * FROM board;

-- 다음에 생성될 ID를 확인
SELECT board_seq.NEXTVAL FROM dual;  -- 예: 8

-- 현재 board에 저장된 ID 확인
SELECT id FROM board ORDER BY id;

--실행 후 트리거에서 ctrl + enter



