/* 用户表 */
CREATE TABLE t_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name varchar(128)  COMMENT '操作员姓名',
  phone varchar(128) COMMENT '操作员手机',
  age TINYINT NOT NULL,
  salary DOUBLE NOT NULL,
  PRIMARY KEY (id)
);