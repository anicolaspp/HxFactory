# HxFactory
Factory library to create and run Hystrix commands with simplicity

[![Build Status](https://travis-ci.org/anicolaspp/HxFactory.svg?branch=master)](https://travis-ci.org/anicolaspp/HxFactory) [![codecov](https://codecov.io/gh/anicolaspp/HxFactory/branch/master/graph/badge.svg)](https://codecov.io/gh/anicolaspp/HxFactory)

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

If you need to access to the same database to get one specific user, you have to do when using pure Hystrix

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
but when using ***HxFactory*** you can simply do

```java
val getUserCommand = Command.create(
    "getUser",
    () -> db.run(getUserById(id))
);

getUserCommand.execute();
```

As we can see, there is not need to created new class for any command we need to run. 

Let's see some classing examples from the Hystrix docs and how we can write them using ***HxFactory***.

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
val command = Command.create(
    "hello", 
    () -> { throw new RuntimeException("this command always fails"); },
    () -> "Hello Failure " + name + "!");

assert command.execute().equals("Hello Failure " + name + "!");
```

Another good example is to fallback by calling a second command. 

Fallback: Cache via Network **HxFactory**
```java
val fallbackViaNetwork = Command.create(
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

### Some gotchas

It is important to note that the action to be passed in is execute lazyly when you run the command. Mistakingly, you can do the following. 

```java
val getDataAsync = Command.WithFallback.create(
    "async", 
    () -> CompletableFuture.supplyAsync(() -> db.run(...)),
    () -> CompletableFuture.completedFuture(someStaticData)
);

CompletionStage<User> userFuture = getDataAsync.execute();
```

If for any reason, `db.run` fails to execute, the fallback statement will never executed. THIS IS NOT A BUG since we instructing to create a `CompletableFuture` with some operation `db.run` to be execute at some point. From the point of view of the command, the operation `.supplyAsync` never fails, what fails is the execution or result of the following operation, but that is outside from the command context itself. 

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
