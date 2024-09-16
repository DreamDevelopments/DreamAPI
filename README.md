# DreamAPI 
[![Discord](https://img.shields.io/discord/746768784139747400?logo=discord&logoColor=white&label=support&color=blue)](https://discord.gg/dream-developments-746768784139747400)
[![GitHub Issues](https://img.shields.io/github/issues/DreamDevelopments/DreamAPI?color=orange)](https://github.com/DreamDevelopments/DreamAPI/issues)
[![JitPack](https://jitpack.io/v/DreamDevelopments/DreamAPI.svg)](https://jitpack.io/#DreamDevelopments/DreamAPI)

API used for DreamDevelopments plugins.

Includes:
- Message API
- GUI API
- Config API

## How to use

You can import DreamAPI using Maven or Gradle

### Maven
```xml
<repositories>

  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>

</repositories>

<dependencies>

  <dependency>
    <groupId>com.github.DreamDevelopments</groupId>
    <artifactId>DreamAPI</artifactId>
    <version>0.2.3</version>
  </dependency>

</dependencies>
```

### Gradle
```gradle

repositories {

  maven { url 'https://jitpack.io' }

}

dependencies {

  implementation 'com.github.DreamDevelopments:DreamAPI:0.2.3'

}
```
