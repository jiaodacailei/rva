# 子目录名称
module="taiyo-admin"
#数据库是否覆盖 0-强制覆盖 1-不覆盖且退出脚本 2-不处理数据库
mysql_db_overwrite="1"
#源码项目名
project_name="taiyo"
# git
#源码
git_url="https://gitee.com/jiaodacailei/rva.git"
git_branch="dev"
#文件备份 master
git_url_file_backup="https://gitee.com/ma-fengyun/rva-backup.git"
# mysql
mysql_user="root"
mysql_password="ca1le1"
mysql_host="localhost"
mysql_port="3306"
mysql_db="taiyo"
mysql_charset="utf8"
# redis
redis_host="192.168.1.102"
redis_port="6379"
redis_database="10"
redis_password="ca1le1"
# java
server_ip="192.168.1.102"
server_port="8085"
java_bin="/usr/local/jdk1.8.0_20/bin/java"
# nginx
nginx_port="80"
nginx_server_name="localhost"
nginx_root_path="/home/html/static"
#uniapp
uniapp_h5_base="taiyo"
rpp_src_path="/home/src/rpp/"
rpp_package_path="/home/uniapp/rpp/src/"
uniapp_h5_nginx_path="${nginx_root_path}/${uniapp_h5_base}"
# master
uniapp_git_url="https://gitee.com/jiaodacailei/rpp.git"
#mail
email_address_succeed="jiaodacailei@sina.com,2278611806@qq.com"
email_address_failed="jiaodacailei@sina.com,2278611806@qq.com"

#####以下配置不用修改
# sql备份路径

##源码路径
path_code="/home/src/${project_name}/"
root_upload_path="/home/html/uploadPath/backup/"

if [ -z "${module}" ]; then
  publicPath="/"
  outputDir="dist"
  ##jar-path
  path_jar="/home/service/${project_name}/default"
  jar_all_jar="${path_jar}/ruoyi-admin-default.jar"
  upload_path="${root_upload_path}${mysql_db}/"

else
  publicPath="/${module}/"
  outputDir="${module}"
  path_jar="/home/service/${project_name}/${module}"
  jar_all_jar="${path_jar}/ruoyi-admin-${module}.jar"
  upload_path="${root_upload_path}${module}/"

fi
nginx_path="/usr/local/nginx/"
sh_root_path="/home/deploy/sh/"
sql_back_path="${upload_path}/sql/"
sql_sh_back_path="/home/deploy/backup/sh/"
sql_sh_back_path_project="${sql_sh_back_path}${mysql_db}-${module}"
sh_log_path="/home/deploy/logs/"

#定时任务
#sql定时 (每天 07:30/11:30/15:30/19:30/23:30)
sql_cron="30 07,11,15,19,23 * * *"
#文件备份定时 (每天11:59/23:59)
git_push_cron="59 11,23 * * * "
#目录存在重命名
function dir_rename() {
  if [ -d $1 ]; then
    mv $1 "$1-$(date +%Y-%m-%d-%H%M%S)"
  fi
}

#目录不存在则新建
function mk_dir() {
   if [ ! -d "$1" ]; then
    mkdir $1
  fi
}


