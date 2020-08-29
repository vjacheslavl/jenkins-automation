package utils

class DslComponents {

    static String defaultDescription = "This job is managed by master-seed. Manual configuration will be overwritten.\n"

    static def pipelineScm(String jenkinsFile) {

        return {
            cpsScm {
                scm {
                    git {
                        branch "*/master"
                        remote {
                            url "/qa/jenkins-automation"
                        }
                        extensions {
                            cloneOptions {
                                shallow()
                                noTags()
                            }
                        }
                    }
                }
                scriptPath jenkinsFile
            }
        }
    }
}