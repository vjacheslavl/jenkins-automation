# Jenkins Job DSL and pipeline script gradle project

Inspired by https://github.com/sheehan/job-dsl-gradle-example

Conjoined project for jenkins pipeline management using [job-dsl-plugin](https://github.com/jenkinsci/job-dsl-plugin) for job creation and [jenkins-pipeline-plugin](https://jenkins.io/doc/book/pipeline/)
for job logic definition using scripted pipelines.   
Project supports code completion of job-dsl scripts and pipeline scripts in IntelliJ IDEA. Just import project as gradle project using gradle wrapper.   

## Running all tests
`./gradlew clean check` 

## File structure of pipelines
Pipeline module contains all of scripted jenkins files

    .   
    ├── scripts                 # Jenkins pipeline script files   
    └── build.gradle            # Build file   

## File structure of seeds
Seeds module contains seed jobs for creating jenkins pipeline jobs as well as some helper classes like builders and utils

    .   
    ├── seeds                   # DSL script files   
    ├── resources               # Resources for DSL scripts   
    ├── src   
    │   ├── main   
    │   │   └── groovy          # Support classes      
    │   └── test   
    │       └── groovy          # Specs   
    └── build.gradle            # Build file

## Testing seed jobs
`./gradlew :seeds:test` - runs seed jobs tests.
[JobScriptsSpec](seeds/src/test/groovy/JobScriptsSpec.groovy) 
will loop through all DSL files and make sure they don't throw any exceptions when processed. All XML output files are written to `build/debug-xml`. 
This can be useful if you want to inspect the generated XML before check-in.

## Shared libraries

https://www.jenkins.io/doc/book/pipeline/shared-libraries/

`./gradlew :check` - runs shared library unit tests

    .      
    ├── src                     # Groovy source files
    ├── test                    # Unit tests   
    └── vars                    # Reusable library functions (global vars)
        ├── foo.bar             # Global 'foo' library   
        └── foo.txt             # Docs for global 'foo' library

## Local jenkins setup
Installing Jenkins locally for development purpose

! please note that script security is disabled locally using permissive-script-security plugin
Still warnings from script security are displayed in console logs of your pipelines
In production use the scripts still has to be approved by administrators of Jenkins 

### Start
```bash
sh ./docker/start-local-jenkins.sh
```
creates 2 containers
- jenkins master (with plugins preinstalled)
- simple agent


### Starting with seeds setup

Once you have local jenkins created you can proceed with creating jobs.

Use http://localhost:8092/job/master-seed  job to generate all jobs from seeds

#### Useful links on started Jenkins instance
- http://localhost:8092/plugin/job-dsl/api-viewer/index.html
- http://localhost:8092/job/master-seed/pipeline-syntax/gdsl


### Important files
- jenkins-docker-compose.yml - docker compose for creating jenkins master and  agent
- jenkins.yaml - configuration for jenkins https://github.com/jenkinsci/configuration-as-code-plugin
- /master/plugins.txt - list of all plugins, that local jenkins will install 