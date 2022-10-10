#!/bin/bash
echo "hander config yml/js/nginx.conf"
. ./config.sh

template_dir="/home/deploy/template/"

cur_nginx_path_conf="${nginx_path}/conf/"

function getTemplates() {
  rm -rf ${template_dir}
  cp -r ${path_code}doc/deploy/template ${template_dir}
}

update_java_application() {
  # application.yml
  sed -i "s#@@redis_host#${redis_host}#g" ${template_dir}application.yml
  sed -i "s#@@redis_port#${redis_port}#g" ${template_dir}application.yml
  sed -i "s#@@redis_database#${redis_database}#g" ${template_dir}application.yml
  sed -i "s#@@redis_password#${redis_password}#g" ${template_dir}application.yml
  sed -i "s#@@server_port#${server_port}#g" ${template_dir}application.yml
  sed -i "s#@@profile#${upload_path}#g" ${template_dir}application.yml
  # application-druid.yml
  sed -i "s#@@druid_master_url#${mysql_host}:${mysql_port}/${mysql_db}#g" ${template_dir}application-druid.yml
  sed -i "s#@@druid_master_username#${mysql_user}#g" ${template_dir}application-druid.yml
  sed -i "s#@@druid_master_password#${mysql_password}#g" ${template_dir}application-druid.yml
  copy_java_config
}

update_vue_multi() {
  #index.js
  sed -i "s#@@base#/${module}#g" ${template_dir}index.js
  #vue.config.js
  sed -i "s#@@publicPath#${publicPath}#g" ${template_dir}vue.config.js
  sed -i "s#@@outputDir#${outputDir}#g" ${template_dir}vue.config.js
  #.env.production
  sed -i "s#@@module#/${module}#g" ${template_dir}.env.production

  # cp
  cp ${template_dir}".env.production" ${path_code}ruoyi-ui/.env.production
  cp ${template_dir}index.js ${path_code}ruoyi-ui/src/router/index.js
}

#vue.config.js
update_vue_api() {
  sed -i "s#@@api#http://${server_ip}:${server_port}#g" ${template_dir}vue.config.js
  cp ${template_dir}vue.config.js ${path_code}ruoyi-ui/vue.config.js
}

update_nginx_single() {
  #nginx-single.conf
  sed -i "s#@@port#${nginx_port}#g" ${template_dir}nginx-single.conf
  sed -i "s#@@server_name#${nginx_server_name}#g" ${template_dir}nginx-single.conf
  sed -i "s#@@proxy_pass#http://${server_ip}:${server_port}#g" ${template_dir}nginx-single.conf
  sed -i "s#@@nginx_root_path#${nginx_root_path}#g" ${template_dir}nginx-single.conf
  mv ${cur_nginx_path_conf}nginx.conf "${cur_nginx_path_conf}nginx_$(date +%Y-%m-%d-%H%M%S).conf"
  cp ${template_dir}nginx-single.conf ${cur_nginx_path_conf}nginx.conf
}

is_exist_str() {
  cat "$1" | grep "$2"
  return $?
}
delete_location_admin() {
  location_key_admin="/${module}/prod-api/"
  is_exist_str "${cur_nginx_path_conf}nginx.conf" "$location_key_admin"
  if [ "$?" ]; then
    start_row=$(grep -n $location_key_admin "${cur_nginx_path_conf}nginx.conf" | head -1 | cut -d ":" -f 1)
    end_row=$(($start_row + 2))
    rows="$start_row,${end_row}d"
    echo $rows
    sed -i $rows "${cur_nginx_path_conf}nginx.conf"
  fi
}
delete_location_h5() {
  location_key_h5="/${uniapp_h5_base}/dev_api/"
  is_exist_str "${cur_nginx_path_conf}nginx.conf" "$location_key_h5"
  if [ "$?" ]; then
    start_row=$(grep -n $location_key_h5 "${cur_nginx_path_conf}nginx.conf" | head -1 | cut -d ":" -f 1)
    end_row=$(($start_row + 2))
    rows="$start_row,${end_row}d"
    echo $rows
    sed -i $rows "${cur_nginx_path_conf}nginx.conf"
  fi
}


delete_nginx_repeat_key() {
  is_exist_str "${cur_nginx_path_conf}nginx.conf" "@@location"
  if [ "$?" ]; then
    #多子目录
    delete_location_admin
    delete_location_h5
  fi
}

update_nginx_multi() {
  is_exist_str "${cur_nginx_path_conf}nginx.conf" "@@location"
  if [ "$?" ]; then
    #nginx-multi.conf append
    cp "${cur_nginx_path_conf}nginx.conf" "${cur_nginx_path_conf}nginx-multi_$(date +%Y-%m-%d-%H%M%S).conf"
    delete_nginx_repeat_key
  else
    mv "${cur_nginx_path_conf}nginx.conf" "${cur_nginx_path_conf}nginx-single_$(date +%Y-%m-%d-%H%M%S).conf"
    sed -i "s#@@nginx_root_path#${nginx_root_path}#g" ${template_dir}nginx-multi.conf
    cp "${template_dir}nginx-multi.conf" "${cur_nginx_path_conf}nginx.conf"

  fi
  sed -i "s#@@location#& \n        location /${module}/prod-api/ { \n               proxy_pass http://${server_ip}:${server_port}/;     \n        }#g" ${cur_nginx_path_conf}nginx.conf
  sed -i "s#@@location#& \n        location /${uniapp_h5_base}/dev_api/ { \n               proxy_pass http://${server_ip}:${server_port}/;     \n        }#g" ${cur_nginx_path_conf}nginx.conf

}
copy_java_config() {
  # cp
  echo "cp ${template_dir}application.yml ${path_jar}"
  dir_rename ${path_jar}
  mkdir -p ${path_jar}
  cp ${template_dir}application.yml ${path_jar}
  cp ${template_dir}application-druid.yml ${path_jar}
  echo "copy_java_config-----------"
}
# module.service  java_bin=$(which java)
update_java_start_service() {
  sed -i "s#@@java_bin#${java_bin}#g" ${template_dir}module.service
  sed -i "s#@@module#${module}#g" ${template_dir}module.service
  sed -i "s#@@path_jar#${path_jar}#g" ${template_dir}module.service
  sed -i "s#@@jar_all_path#${path_jar}/ruoyi-admin-${module}.jar#g" ${template_dir}module.service
  /bin/cp -rf ${template_dir}module.service /usr/lib/systemd/system/${module}.service
}

getTemplates
if [ -z "${module}" ]; then
  update_nginx_single
else
  update_vue_multi
  update_nginx_multi
#cp
fi

update_java_application
update_vue_api
update_java_start_service

echo "hander_config sucsess-----"
