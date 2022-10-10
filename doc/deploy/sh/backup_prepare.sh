#!bin/bash

. ./config.sh
#初始化git仓库
function init_git(){
  cd $upload_path
  git init
  git add .
  git remote add origin ${git_url_file_backup}
  echo "初始化git仓库：${git_url_file_backup} 成功"
}
#初始化sql备份脚本
function init_sql_backup_sh() {
    cd $sql_sh_back_path_project
    cp -f ${sh_root_path}/config.sh ./
    cp -f ${sh_root_path}/sqlbackup.sh ./
    echo "初始化sql备份脚本：${sh_root_path} 成功"
}
function mk_dirs() {
  mk_dir $sql_sh_back_path_project
  mk_dir $upload_path
  mk_dir $sql_back_path
}
mk_dirs
init_git
init_sql_backup_sh