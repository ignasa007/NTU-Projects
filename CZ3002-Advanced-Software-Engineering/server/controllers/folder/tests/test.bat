@echo off
set start=%1
set stop=%2

IF %start% LEQ 1 (IF 1 LEQ %stop% (node test_read.js))
IF %start% LEQ 2 (IF 2 LEQ %stop% (node ../../../index.js))