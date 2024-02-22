# DreamAPI [![](https://jitpack.io/v/DreamDevelopments/DreamAPI.svg)](https://jitpack.io/#DreamDevelopments/DreamAPI)

API used for DreamDevelopments plugins.

Includes:
- Message API
- GUI API
- Config API

## How to use

You can import DreamAPI using Maven or Gradle

### Gradle
```gradle

repositories {
  mavenCentral()

  maven { url 'https://jitpack.io' }
}

dependencies {

  implementation 'com.github.DreamDevelopments:DreamAPI:0.1.0'

}
```

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
    <version>Tag</version>
  </dependency>

</dependencies>
