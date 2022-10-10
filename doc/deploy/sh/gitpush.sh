#!/bin/bash
#  cd ${root_upload_path}
#  cd ..
#  init
#  git init
#  git remote add origin https://gitee.com/ma-fengyun/rva-backup.git

function push() {
  cd /home/html/uploadPath/
  git add .
  git commit -m " file backup"
  git pull origin master
  git push origin master
}
push

if [ $? -eq 0 ]; then
  echo "git备份成功" | mail -s "git备份成功" jiaodacailei@sina.com,2278611806@qq.com
else
  tail -n 100 /home/deploy/logs/gitpush.log | mail -s "git备份失败" jiaodacailei@sina.com,2278611806@qq.com
fi
