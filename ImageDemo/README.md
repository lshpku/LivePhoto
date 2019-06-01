### LivePhoto

##### 建表

进入mysql进入如下命令

```mysql
create table account(id int not null auto_increment primary key, name varchar(50)) engine=InnoDB default charset=utf8 collate=utf8_general_ci;

create table news(id int not null auto_increment primary key, title varchar(50) comment '标题', intro varchar(1000) comment '新闻介绍', photos LongBlob comment '照片集合', account_id int, news_time datetime, foreign key(account_id) references account(id) on update cascade on delete cascade) engine=InnoDB default charset=utf8 collate=utf8_general_ci;

create table photo( id int not null auto_increment primary key, name varchar(100) comment '名 称', photo LongBlob comment '照片', intro varchar(1000) comment '介绍', news_id int, foreign key(news_id) references news(id) on update cascade on delete cascade) engine=InnoDB default charset=utf8 collate=utf8_general_ci;
```

或在终端输入，建立LivePhoto数据库

```bash
mysql -h [IP地址] -u [用户名] -p LivePhoto < create_tables.sql
```
