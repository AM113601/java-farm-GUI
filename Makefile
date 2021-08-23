.PHONY = compile run jar runjar

JC = C:/'Program Files'/Java/jdk-11/bin/javac

JRE = C:/'Program Files'/Java/jdk-11/bin/java

MP = --module-path C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib --add-modules javafx.controls

CP = -classpath C:/Users/yhind/ateam21-final-project;C:/Users/yhind/ateam21-final-project/application;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.base.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.controls.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.fxml.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.graphics.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.media.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.swing.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx.web.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/javafx-swt.jar;C:/Users/yhind/openjfx-11.0.2_windows-x64_bin-sdk/javafx-sdk-11.0.2/lib/src.zip

SRC = application/*.java

APP = application.Main

compile:
	$(JC) $(CP) $(SRC)

run:
	$(JRE) $(MP) $(CP) $(APP)

jar:
	jar -cvmf manifest.txt executable.jar application/*.class *.jpg *.png

runjar:
	java $(MP) -jar executable.jar

