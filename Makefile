all:
	javac src/WebServer/*.java src/DBInterface/*.java src/LivePhoto.java src/BackGround/*.java
run:
	java -classpath src LivePhoto
runb:
	nohup java -classpath src LivePhoto &
pid:
	ps -aux | grep java
init:
	java -classpath src DBInterface.DBInit