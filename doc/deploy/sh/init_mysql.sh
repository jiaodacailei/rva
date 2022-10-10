#!/bin/bash

. ./config.sh

LOGIN_CMD="mysql -h$mysql_host  -P$mysql_port  -u$mysql_user  -p$mysql_password"

echo ${LOGIN_CMD}

create_database() {
  count_sql="SELECT  COUNT(*)  AS count FROM  information_schema.SCHEMATA where SCHEMA_NAME = '${mysql_db}'"
  $LOGIN_CMD -e "${count_sql}"
  count=$($LOGIN_CMD -e "${count_sql}" | awk '{print $1}' | sed -n '2p')
  if test $count -eq 1; then
    #数据库存在
    if [ $mysql_db_overwrite -eq 0 ]; then
      backup
      echo "database $mysql_db exist  and  mysql_db_overwrite 为  $mysql_db_overwrite -强制覆盖"
      drop_db_sql="drop database ${mysql_db}"
      echo $drop_db_sql
      echo ${drop_db_sql} | ${LOGIN_CMD}
      create_database
    else
      echo "database $mysql_db exist  and  mysql_db_overwrite 为  $mysql_db_overwrite -不覆盖且退出脚本"
      echo "End of the script"
      exit 1
    fi
  else
    #数据库不存在
    create_db_sql="create database ${mysql_db}  character set utf8"
    echo $create_db_sql
    echo ${create_db_sql} | ${LOGIN_CMD}
    if [ $? -eq 0 ]; then
      echo "succeed to create database ${mysql_db}"
      import_db
    fi
  fi
}

import_db() {
  mysql -h$mysql_host -P$mysql_port -u$mysql_user -p$mysql_password -D${mysql_db} <${path_code}sql/rva_template.sql
}
backup() {
  echo "backup to $sql_back_path"
  mysqldump -h$mysql_host -P$mysql_port -u$mysql_user -p$mysql_password -B $mysql_db >$sql_back_path$mysql_db-$(date +%Y-%m-%d-%H%M%S).sql
}

if [ "$mysql_db_overwrite" -ne 2 ]; then
  create_database
fi

