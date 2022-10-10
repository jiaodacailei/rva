. ./config.sh
echo "${sql_cron} sh ${sh_root_path}batch_back_sql.sh| tee -a ${sh_log_path}batch_back_sql.log" >> /var/spool/cron/root
echo "${git_push_cron} sh ${sh_root_path}gitpush.sh| tee -a ${sh_log_path}itpush.log" >> /var/spool/cron/root
