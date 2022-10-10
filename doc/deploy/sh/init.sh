#!/bin/bash
. ./config.sh
file_name="$(date +%Y-%m-%d-%H%M%S).log"
sh ./deploy_init.sh | tee -a "../logs/$file_name"

if [ $? -eq 0 ]; then
  echo -e " 前端: http://36nn869333.qicp.vip/${uniapp_h5_base}\n 后台: http://36nn869333.qicp.vip/${module}" | mail -s "${module}部署成功" $email_address_succeed
else
  tail -n 1000 ../logs/$file_name | mail -s "${module}-部署失败" $email_address_failed
fi
