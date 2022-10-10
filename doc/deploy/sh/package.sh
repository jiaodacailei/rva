#!/bin/bash
. ./config.sh

# jar

function package_jar() {
  cd ${path_code}
  mvn install -Dmaven.test.skip=true -U
  cp ${path_code}ruoyi-admin/target/ruoyi-admin.jar ${path_jar}/ruoyi-admin-${module}.jar
   echo "java ruoyi-admin 打包成功"
}

#vue
function webpack() {
  cd ${path_code}ruoyi-ui
  npm install webpack --legacy-peer-deps
  npm install
  npm install babel-plugin-dynamic-import-node --save-dev
  npm uninstall webpack
  npm install webpack@^4.0.0 --save-dev
  npm run build:prod
  dir_rename ${nginx_root_path}${outputDir}
  cp -r ${path_code}ruoyi-ui/${outputDir} ${nginx_root_path}
  echo "vue ${outputDir} 打包成功"
}
#uniapp-h5
function uniapp_h5() {
  rm -rf $rpp_src_path
  git clone -b master $uniapp_git_url $rpp_src_path
  rm -rf $rpp_package_path
  cp -r $rpp_src_path $rpp_package_path

  cd $rpp_package_path
  cd ..
  npm run build:h5

  dir_rename $uniapp_h5_nginx_path
  cp -r dist/build/h5 $uniapp_h5_nginx_path
  echo "uniapp_h5 ${uniapp_h5_base} 打包成功"
}
package_jar
webpack
uniapp_h5
