@echo off
echo �������������ѡ�dev, alpha, prd, test,test2�е�һ��
set /p env=>nul 
echo ���û�����%env%
cd %~dp0
call mvn clean package -Denv=%env%
pause