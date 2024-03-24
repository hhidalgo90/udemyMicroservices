@echo off
set RABBIT_ADDRESSES=localhost:5672
set STORAGE_TYPE=mysql
set MYSQL_DB=zipkin
set MYSQL_USER=root
set MYSQL_PASS=Hector123
java -jar ./zipkin.jar