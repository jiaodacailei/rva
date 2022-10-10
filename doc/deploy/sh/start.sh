#!/bin/bash

. ./config.sh

pid="$(netstat -nlp | grep :${server_port} | awk '{print $7}' | awk -F"/" '{ print $1 }')"
# java
if [ -n "$pid" ]; then
  kill -9 $pid
fi
systemctl daemon-reload
systemctl start ${module}.service

sleep 1m
java_pid="$(netstat -nlp | grep :${server_port} | awk '{print $7}' | awk -F"/" '{ print $1 }')"
# java
if [ -n "$java_pid" ]; then
  echo "java 启动成功"
else
  echo "java 启动启动失败 请查看日志 ： journalctl -f"
  exit 3
fi

function nginx_start() {
  # nginx
  nginx_pid= "$(ps -ef | grep nginx | awk '{print $2}' | sed -n '2p')"
  if [ -n "${nginx_pid}" ]; then
    kill -9 ${nginx_pid}
  fi
  /usr/local/nginx/sbin/nginx
}


function nginx_reload() {
  nginx -s reload
  if [ $? -eq 0 ]; then
    echo "nginx reload success ................"
  else
   nginx_start
  fi
}
nginx_reload
