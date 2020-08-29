def call(String jobName, boolean waitToFinish, Map stringParams, boolean propagate = true) {
    build job: jobName, quietPeriod: 2, wait: waitToFinish, propagate: propagate, parameters: stringParams.collect { name, value -> [$class: 'StringParameterValue', name: name, value: value]}
}

def call(String jobName, boolean waitToFinish, boolean propagate = true) {
    build job: jobName, quietPeriod: 2, wait: waitToFinish, propagate: propagate
}
