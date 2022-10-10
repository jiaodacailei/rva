#!/bin/bash
. ./config.sh
git_cmd="git clone -b ${git_branch} ${git_url} ${path_code}"
rm -rf ${path_code}
echo ${git_cmd} .....
${git_cmd}
