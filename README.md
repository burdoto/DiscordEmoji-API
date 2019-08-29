# DiscordEmoji-API [![Build Status](https://github.com/burdoto/DiscordEmoji-API/workflows/Build%20Tests/badge.svg)](https://github.com/burdoto/DiscordEmoji-API/actions) [![Javadocs](http://javadoc.io/badge/de.kaleidox/discordemoji-api.svg)](http://javadoc.io/doc/de.kaleidox/discordemoji-api) [![Maven Central Release](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordemoji-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.kaleidox/discordemoji-api) [![Development Release](https://jitpack.io/v/burdoto/DiscordEmoji-API.svg)](https://jitpack.io/#burdoto/DiscordEmoji-API)
#### Java-Wrapper for the [discordemoji.com](https://discordemoji.com/) API


## Importing

### Gradle
For importing this project using Gradle, you need to add the following to your `build.gradle`:
```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'de.kaleidox:discordemoji-api:1.0.0'
}
```

## Using

This wrapper contains [a facade class](https://burdoto.github.io/DiscordEmoji-API/de/kaleidox/discordemoji/DiscordEmoji.html) that can be conveniently used to request anything provided by the [discordemoji.com](https://discordemoji.com/) API.

Requested object are stored in a cache and can be re-requested by their IDs using the respective `getByID(int)` method.

## Speed Test Results
These are the time results for the wrapper when performing a full cache refresh on startup:
[![Speedtest Results](http://kaleidox.de/share/img/api/discordemoji/speedtest-results.png)]
