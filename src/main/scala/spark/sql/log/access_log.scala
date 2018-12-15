package spark.sql.log


/*
create table access_log (
url varchar(64) not null,
cmsType varchar(16) not null,
cmsId varchar(16) not null,
traffic bigint(10) not null,
ip varchar(32) not null,
city varchar(16) not null,
time varchar(64) not null,
day varchar(64) not null
*/


case class access_log(url:String,cmsType:String,cmsId:String,traffic:Int,ip:String,city:String,time:String,day:String)
