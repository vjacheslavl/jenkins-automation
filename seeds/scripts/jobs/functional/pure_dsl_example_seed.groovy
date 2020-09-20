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

(this as DslFactory).pipelineJob("example-job_with_pipeline") {
    description "this job created using DSL"
    definition {
        cpsScm {
            scm {
                git {
                    branch "*/master"
                    remote {
                        url "/qa/jenkins-automation"
                    }
                }
            }
            scriptPath "pipelines/scripts/pipelineJob/mock_job.pipeline"
        }
    }
}