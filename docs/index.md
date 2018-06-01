---
layout: default
---
[![Maven Central](https://img.shields.io/maven-central/v/net.java.jinput/coreapi.svg)](https://maven-badges.herokuapp.com/maven-central/net.java.jinput/coreapi)
[![Javadocs](http://www.javadoc.io/badge/net.java.jinput/coreapi.svg)](http://www.javadoc.io/doc/net.java.jinput/coreapi)

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

Include dependency in your project:

1. Maven - Easy way
-----
    ```xml
    <dependency>
        <groupId>net.java.jinput</groupId>
        <artifactId>jinput</artifactId>
        <version>{{ jinput_version }}</version>
        <type>pom</type>
    </dependency>
    ```
    You'll also need to add the build plugin in your `build/plugins` section of your pom
    ```xml
    <plugin>
        <groupId>com.googlecode.mavennatives</groupId>
        <artifactId>maven-nativedependencies-plugin</artifactId>
    </plugin>
    ```


2. Build from sources - Experts only
-----
    1. Download latest source from [here]({{ site.github.zip_url }}).
    2. Build maven root project:
        ```bash
        mvn build .
        ```
    3. Include generated artifacts to your project.

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
