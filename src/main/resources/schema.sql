DROP TABLE IF EXISTS todo;

CREATE TABLE todo
(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(255) NOT NULL,
    isCompleted CHARACTER NOT NULL
);

Insert Into TODO (CONTENT, ISCOMPLETED) values ('Study Spring', 'N');
Insert Into TODO (CONTENT, ISCOMPLETED) values ('Study MyBatis', 'N');