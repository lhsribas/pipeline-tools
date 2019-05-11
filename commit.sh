#!/bin/bash

echo $1

if [ !-z $1 ]; then 

  git commit -am "$1"
  git push

else

  echo "Please provide a message to execute a commit"

fi;
