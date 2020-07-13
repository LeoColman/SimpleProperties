
# Simple Properties


[![Build Status](https://travis-ci.com/LeoColman/SimpleProperties.svg?branch=master)](https://travis-ci.com/LeoColman/SimpleProperties) [![GitHub](https://img.shields.io/github/license/LeoColman/SimpleProperties.svg)](https://github.com/Kerooker/SimpleProperties/blob/master/LICENSE) [![Maven Central](https://img.shields.io/maven-central/v/com.kerooker.simpleproperties/simple-properties.svg)](https://search.maven.org/search?q=g:com.kerooker.simpleproperties)

## Introduction

Aplications might behave differently in different environments. One might have a specific configuration for Production Environment, which is different from Development Environment. 

With simple properties, you can configure a different profile of properties for each of your environments, and access them through an easy to use DSL.

This library was inspired and is very similar to [**Spring Profiles**](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html), but only for Properties, and in a simpler way.

## Adding to your project
Edit your `build.gradle` to contain the following dependency, from Maven Central:

```
implementation("com.kerooker.simpleproperties:simple-properties:{currentVersion}")
```

## Usage Example

```kotlin

object MySystemProperties {

    private val simpleProperties = SimpleProperties()
    
    val databaseUrl: String = simpleProperties["my.database.url"]

}

```

| File | Value of "my.database.url" |
| --- | --- |
| application-local.properties | localhost:3306 |
| application-qa.properties | qa.database.server.intranet:3306 |
| application-prod.properties | prod.database.server.intranet:7725 | 


Launching app:

With local profile:

`java -jar myapp.jar -Dactive_profiles=local`

```kotlin
MySystemProperties.databaseUrl == "localhost:3306"
```

With production profile:
`java -jar myapp.jar -Dactive_profiles=prod`
```kotlin
MySystemProperties.databaseUrl == "prod.database.server.intranet:7725"
```

## Default profile

SimpleProperties will always try to read an optional default file `application.properties`. Any profile may override properties from the default file.

## Configuring profiles

SimpleProperties will try to find your profiles in some places:

1. System Environment Variables
2. Java Properties
3. Defined at instantiation
    1. `SimpleProperties(profiles = listOf("qa"))`
    
## More than one profile

Multiple profiles may be used at once, in a comma separated value in case of System (`-Dactive_profiles=foo,bar,baz`).

If keys are overriden, the last profile in the list will take priority.

## Where to place files

Files are expected to be in the classpath. In `Spring`, this is usually done by placing the file in the `resources` folder, and this is allowed in Simple Properties too:

```
MyProject
`-- src
    `-- main
        |-- kotlin
        |   |-- Foo.kt
        |   `-- Bar.kt
        `-- resources
            |-- application.properties
            |-- application-foo.properties
            `-- application-bar.properties
```


You can customize where your files will be placed when creating the `SimpleProperties` instance:

```kotlin
SimpleProperties(filesLocation = "foo/bar")
```

Which allows for example the structure
```
MyProject
`-- resources
    `-- foo
        `-- bar
            |-- application.properties
            `-- application-foo.properties
```
