CREATE TABLE board (
    id          NUMBER PRIMARY KEY,
    title       VARCHAR2(200) NOT NULL,
    content     CLOB NOT NULL,
    author      VARCHAR2(100) NOT NULL,
    createdDate  DATE DEFAULT SYSDATE,
    updatedDate  DATE
);

CREATE SEQUENCE board_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

INSERT INTO board (id, title, content, author)
		VALUES (board_seq.NEXTVAL, '첫 번째 게시글', '게시글 내용입니다.', '홍길동');

INSERT INTO board (id, title, content, author)
		VALUES (board_seq.NEXTVAL, '두 번째 글', '내용 테스트', '홍길동1');
