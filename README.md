# Jenkins Job DSL and pipeline script gradle project

Conjoined project for jenkins pipeline management using [job-dsl-plugin](https://github.com/jenkinsci/job-dsl-plugin) for job creation and [jenkins-pipeline-plugin](https://jenkins.io/doc/book/pipeline/)
for job logic definition using scripted pipelines.   
Project supports code completion of job-dsl scripts and pipeline scripts in IntelliJ IDEA. 
Just import project as gradle project using gradle wrapper.   

## Running all tests in this repository
`./gradlew clean check` 

## Pipelines DSL 
Pipeline module contains all of scripted jenkins files

    .   
    ├── scripts                 # Jenkins pipeline script files   
    └── build.gradle            # Build file   

## Jenkins seeds (job-dsl)

Inspired by https://github.com/sheehan/job-dsl-gradle-example

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

### Testing of seed jobs
`./gradlew :seeds:test` - runs seed jobs tests.
[JobScriptsSpec](seeds/src/test/groovy/JobScriptsSpec.groovy) 
will loop through all DSL files and make sure they don't throw any exceptions when processed. All XML output files are written to `build/debug-xml`. 
This can be useful if you want to inspect the generated XML before check-in.

Tests are using jenkins test harness
- https://www.jenkins.io/doc/developer/testing/

## Shared libraries

https://www.jenkins.io/doc/book/pipeline/shared-libraries/

    .      
    ├── src                     # Groovy source files
    ├── test                    # Unit tests   
    └── vars                    # Reusable library functions (global vars)
        ├── foo.bar             # Global 'foo' library   
        └── foo.txt             # Docs for global 'foo' library

### Testing of shared libraries

`./gradlew :check` - runs shared library unit tests

Inspired by 
https://github.com/macg33zr/pipelineUnit

Uses Jenkins pipelineUnit - https://github.com/jenkinsci/JenkinsPipelineUnit

## Setti up local jenkins in docker
Installing Jenkins locally for development purposes

! please note that script security is disabled locally using permissive-script-security plugin
Still warnings from script security are displayed in console logs of your pipelines
In production use the scripts still has to be approved by administrators of Jenkins

    .   
    ├── master                      
    │   ├── Dockerfile              # Dockerfile for master creation      
    │   └── plugins.txt             # list of all plugins, that local Jenkins will install   
    ├── jenkins.yaml                # (CaSC) configuration file for jenkins. Contains all settings for new server.  
    │                               # https://github.com/jenkinsci/configuration-as-code-plugin
    │
    ├── jenkins-docker-compose.yml  # docker compose for creating jenkins master and  agent       
    ├── start-local-jenkins.sh      # script for startting local jenkins
    └── stop-local-jenkins.sh       # script for removal of local jenkins
    
    - 

### Starting jenkins in docker
```bash
sh ./docker/start-local-jenkins.sh
```
creates 2 containers
- jenkins master (with plugins preinstalled)
- simple agent

Local jenkins url
http://localhost:8092

### Creating jobs using master-seed job

Once you have local jenkins created you can proceed with creating jobs.

Use http://localhost:8092/job/master-seed  job to generate all jobs from seeds

#### Useful links on started Jenkins instance
- http://localhost:8092/plugin/job-dsl/api-viewer/index.html
- http://localhost:8092/job/master-seed/pipeline-syntax/gdsl