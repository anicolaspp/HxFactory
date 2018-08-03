# HxFactory
Factory library to create and run Hystrix commands with simplicity

[![Build Status](https://travis-ci.org/anicolaspp/HxFactory.svg?branch=master)](https://travis-ci.org/anicolaspp/HxFactory) [![codecov](https://codecov.io/gh/anicolaspp/HxFactory/branch/master/graph/badge.svg)](https://codecov.io/gh/anicolaspp/HxFactory)

## From Hystrix Command to HxCommand

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
Using HxFactory we can reduce this to the following statement

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
but when using HxFactory you can simply do

```java
val getUserCommand = Command.create(
    "getUser",
    () -> db.run(getUserById(id))
);

getUserCommand.execute();
```
