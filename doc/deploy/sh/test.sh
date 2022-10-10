#!/bin/bash

function mk_dir() {
  if [ ! -d "$1" ]; then
echo "filename $1 no "
    mkdir $1
else
echo "filename $1  yes"
  fi
}


mk_dir pp