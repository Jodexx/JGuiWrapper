# JGuiWrapper

> A library for creating customizable GUIs on **PaperMC 1.16.5–1.21.10** servers

[![Release](https://github.com/Jodexx/JGuiWrapper/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/Jodexx/JGuiWrapper/actions/workflows/gradle-publish.yml)
[![JGuiWrapper API version](https://jitpack.io/v/Jodexx/JGuiWrapper.svg)](https://jitpack.io/#Jodexx/JGuiWrapper)
---

## [Wiki](https://wiki.jodex.xyz/JGuiWrapper/jguiwrapper-start)

## Requirements

- <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="16" width="16" alt="Java icon"> <b>Java 16</b> or higher
- <img src="https://assets.papermc.io/brand/papermc_logo.min.svg" height="16" width="16" alt="PaperMC icon"> <b>Paper 1.16.5</b> or higher

## Installation and Usage

Snippets are available for Maven, Gradle Groovy, and Gradle Kotlin DSL.

There are **two ways** to use the library depending on your project structure:

### 1. Using as a standalone plugin (`api` module)

* JGuiWrapper is installed as a separate plugin on the server.
* Your plugin simply connects to its API **without shading**.

##### Maven

```xml
<repository>
    <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.Jodexx.JGuiWrapper</groupId>
  <artifactId>api</artifactId>
  <version>v1.0.0.9-beta</version>
</dependency>
```

##### Gradle (Groovy DSL)

```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Jodexx.JGuiWrapper:v1.0.0.9-beta'
}
```

##### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Jodexx.JGuiWrapper:api:v1.0.0.9-beta")
}
```

### 2. Embedding into your project (`common` module)

* You directly include the library and **shade** it into the final jar using `shade` (or `shadowJar`).
* No need to install JGuiWrapper as a separate plugin.

##### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.Jodexx.JGuiWrapper</groupId>
  <artifactId>common</artifactId>
  <version>v1.0.0.9-beta</version>
</dependency>
<!-- if you want to include nms logic for title management -->
<dependency>
  <groupId>com.github.Jodexx.JGuiWrapper</groupId>
  <artifactId>nms</artifactId>
  <version>v1.0.0.9-beta</version>
</dependency>
```

##### Gradle (Groovy DSL)

```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Jodexx.JGuiWrapper:common:v1.0.0.9-beta'
    implementation 'com.github.Jodexx.JGuiWrapper:nms:v1.0.0.9-beta' // if you want to include nms logic for title management 
}
```

##### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Jodexx.JGuiWrapper:common:v1.0.0.9-beta")
    implementation("com.github.Jodexx.JGuiWrapper:nms:v1.0.0.9-beta") // if you want to include nms logic for title management 
}
```

#### General Initialization

> [!WARNING]
> Using the library without the standalone JGuiWrapper plugin requires you to initialize the listeners in your main plugin class.

```java
@Override
public void onEnable() {
    JGuiInitializer.init(this); // registers all listeners
}
```

After calling `init`, you can create and open GUIs for any player.

---
