package jobs.functional

import builders.GitlabTriggeredPipelineBuilder

new GitlabTriggeredPipelineBuilder(
        jobName: "Sample gitlab job",
        jobDisplayName: " Simple gitlab triggered job",
        jenkinsFile: "pipelines/scripts/pipelineJob/mock_job.pipeline",
        additionalDescription: "Some description",
        rebuildCommentTrigger: "@jenkins run this job",
        buildsNumToKeep: 100,
        buildArtifactNumToKeep: 100
).build(this);