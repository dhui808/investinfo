set LIB_PATH=target\lib
set LIB=%LIB_PATH%\jna-5.6.0.jar;%LIB_PATH%\jna-platform-5.6.0.jar
set LIBREOFFICE_PATH="C:\Program Files\LibreOffice"\program\classes
set LIBREOFFICE_LIB=%LIBREOFFICE_PATH%\libreoffice.jar;%LIBREOFFICE_PATH%\unoloader.jar;%LIBREOFFICE_PATH%\officebean.jar
set CP=target\classes;..\jloputility\target\classes;%LIB%;%LIBREOFFICE_LIB%;%LIB_PATH%\jsoup-1.18.3.jar;%LIB_PATH%\commons-io-2.18.0.jar;%LIB_PATH%\jackson-core-2.19.1.jar;%LIB_PATH%\jackson-databind-2.19.1.jar;%LIB_PATH%\jackson-annotations-2.19.1.jar;%LIB_PATH%\mysql-connector-j-9.1.0.jar;%LIB_PATH%\commons-lang3-3.11.jar
"%JAVA_HOME%/bin/java" -classpath %CP% cftc.Main %1 %2 %3 %4 %5 %6
