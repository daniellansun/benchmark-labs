@echo off
gradlew clean jmh --no-build-cache --no-daemon -Pclassifier=%1
