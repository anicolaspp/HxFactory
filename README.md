# HxFactory
Factory library to create and run Hystrix commands with simplicity

[![Build Status](https://travis-ci.org/anicolaspp/HxFactory.svg?branch=master)](https://travis-ci.org/anicolaspp/HxFactory) [![codecov](https://codecov.io/gh/anicolaspp/HxFactory/branch/master/graph/badge.svg)](https://codecov.io/gh/anicolaspp/HxFactory)

## From Hystrix Command to HxCommand

When using Hystrix, we need to define a command for every single interaction with our dependencies. 

```
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
```
