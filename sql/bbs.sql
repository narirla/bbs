
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