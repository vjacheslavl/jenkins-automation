package jobs.functional

import builders.PipelineJobBuilder


new PipelineJobBuilder(
        jobName: "Example_PipelineJob",
        additionalDescription: "This job is created using Job DSL builder",
        jenkinsFile: "pipelines/scripts/pipelineJob/mock_job.pipeline",
        cronExpression: "H H(1-2) * * 1-5",
        daysToKeepBuilds: 31,
        buildsNumToKeep: 50
).build(this);


new PipelineJobBuilder(
        jobName: "Example_PipelineJob_with_Params",
        additionalDescription: "This job is created using Job DSL builder",
        jenkinsFile: "pipelines/scripts/pipelineJob/mock_job.pipeline",
).build(this).with {
    parameters {
        stringParam("stringParameter", "", "String parameter example")
        choiceParam('choiceOfTests', ['test1', 'test2', 'test3', 'test4'], "Choice parameter example")
    }
}
