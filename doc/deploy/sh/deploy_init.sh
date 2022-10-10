#!/bin/bash
sh ./git.sh
sh ./init_mysql.sh
if [ $? -ne 0 ]; then
  exit 1
fi

sh ./hander_config.sh
sh ./package.sh
sh ./start.sh

#����
sh backup_prepare.sh
