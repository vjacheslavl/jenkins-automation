import com.cloudbees.groovy.cps.NonCPS

def call() {
    String triggerPhrase = env.gitlabTriggerPhrase //comment from GitLab

    Map<String, Closure> allJobs = ["job1": { println("Trigger job1") },
                                    "job2": { println("Trigger job2") },
                                    "job3": { println("Trigger job2") }]

    node("") {
        gitlabCommitStatus(name: "Orchestration Job") {

            if (triggerPhrase == "@jenkins trigger all jobs") {

                parallel(allJobs)

            } else if (triggerPhrase.contains("@jenkins trigger job ")) {

                String jobName = extractJobName(triggerPhrase)
                parallel(allJobs.findAll({ it.key == jobName }))

            } else {
                error "command not found"
            }

        }
    }
}

@NonCPS
String extractJobName(String comment) {
    def matcher = comment =~ "@jenkins trigger job (.*)"
    return ((matcher[0]) as ArrayList).get(1)
}