@echo off
cd /d C:\Users\Admin\library-management-system\lms-backend
set SPRING_PROFILES_ACTIVE=dev
set DB_PASSWORD=
mvn spring-boot:run
pause
