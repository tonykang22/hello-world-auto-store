#!/bin/bash

## Variables
ROOT_PATH="/root"
PROJECT_PATH="$ROOT_PATH/hello-world-auto-store"
MODULE_NAME="product-analyzer"
MODULE_PATH="$PROJECT_PATH/$MODULE_NAME"
JAR_PATH="$MODULE_PATH/build/libs/$MODULE_NAME-1.0-SNAPSHOT.jar"
WEB_BROWSER_NAME="firefox"
WEB_DRIVER_NAME="geckodriver"

## Move to MODULE_PATH
moveToModulePath() {
  cd $MODULE_PATH
}

## Build
build() {
  ./gradlew build
}

## Shutdown Jar
shutdownJar() {
  ps -ef | grep $JAR_PATH | grep -v "grep" | awk '{print $2}' | xargs -i{} kill -9 {}
}

## Shutdown web resources
shutdownWebResources() {
  ps -ef | grep $WEB_BROWSER_NAME | grep -v "grep" | awk '{print $2}' | xargs -i{} kill -9 {}
  ps -ef | grep $WEB_DRIVER_NAME | grep -v "grep" | awk '{print $2}' | xargs -i{} kill -9 {}
}

## Make file paths
makeFilePaths() {
  mkdir -p $PROJECT_PATH/output-file/select-scores/naver-category
  mkdir -p $PROJECT_PATH/output-file/select-scores/panda-rank
  mkdir -p $PROJECT_PATH/output-file/sourcing-results/naver-category
  mkdir -p $PROJECT_PATH/output-file/sourcing-results/panda-rank
  mkdir -p $PROJECT_PATH/output-file/select-score-context/naver-category
  mkdir -p $PROJECT_PATH/output-file/select-score-context/panda-rank
  mkdir -p $PROJECT_PATH/logs
}

## Move to PROJECT_PATH
moveToProjectPath() {
  cd $PROJECT_PATH
}

## Exec Jar
execJar() {
  nohup java -jar -Dspring.profiles.active=prod $JAR_PATH
}

## Check process start
checkProcessStart(){
  sleep 10 && ps -ef | grep $JAR_PATH | grep -v "grep" | wc -l | awk '{ if($1 != "1") {print "Process cannot started"; exit 1;} else {print "Process start successfully"; exit 0;}}'
  sleep 10 && ps -ef | grep $WEB_DRIVER_NAME | grep -v "grep" | wc -l | awk '{ if($1 == "0") {print "Driver cannot started"; exit 1;} else {print "Driver start successfully"; exit 0;}}'
}

## Main
### Move to Module Path
moveToModulePath

### Pull & Build
build

### Shutdown
shutdownJar
shutdownWebResources

### Make Dirs
makeFilePaths

### Move to Project Path
moveToProjectPath

### Exec
execJar

### Check
checkProcessStart