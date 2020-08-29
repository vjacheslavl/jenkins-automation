def call(Map args) {
    String tasks = args.tasks ? args.tasks.join(' ') : error("Please provide tasks to execute")
    String systemProperties = args.systemProperties ? args.systemProperties.collect { k, v -> "-D$k=$v".trim() }.join(' ') : ''
    String projectProperties = args.projectProperties ? args.projectProperties.collect { k, v -> "-P$k=$v".trim() }.join(' ') : ''
    String switches = args.switches ? (['--no-daemon'] + args.switches).join(' ') : '--no-daemon'
    if (args.javaVersion) {
        env.JAVA_HOME = "${tool name: args.javaVersion}"
        env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
    }
    String additionalCommands = args.additionalCommands ?: ""

    sh "./gradlew $switches $tasks $projectProperties $systemProperties $additionalCommands"
}
