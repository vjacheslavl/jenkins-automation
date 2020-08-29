package jobs.functional

import builders.BuildMonitorViewBuilder
import builders.PipelineJobBuilder

import static groovy.json.JsonOutput.toJson
import static utils.CommonUtils.parseJson


def pipelineScript = parseJson(readFileFromWorkspace("resources/jobs/json/test_pipelines.json"))

pipelineScript.testRunners.each { String jenkinsFile, Map pipelines ->
    pipelines.each { String pipeline, Map params ->
        Map environmentVariables = [jobNamePattern: params.jobNamePattern,
                                    dashboardName : params.name,
                                    type          : params.type
        ]

        new PipelineJobBuilder(
                jobName: pipeline,
                additionalDescription: params.description,
                jenkinsFile: "pipelines/scripts/pipelineJob/$jenkinsFile",
                jobDisabled: params.disabled,
                envVars: environmentVariables,
                cron: params.cron,
                daysToKeepBuilds: 31,
                buildsNumToKeep: 50
        ).build(this).with {
            parameters {
                stringParam("jobPattern", "", "Job names pattern to trigger (empty = default)")
            }
        }

        new BuildMonitorViewBuilder(viewName: params."name")
                .buildWithRegex(this, params.jobNamePattern)
    }
}

pipelineScript.testJobs.each { String jenkinsFile, Map jobs ->
    jobs.each { String jobName, Map params ->

        new PipelineJobBuilder(
                jobName: jobName,
                jenkinsFile: "pipelines/scripts/pipelineJob/$jenkinsFile",
                daysToKeepBuilds: 31,
                buildsNumToKeep: 15,
                jobDisabled: params.disabled,
                envVars: [systemProperties: toJson(params.systemProperties)]
        ).build(this)
    }
}