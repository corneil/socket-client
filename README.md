# Simple Socket Client

## Build

This will build an application jar and a native version using GraalVM

```shell
./gradlew build nativeCompile
```
Remove nativeCompile if you don't have GraalVM installed or don't require a native image.

## Execute

* The default value for _**port**_ is `9000`
* The default value for _**host**_ is `localhost`

## Application Jar

```shell
java -jar ./build/libs/socket-client-1.0-SNAPSHOT.jar [host] [port]
```

## Distribution
Expand `build/distributions/socket-client-1.0-SNAPSHOT.zip` or `build/distributions/socket-client-1.0-SNAPSHOT.tar`

```shell
cd build/distributions
unzip socket-client-1.0-SNAPSHOT.zip
cd socket-client-1.0-SNAPSHOT/bin
./socket-client [host] [port]
```
## Native

```shell
./build/native/nativeCompile/socket-client [host] [port]
```


