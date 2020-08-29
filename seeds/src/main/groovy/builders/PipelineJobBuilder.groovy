package builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static utils.DslComponents.defaultDescription
import static utils.DslComponents.pipelineScm

class PipelineJobBuilder {

    String jobName
    String cron
    String jobDisplayName
    String jenkinsFile
    String additionalDescription = ""
    boolean jobDisabled = false
    Map envVars
    List<String> blockOn

    int daysToKeepBuilds = 31
    int buildsNumToKeep = 31
    int buildArtifactNumToKeep = 15
    int daysToKeepArtifacts = 31


    Job build(DslFactory dslFactory) {
        dslFactory.pipelineJob(jobName) {
            description "<pre>${defaultDescription}${additionalDescription}</pre>"
            if (jobDisplayName) {
                displayName(jobDisplayName)
            }
            disabled jobDisabled
            environmentVariables envVars
            if (cron) {
                properties {
                    pipelineTriggers {
                        triggers {
                            cron {
                                spec cron
                            }
                        }
                    }
                }
            }
            if (blockOn) {
                blockOn(blockOn) {
                    blockLevel('GLOBAL')
                    scanQueueFor('ALL')
                }
            }
            logRotator {
                daysToKeep daysToKeepBuilds
                numToKeep buildsNumToKeep
                artifactNumToKeep buildArtifactNumToKeep
                artifactDaysToKeep daysToKeepArtifacts
            }
            definition pipelineScm(jenkinsFile)
        }
    }
}
