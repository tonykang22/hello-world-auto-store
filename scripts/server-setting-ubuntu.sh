#!/bin/bash

# Variables

PROJECT_ROOT_PATH="/root/hello-world-auto-store"
WEB_DRIVER_PATH=$PROJECT_ROOT_PATH/drivers
DOWNLOAD_TMP_PATH="/tmp"

# Java
## Install Open JDK 11
installOpenJdk() {
	sudo apt-get install -y openjdk-11-jre-headless
}

## Set JAVA_HOME
setJavaHome() {
	echo export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac)))) >> /etc/profile
	source /etc/profile
}

# Gradle
## Install Gradle-7.1
installGradle() {
	wget https://services.gradle.org/distributions/gradle-7.1-bin.zip -P $DOWNLOAD_TMP_PATH
	sudo unzip -d /opt/gradle $DOWNLOAD_TMP_PATH/gradle-7.1-bin.zip
}

## Set GRADLE_HOME, PATH

setGradleHome() {
	echo export GRADLE_HOME=/opt/gradle/gradle-7.1 >> /etc/profile
	echo export PATH=${GRADLE_HOME}/bin:${PATH} >> /etc/profile

	source /etc/profile
}

# Install Firefox
installFirefox() {
	sudo apt-get install -y firefox
}

# Install Gecko Driver
installGeckoDriver() {
	wget https://github.com/mozilla/geckodriver/releases/download/v0.30.0/geckodriver-v0.30.0-linux64.tar.gz -P $DOWNLOAD_TMP_PATH
	tar -xvf $DOWNLOAD_TMP_PATH/geckodriver-v0.30.0-linux64.tar.gz -C $DOWNLOAD_TMP_PATH
	mkdir -p $WEB_DRIVER_PATH
	mv $DOWNLOAD_TMP_PATH/geckodriver $WEB_DRIVER_PATH
}

# Main
## Set Java
installOpenJdk
setJavaHome

## Set Gradle
installGradle
setGradleHome

## Set Firefox
installFirefox

## Set Gecko Driver
installGeckoDriver


