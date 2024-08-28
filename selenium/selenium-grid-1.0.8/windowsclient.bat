start "Selenium server" cmd.exe /c java -jar vendor/selenium-server-standalone*
ping -n 3 127.0.0.1 >nul
:while

java -jar test-cases\selenium_events_tests.jar listener

@GOTO while

:wend