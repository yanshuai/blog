---
title: 忘记MySQL root密码解决办法
perex: 当忘记mysql root密码时，重新设置root密码的详细步骤
layout: post
---

# 忘记密码
1. ```service mysqld stop```终止mysql服务
1. ```safe_mysqld --skip-grant-tables &```使用skip-grant-tables选项启动MySQL
1. 此时可不使用密码登录MySQL，更新mysql.user表的root密码
```
$mysql -uroot
mysql> use mysql
mysql> update user set password=password('<new password>') where user='root'
mysql> flush privileges
mysql> exit
```
1. ```service mysqld restart```重启mysql服务。
