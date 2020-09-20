def call(List goals, List configurations, Map systemProperties, String pomFile = 'pom.xml') {
    String goalsString = goals.join(" ")
    String projectPropsString = configurations ? "-P$configurations" : ""
    String systemPropsString = systemProperties ? systemProperties.collect { k, v -> "-D$k=$v" }.join(" ") : ""

    def jdk = tool name: 'openjdk-11', type: 'jdk'
    def maven = tool name: 'apache-maven-3.2.5-takari', type: 'maven'

    withEnv(["JAVA_HOME=${jdk}", "PATH+MAVEN=${maven}/bin:${env.JAVA_HOME}/bin"]) {
        sh "mvn -V -U -s ./$pomFile $goalsString $projectPropsString $systemPropsString"
    }
}

def call(List goals, Map systemProperties) {
    call(goals, [], systemProperties)
}

def call(List goals) {
    call(goals, [:])
}