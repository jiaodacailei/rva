@echo off
set NGINX_HOME=D:\software\nginx\nginx-1.14.2
set APP_HOME=%NGINX_HOME%\..\..\cigarette

echo ----install be...
rmdir /q /s %APP_HOME%
mkdir %APP_HOME%\sql
echo clean %APP_HOME% ok!

copy ..\ruoyi-admin\target\ruoyi-admin.jar %APP_HOME%
echo jar ok!

copy ..\doc\conf\application-druid.yml %APP_HOME%
copy ..\doc\conf\application.yml %APP_HOME%
echo yml ok!

copy ..\doc\conf\start.bat %APP_HOME%
echo start.bat ok!

copy ..\sql\cigarette\cigarette.sql %APP_HOME%\sql
copy ..\sql\cigarette\hsmis.sql %APP_HOME%\sql
copy ..\sql\cigarette\hsmis2ciga.sql %APP_HOME%\sql
echo sql ok!

echo ----install fe...
copy /Y ..\doc\conf\nginx.conf %NGINX_HOME%\conf
echo nginx.conf ok!

rmdir /q /s %NGINX_HOME%\html\static\cigarette-admin
mkdir %NGINX_HOME%\html\static\cigarette-admin
echo clean %NGINX_HOME%\html\static\cigarette-admin ok!

xcopy ..\ruoyi-ui\cigarette-admin %NGINX_HOME%\html\static\cigarette-admin /E /Q
echo cigarette-admin ok!

pause
