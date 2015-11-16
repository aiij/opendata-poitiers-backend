# Open Data Poitiers Backend

This project is a backend for *Grand Poitiers* Open Data.
First usage is [Poitiers Vélo](https://itunes.apple.com/us/app/id1020211807?mt=8) iOS App.

To build 
```bash
./gradlew build
```

To Run 
```bash
java -jar build/libs/open.data.poitiers.bike.shelters-1.0.jar
```
Application will be available on *http://localhost:8080/*

This will create an ES Node, to skip ES Node creation, add *-DSKIP_CREATE_ES_DEV_NODE=true* option.
