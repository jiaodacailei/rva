#!/bin/bash
sql_sh_back_path="/home/deploy/backup/sh/"
function sql_back() {
  cd ${sql_sh_back_path}
  for file in ./*; do
    if test -d $file; then
      cd $file
      _work=$(pwd)
      echo $_work
      echo "cd $file"
      sh ./sqlbackup.sh
      cd ..
    fi
  done
}

sql_back

if [ $? -eq 0 ]; then
  echo "sql备份成功" | mail -s "sql备份成功" jiaodacailei@sina.com,2278611806@qq.com
else
  tail -n 1000 /home/deploy/logs/batch_back_sql.log | mail -s "sql备份失败" jiaodacailei@sina.com,2278611806@qq.com
fi
