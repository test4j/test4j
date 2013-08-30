SELECT s.username,
decode(l.type,'TM','TABLE LOCK','TX','ROW LOCK',NULL) LOCK_LEVEL,
o.owner,o.object_name,o.object_type,
s.sid,s.serial#,s.terminal,s.machine,s.program,s.osuser
FROM v$session s,v$lock l,dba_objects o
WHERE l.sid = s.sid
AND l.id1 = o.object_id(+)
AND s.username is NOT Null
and o.OWNER = 'APOLLO_TEST';

#死锁检测
select username,lockwait,status,machine,program from v$session where sid in 
(select session_id from v$locked_object)

select  a2.MACHINE,a3.os_user_name,a2.PROGRAM,a2.SCHEMANAME,a1.START_TIME,a4.object_name,a2.sid,a2.SERIAL#
  from v$transaction a1,v$session a2,v$locked_object a3,dba_objects a4
  where a1.ADDR = a2.TADDR and a3.SESSION_ID = a2.SID and a3.OBJECT_ID = a4.object_id
 and a2.TYPE = 'USER'

#死锁语句
select sql_text from v$sql where hash_value in 
(select sql_hash_value from v$session where sid in 
(select session_id from v$locked_object))

2）kill掉这个死锁的进程：
　　alter system kill session ‘sid,serial#’; （其中sid=l.session_id）

3）如果还不能解决：
select pro.spid from v$session ses,
v$process pro where ses.sid=XX and 
ses.paddr=pro.addr;

#查询连接会话
 select sid,SERIAL#,logon_time,username,machine from v$session order by machine; 
 
 ALTER SYSTEM KILL SESSION '196,16194' immediate; 