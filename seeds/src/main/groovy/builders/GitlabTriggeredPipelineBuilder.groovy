package builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static utils.DslComponents.defaultDescription
import static utils.DslComponents.pipelineScm

class GitlabTriggeredPipelineBuilder {

    String jobName
    String cron
    String jenkinsFile
    String additionalDescription = ''
    boolean jobDisabled = false
    Map envVars

    boolean mergeRequestEvents = false
    boolean acceptedMergeRequestEvents = false
    boolean closedMergeRequestEvents = false
    boolean approvedMergeRequestEvents = false
    boolean pushEvents = false
    String rebuildMergeRequest = 'both'
    String rebuildCommentTrigger = '@jenkins retry'
    String triggeredBranchRegex
    String jobDisplayName

    String gitlabRepo = 'gitlab.somerepo.com'

    int daysToKeepBuilds = 20
    int buildsNumToKeep = 50
    int buildArtifactNumToKeep = 50
    int daysToKeepArtifacts = 20
    int maxConcurrentBuilds = 0

    Job build(DslFactory dslFactory) {
        dslFactory.pipelineJob(jobName) {
            description "${defaultDescription}${additionalDescription}"
            disabled jobDisabled
            environmentVariables envVars
            if (jobDisplayName) {
                displayName jobDisplayName
            }
            if (maxConcurrentBuilds != 0) {
                throttleConcurrentBuilds {
                    maxTotal(maxConcurrentBuilds)
                }
            }
            properties {
                gitLabConnection {
                    gitLabConnection gitlabRepo
                }
            }
            logRotator {
                daysToKeep daysToKeepBuilds
                numToKeep buildsNumToKeep
                artifactNumToKeep buildArtifactNumToKeep
                artifactDaysToKeep daysToKeepArtifacts
            }
            triggers {
                if (cron) {
                    cron(cron)
                }
                gitlabPush {
                    buildOnMergeRequestEvents mergeRequestEvents
                    buildOnPushEvents pushEvents
                    rebuildOpenMergeRequest rebuildMergeRequest
                    commentTrigger rebuildCommentTrigger
                    if (triggeredBranchRegex) {
                        targetBranchRegex triggeredBranchRegex
                    }
                }
            }
            definition pipelineScm(jenkinsFile)

            configure {
                it / 'triggers' / 'com.dabsquared.gitlabjenkins.GitLabPushTrigger' << {
                    triggerOnAcceptedMergeRequest(acceptedMergeRequestEvents)
                    triggerOnClosedMergeRequest(closedMergeRequestEvents)
                    triggerOnApprovedMergeRequest(approvedMergeRequestEvents)
                }
            }
        }
    }
}
