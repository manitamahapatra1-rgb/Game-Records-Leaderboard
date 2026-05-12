runApplication:
        javac -cp ../junit5.jar *.java
        java App

runAllTests:
        javac -cp ../junit5.jar *.java
        java -jar ../junit5.jar -cp . -c BackendTests

clean:
        rm -f *.class