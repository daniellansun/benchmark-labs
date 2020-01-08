@echo off
gradlew clean jmh --no-build-cache -Pclassifier=%1
