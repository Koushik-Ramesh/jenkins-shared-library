# jenkins-shared-library
Shared library in jenkins helps to elimnate the common patterns of code and can be used to define the common patters as functions and can be called from your pipelines.

### The structure of jenkins shared library

```
        (root)
        +- src                     # Groovy source files
        |   +- org
        |       +- foo
        |           +- Bar.groovy  # for org.foo.Bar class
        +- vars
        |   +- foo.groovy          # for global 'foo' variable
        |   +- foo.txt             # help for 'foo' variable
        +- resources               # resource files (external libraries only)
        |   +- org
        |       +- foo
        |           +- bar.json    # static helper data for org.foo.Bar

```

The `src`` directory should look like standard Java source directory structure. This directory is added to the classpath when executing Pipelines.

The vars directory hosts script files that are exposed as a variable in Pipelines. The name of the file is the name of the variable in the Pipeline. So if you had a file called vars/log.groovy with a function like def info(message)…​ in it, you can access this function like log.info "hello world" in the Pipeline. You can put as many functions as you like inside this file. Read on below for more examples and options.