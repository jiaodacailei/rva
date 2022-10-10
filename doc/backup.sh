#!/bin/bash
# 设置mysql的登录用户名和密码(根据实际情况填写)
mysql_user="root"
mysql_password="ca1le1"
mysql_host="localhost"
mysql_port="3306"
mysql_db="ry-vue"
mysql_charset="utf8"

# 备份文件存放地址(根据实际情况填写)
backup_location=/root/db_backup

# 是否删除过期数据
expire_backup_delete="ON"
expire_days=15
backup_time=`date +%Y%m%d%H%M`
backup_dir=$backup_location
welcome_msg="Welcome to use MySQL backup tools!"

# 判断mysql实例是否正常运行
mysql_ps=`ps -ef |grep mysql |wc -l`
mysql_listen=`netstat -an |grep LISTEN |grep $mysql_port|wc -l`
if [ [$mysql_ps == 0] -o [$mysql_listen == 0] ]; then
        echo "ERROR:MySQL is not running! backup stop!"
        exit
else
        echo $welcome_msg
fi

# 备份指定数据库中数据
mysqldump -h$mysql_host -P$mysql_port -u$mysql_user -p$mysql_password -B $mysql_db > $backup_dir/$mysql_db-$backup_time.sql
flag=`echo $?`
if [ $flag == "0" ];then
        echo "database $mysql_db success backup to $backup_dir/$mysql_db-$backup_time.sql"
else
        echo "database $mysql_db backup failed!"
fi

# 删除过期数据
if [ "$expire_backup_delete" == "ON" -a  "$backup_location" != "" ];then
        `find $backup_location/ -type f -mtime +$expire_days | xargs rm -rf`
        echo "Expired backup data delete complete!"
fi

# 创建cron任务，定时执行backup.sh脚本
# crontab -e
# 添加定时任务(每天07:30至23:30，每格四个小时执行备份操作)
# 30 07,11,15,19,23 * * * cd /root/;sh backup.sh >> ry-vue.txt 2>>ry-vue.txt

# 该文件的换行符需要修改为：LF
# 文件上传linux后，需要设置可执行权限