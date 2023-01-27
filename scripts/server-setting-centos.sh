#!/bin/bash

# Variables

PROJECT_ROOT_PATH="/root/hello-world-auto-store"
WEB_DRIVER_PATH="$(PROJECT_ROOT_PATH)/drivers"

# Java

## Install Open JDK 11

installOpenJdk() {
	sudo yum -y install java-11-openjdk-devel.x86_64
}

## Set JAVA_HOME

setJavaHome() {
	echo export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac)))) >> /etc/profile
	source /etc/profile
}

# Gradle

## Install Gradle-7.1

installGradle() {
	wget https://services.gradle.org/distributions/gradle-7.1-bin.zip -P /tmp
	sudo unzip -d /opt/gradle /tmp/gradle-7.1-bin.zip
}

## Set GRADLE_HOME, PATH

setGradleHome() {
	echo export GRADLE_HOME=/opt/gradle/gradle-7.1 >> /etc/profile
	echo export PATH=${GRADLE_HOME}/bin:${PATH} >> /etc/profile

	source /etc/profile
}

# Install Firefox
installFirefox() {
	sudo yum -y install firefox
}

# Install Gecko Driver
installGeckoDriver() {
  TMP_DIR=/tmp
	wget https://github.com/mozilla/geckodriver/releases/download/v0.30.0/geckodriver-v0.30.0-linux64.tar.gz -P $(TMP_DIR)
	tar -xvf $(TMP_DIR)/geckodriver-v0.30.0-linux64.tar.gz
	mkdir -p $(WEB_DRIVER_PATH)
	mv $(TMP_DIR)/geckodriver $(WEB_DRIVER_PATH)
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


