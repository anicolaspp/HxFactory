# HxFactory
Factory library to create and run Hystrix commands with simplicity

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.anicolaspp/HxFactory/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.anicolaspp/HxFactory) [![Build Status](https://travis-ci.org/anicolaspp/HxFactory.svg?branch=master)](https://travis-ci.org/anicolaspp/HxFactory)

> [Hystrix](https://github.com/Netflix/Hystrix)([part of Netflix OSS](https://netflix.github.io/)) is a latency and fault tolerance library designed to isolate points of access to remote systems, services and 3rd party libraries, stop cascading failure and enable resilience in complex distributed systems where failure is inevitable.

***HxFactory*** allows us to use Hystrix in the simples way possible.

```xml
<dependency>
    <groupId>com.github.anicolaspp</groupId>
    <artifactId>HxFactory</artifactId>
    <version>1.0.3</version>
</dependency>
```

## From Hystrix Command to ***HxFactory***

When using Hystrix, we need to define a command for every single interaction with our dependencies. 

```java
class GetUsersCommand extends HystrixCommand<List<User>> {
    
    private static Query getAllUsers = ...
    private Database db;
    
    protected GetUsersCommand(Database db, HystrixCommandGroupKey group) {
        super(group);
        
        this.db = db;
    }
    
    @Override
    protected List<User> run() throws Exception {
        return db.run(getAllUsers);
    }
}

GetUsersCommand command = new GetUsersCommand(...);

command.execute();
```
Using ***HxFactory*** we can reduce this to the following statement

```java
val command = Command.create(
    "db access", 
    () -> db.run(...)
);

command.execute();
```

If you need to access the same database to get one specific user, this is how to do it with ***Hystrix***

```java
class GetUserCommand extends HystrixCommand<User> {
    
    
    private Database db;
    private final String id;
    
    protected GetUsersCommand(Database db, String id, HystrixCommandGroupKey group) {
        super(group);
        
        this.db = db;
        this.id = id;
    }
    
    @Override
    protected User run() throws Exception {
        return db.run(getUserById(id));
    }
    
    private Query getUserById(String id) {
        ....
    }
}

GetUserCommand getUserCommand = new getUserCommand(...);

getUserCommand.execute();

```
Using ***HxFactory*** you can simply do

```java
val getUserCommand = Command.create("getUser", () -> db.run(getUserById(id)));

getUserCommand.execute();
```

As we can see, no new classes are needed, we can define the action as a lambda and pass it in. 

Let's see some examples from the ***Hystrix*** docs and compare them to ***HxFactory***.

Hello World **Hystrix**
```java
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        // a real example would do work like a network call here
        return "Hello " + name + "!";
    }
}

CommandHelloWorld helloWordCommand = new CommandHelloWorld("anicolaspp");

assert helloWordCommand.execute().equals("Hello anicolaspp");
```

Hello World **HxFactory**
```java
String name = "anicolaspp"; 

val helloWordCommand = Command.create("hello", () -> "Hello " + name);

assert helloWordCommand.execute().equals("Hello anicolaspp");
```

Commands with fallbacks **Hystrix**
```java
public class CommandHelloFailure extends HystrixCommand<String> {

    private final String name;

    public CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }
}

CommandHelloFailure command = new CommandHelloFailure("me");

assert command.execute().equals("Hello Failure me");
```

Commands with fallbacks **HxFactory**
```java
val command = Command.WithFallback.create(
    "hello", 
    () -> { throw new RuntimeException("this command always fails"); },
    () -> "Hello Failure " + name + "!"
);

assert command.execute().equals("Hello Failure " + name + "!");
```

Another good example is to use a fallback by calling the second command. 

Fallback: Cache via Network **HxFactory**
```java
val fallbackViaNetwork = Command.WithFallback.create(
    "viaNetwork",
    () -> MemCacheClient.getValue(id),   
    () -> null
);

val commandWithFallbackViaNetwork = Command.WithFallback.create(
    "primaryCommand",
    () -> { throw new RuntimeException("force failure for example"); },   
    () -> fallbackViaNetwork.execute()
);

commandWithFallbackViaNetwork.execute();
```

***HxFactory*** only exposes a way to create, with simplicity, `HystrixCommand<T>`, everything being returned from ***HxFactory*** is a ***HystrixCommand***.
   
## Configuring Commands

***HxFactory*** offers some other ways to create commands, for example, by specifying a command timeout. 

```java
HystrixCommand<String> command = Command.WithFallback.create(
                    "testTimeout",
                    () -> {
                        Thread.sleep(10000);
                        
                        return "";
                    },
                    () -> "fallback",
                    1000);
            
assert command.execute().equals("fallback");
```
Notice that the main function will timeout since it blocks for a time (10s) longer than the specified timeout (1s); then the fallback will be executed. 

***HxFactory*** doesn't add anything on top of ***Hystrix*** so everything you expect to work with ***Hystrix*** will work when using ***HxFactory***. This includes circuit breakers, request caching, etc...

The following test shows how the circuit breaker works.

```java
    @Test
    public void testFallbackCommandOpensClose() throws InterruptedException {
        val cmd1 = Command.WithFallback.create(
                "testFallbackCommandOpensClose",
                () -> "hello",
                () -> "bye bye");
        
        cmd1.execute();
        
        assert !cmd1.isCircuitBreakerOpen();
        
        for (int i = 0; i < 1000; i++) {
            val failedCmd = Command.WithFallback.create(
                    "testFallbackCommandOpensClose",
                    () -> {
                        throw new RuntimeException("error");
                    },
                    () -> "fallback"
            );
            
            assert failedCmd.execute().equals("fallback");
        }
        
        Thread.sleep(1000);
        
        assert cmd1.isCircuitBreakerOpen();
        
        val ensureFallBackWhenOpenCommand = Command.WithFallback.create(
                "testFallbackCommandOpensClose",
                () -> "should not return this",
                () -> "from fall back");
        
        assert ensureFallBackWhenOpenCommand.execute().equals("from fall back");
    }
```

We can configure a `Command` to use caching in the following way.

```java
@Test
    public void testCache() {
        val command = Command.WithCacheContext
                .WithCacheKey
                .create("key",
                        "testCache",
                        () -> "hello"
                );
        
        assert command.execute().equals("hello");
        
        val secondCommand = Command.WithCacheContext
                .WithCacheKey
                .create("key",
                        "testCache",
                        () -> "me"
                );
        
        // notice we are executing second command but we are getting data from first one.
        val result = secondCommand.execute();
        
        assert result.equals("hello");
    }
```

Of course, you can have cache when using commands with fallback.

```java
 @Test
    public void testCacheFallback() {
        val command = Command.WithCacheContext.WithCacheKey.create(
                "testCacheFallback",
                "firstCommand",
                () -> {
                    throw new RuntimeException("Error");
                },
                () -> "fallback"
        );

        assert command.execute().equals("fallback");

        val secondCommand = Command.WithCacheContext.WithCacheKey.create(
                "testCacheFallback",
                "secondCommand",
                () -> "me"
        );

        assert secondCommand.execute().equals("fallback");
    }
```
Notice that when running the second command, we are getting the result of the first command's fallback. This is given by the fact we are using the same cache key, so the second command retrieves the result from cache. 

### Complex Command Configuration

If you need to configure a command is very custom way, ***HxFactory*** offers a way to create commands using a provided `HystrixCommand.Setter`. In this way, you can configure the command in any way you want. The following test shows this use case. 

```java
    @Test
    public void testCommandFromSetter() {
        val customSetter = CommandSetter
                .getSetterFor("customSetterCommand")
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties
                                .defaultSetter()
                                .withCircuitBreakerRequestVolumeThreshold(20)
                                .withCircuitBreakerEnabled(false)
                                .withFallbackEnabled(false)
                );
        
        val command = Command.fromSetter(customSetter, () -> "hello");
        
        assert command.getProperties().circuitBreakerRequestVolumeThreshold().get() == 20;
        assert command.getProperties().circuitBreakerEnabled().get() == false;
        assert command.getProperties().fallbackEnabled().get() == false;
        
        assert command.execute().equals("hello");
    }
```

## Some gotchas

It is important to note that the action to be passed in is executed lazily when you run the command. Mistakingly, you can do the following. 

```java
val getDataAsync = Command.WithFallback.create(
    "async", 
    () -> CompletableFuture.supplyAsync(() -> db.run(...)),
    () -> CompletableFuture.completedFuture(someStaticData)
);

CompletionStage<User> userFuture = getDataAsync.execute();
```

If for any reason, `db.run` fails to execute, the fallback statement will never be executed. ***THIS IS NOT A BUG*** since we are instructing to create a `CompletableFuture` with some operation, `db.run`, to be executed at some point. From the point of view of the command, the operation `.supplyAsync` never fails, what fails is the execution or result of the following operation, but that is outside from the command context itself. 

In order to avoid this, you could do the following

```java
val getDataAsync = Command.WithFallback.create(
    "async", 
    () ->  db.run(...),
    () -> someStaticData
);

CompletionStage<User> userFuture = CompletableFuture.supplyAsync(() -> getDataAsync.execute());
```
In this case, if accessing the db fails, the command fallback will be executed and the result will be passed back to the concurrent task. 
