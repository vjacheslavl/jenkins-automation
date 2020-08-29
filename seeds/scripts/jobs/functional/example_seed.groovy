package jobs.functional

import javaposse.jobdsl.dsl.DslFactory

(this as DslFactory).pipelineJob("example-job") {
    description "this job created using DSL"
    properties {
        pipelineTriggers {
         triggers {
             cron {
                 spec "0 9 * * 4"
             }
         }
     }
    }
}