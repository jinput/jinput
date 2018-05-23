---
layout: default
---
[![Maven Central](https://img.shields.io/maven-central/v/net.java.jinput/jinput.svg)](https://maven-badges.herokuapp.com/maven-central/net.java.jinput/jinput)
[![Javadocs](http://www.javadoc.io/badge/net.java.jinput/jinput.svg)](http://www.javadoc.io/doc/net.java.jinput/jinput)

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
        <version>2.0.7</version>
    </dependency>
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

