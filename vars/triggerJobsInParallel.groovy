
List call(List jobNames, Closure transformBadge = { String text -> text }) {
    Closure jobTrigger = { String jobName ->
        triggerJob(jobName, true, false)
    }
    triggerJobs(jobNames, jobTrigger, transformBadge)
}

List call(Map<String, Map> jobNames, Closure transformBadge = { String text -> text }) {
    Closure jobTrigger = { String jobName ->
        triggerJob(jobName, true, jobNames[jobName], false)
    }
    triggerJobs(jobNames.keySet().toList(), jobTrigger, transformBadge)
}

List call(List jobNames, Map parameters, Closure transformBadge = { String text -> text }) {
    Closure jobTrigger = { String jobName ->
        triggerJob(jobName, true, parameters, false)
    }
    triggerJobs(jobNames, jobTrigger, transformBadge)
}

private List triggerJobs(List jobNames, Closure jobTrigger, Closure transformBadge) {
    List builds = []
    parallelize(jobNames) { String jobName ->
        def buildd = jobTrigger(jobName)
        builds.add(buildd)
        echo("Build ${buildd.getFullDisplayName()} finished with status ${buildd.result}")
    }
    def failedJobs = builds.findAll { v -> v.result == 'FAILURE' }
    if (failedJobs.size() > 0) {
        currentBuild.result = "FAILURE"
    }
    return builds
}