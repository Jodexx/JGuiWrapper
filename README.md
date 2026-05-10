# JGuiWrapper

**A professional library for building customizable inventory GUIs on PaperMC and Minestom servers.**

[![Release](https://github.com/Jodexx/JGuiWrapper/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/Jodexx/JGuiWrapper/actions/workflows/gradle-publish.yml)
[![JGuiWrapper API version](https://jitpack.io/v/Jodexx/JGuiWrapper.svg)](https://jitpack.io/#Jodexx/JGuiWrapper)
[![License](https://img.shields.io/github/license/Jodexx/JGuiWrapper)](LICENSE)

---

## Overview

JGuiWrapper provides a unified API for creating and managing inventory-based GUIs across multiple Minecraft server platforms. It supports both standalone plugin deployment and direct embedding into your project.

> **Note:** The [Wiki](https://jodexx.github.io/JodexIndustriesWiki/JGuiWrapper/jguiwrapper-start) is currently outdated. Refer to this document for accurate setup instructions.

---

## Platform Support

| Platform                                                                                                                                       | Version             | Java     |
|------------------------------------------------------------------------------------------------------------------------------------------------|---------------------|----------|
| <img src="https://assets.papermc.io/brand/papermc_logo.min.svg" height="16" width="16" alt="PaperMC icon"> [PaperMC](https://papermc.io)       | 1.16.5 – 1.21.11    | Java 16+ |
| <img src="https://minestom.net/minestom-logo.png" height="16" width="16" alt="Minestom icon"> [Minestom](https://github.com/minestom/Minestom) | 2026.04.13-1.21.11+ | Java 25+ |

---

## Repository Setup

Add the JitPack repository to your build configuration.

**Maven**
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

**Gradle (Kotlin DSL)**
```kotlin
repositories {
    maven("https://jitpack.io")
}
```

---

## Installation

### Paper

JGuiWrapper offers two integration modes for Paper-based servers.

#### Mode 1 — Standalone Plugin (`api` module)

JGuiWrapper is installed as a separate plugin on the server. Your plugin references the API without shading.

**Maven**
```xml
<dependency>
    <groupId>com.github.Jodexx.JGuiWrapper</groupId>
    <artifactId>api</artifactId>
    <version>v1.0.0.9</version>
    <scope>provided</scope>
</dependency>
```

**Gradle (Kotlin DSL)**
```kotlin
dependencies {
    compileOnly("com.github.Jodexx.JGuiWrapper:api:v1.0.0.9")
}
```

#### Mode 2 — Embedded (`common` module)

The library is shaded directly into your plugin jar. No separate JGuiWrapper plugin is required on the server.

**Maven**
```xml
<dependency>
    <groupId>com.github.Jodexx.JGuiWrapper</groupId>
    <artifactId>common</artifactId>
    <version>v1.0.0.9</version>
</dependency>

<!-- Optional: NMS support for advanced title management -->
<dependency>
    <groupId>com.github.Jodexx.JGuiWrapper</groupId>
    <artifactId>nms</artifactId>
    <version>v1.0.0.9</version>
</dependency>
```

**Gradle (Kotlin DSL)**
```kotlin
dependencies {
    implementation("com.github.Jodexx.JGuiWrapper:common:v1.0.0.9")
    implementation("com.github.Jodexx.JGuiWrapper:nms:v1.0.0.9") // optional: NMS title support
}
```

#### Initialization

> [!WARNING]
> When using the embedded (`common`) module, you must manually register the library's listeners in your plugin's main class.

```java
@Override
public void onEnable() {
    PaperGuiApiImpl.init(this); // registers all required listeners
}
```

---

### Minestom

**Gradle (Kotlin DSL)**
```kotlin
dependencies {
    implementation("com.github.Jodexx.JGuiWrapper:minestom:v1.0.0.9")
}
```

#### Initialization

```java
void main() {
    MinestomGuiApi.init(MinecraftServer.process());
}
```

---

## Module Reference

| Module     | Purpose                                      | Deployment                |
|------------|----------------------------------------------|---------------------------|
| `api`      | Compile-time API for standalone plugin usage | Plugin on server          |
| `common`   | Full library for shaded embedding            | Shaded into your jar      |
| `nms`      | NMS-based title management (optional)        | Shaded alongside `common` |
| `minestom` | Full library for Minestom servers            | Shaded into your jar      |

---