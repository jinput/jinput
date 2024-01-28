---
layout: default
---
[![Maven Central](https://img.shields.io/maven-central/v/net.java.jinput/coreapi.svg)](https://maven-badges.herokuapp.com/maven-central/net.java.jinput/coreapi)
[![Javadocs](http://www.javadoc.io/badge/net.java.jinput/coreapi.svg)](http://www.javadoc.io/doc/net.java.jinput/coreapi)
[![JInput CI](https://github.com/jinput/jinput/actions/workflows/build.yml/badge.svg)](https://github.com/jinput/jinput/actions/workflows/build.yml)

# Welcome to the Java Input API Project!

<p>The JInput Project hosts an implementation of an API for game controller
discovery and polled input.  It is part of a suite of open-source technologies
initiated by the Game Technology Group at Sun Microsystems with intention of
making the development of high performance games in Java a reality.</p>
<p>The API itself is pure Java and presents a platform-neutral
completely portable model of controller discovery and polling.
It can handle arbitrary controllers and returns both human and
machine understandable descriptions of the inputs available.</p>
<p>The implementation hosted here also includes plug-ins to allow
the API to adapt to various specific platforms.  These plug-ins
often contain a native code portion to interface to the host system.
</p>

## Getting Started

### Maven

Include the dependency in your project:
```xml
    <dependency>
        <groupId>net.java.jinput</groupId>
        <artifactId>jinput</artifactId>
        <version>{{site.jinput_version}}</version>
    </dependency>
    <dependency>
        <groupId>net.java.jinput</groupId>
        <artifactId>jinput</artifactId>
        <version>{{site.jinput_version}}</version>
        <classifier>natives-all</classifier>
    </dependency>
```
You'll also need to add the build plugin in your `build/plugins` section of your pom
```xml
    <plugin>
        <groupId>com.googlecode.mavennatives</groupId>
        <artifactId>maven-nativedependencies-plugin</artifactId>
    </plugin>
```
    
A full pom might look like [this one](https://github.com/jinput/jinput/blob/master/examples/example.pom.xml)

### Without maven
The jar file with the java code in should be in maven central, you'll need the [jinput.jar](https://repo1.maven.org/maven2/net/java/jinput/jinput/{{site.jinput_version}}/jinput-{{site.jinput_version}}.jar) and [jinput-natives-all.jar](https://repo1.maven.org/maven2/net/java/jinput/jinput/{{site.jinput_version}}/jinput-{{site.jinput_version}}-natives-all.jar) that contains the native binaries.

## Running

Add the jinput jar to your classpath, if you are using maven and have the native dependencies plugin working, it will have unpacked the native binaries to `target/natives`, you must specify the `java.library.path` property to point to this directy.

Example
```
java -cp ~/.m2/repository/net/java/jinput/jinput/{{site.jinput_version}}/jinput-{{site.jinput_version}}.jar:target/examples-pom-{{site.jinput_version}}.jar -Djava.library.path=target/natives net.java.games.input.example.ReadFirstMouse
```

More generally
```
java -cp <path to jinput.jar>:<your own jars> -Djava.library.path=<path to natives> <main class>
```

## Usage

```java
/* Create an event object for the underlying plugin to populate */
Event event = new Event();

/* Get the available controllers */
Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
for (int i = 0; i < controllers.length; i++) {
    /* Remember to poll each one */
    controllers[i].poll();

    /* Get the controllers event queue */
    EventQueue queue = controllers[i].getEventQueue();

    /* For each object in the queue */
    while (queue.getNextEvent(event)) {
        /* Get event component */
        Component comp = event.getComponent();

        /* Process event (your awesome code) */
        ...
    }
}
```

[More examples here](https://github.com/jinput/jinput/tree/master/examples/src/main/java/net/java/games/input/example).
