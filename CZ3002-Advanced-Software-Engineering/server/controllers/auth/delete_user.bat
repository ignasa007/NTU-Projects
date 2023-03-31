@echo off
set username=%2

IF "%~1" == "delete" (
    aws cognito-idp admin-delete-user^
        --profile cz2006^
        --region us-east-1^
        --user-pool-id us-east-1_2sEbwjNY2^
        --username %username%
)