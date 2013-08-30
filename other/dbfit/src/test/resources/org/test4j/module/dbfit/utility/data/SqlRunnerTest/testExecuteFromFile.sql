delete from tdd_user;
--插入数据1
insert into tdd_user(id,first_name,last_name,my_date,sarary)
values(1,'name1','last1','2011-03-18',2334);

--插入数据2
insert into tdd_user(id,first_name,last_name,my_date,sarary)
values(2,'name2','last2',/**插入注释**/'2011-03-19',2335);

/*
多行注释
多行注释
多行注释
*/
--插入数据3
insert into tdd_user(id,first_name,last_name,my_date,sarary)--本行注释
values(3,'name3','last3','2011-03-20',2336);

insert into tdd_user(id,first_name,last_name,my_date,sarary)
values(4,'name --有换行
4','last3','2011-03-20',2336);

commit;