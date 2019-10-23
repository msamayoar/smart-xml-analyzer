# smart-xml-analyzer

HTML and XML analyzer using on top of [Jsoup](https://jsoup.org/).
This program will find all the <a> tag Elements with the attribute id="make-everything-ok-button" in the original html file and will try to find all 
the elements tags with the same text "Everything OK" and will check if the attributes have any differences.

If any differences are found, it will show the xpath of the <a> Element along with the differences.

# Build
Make sure you have installed java 1.8 and Maven 3.6.0.
To build the jar with the depedencies run the following command.
```sh
$ cd xml-analyzer
$ mvn clean install assembly:single
```

# Run the application
Make sure you are located where the .jar executable file is.
Then you need to execute the following command and provide two argments:
```sh
$ java -jar xml-analyzer-1.0-SNAPSHOT.jar path-origina-file.html path-diff-file.html
```
