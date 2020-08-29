def call(Map args) {
    String testStageName = args.testStageName ?: 'Run tests'
    Closure testRunner = args.testRunner ?: error('Provide test runner')
    boolean throwException = args.throwExecption ?: false

    stage(testStageName) {

        env.JAVA_HOME="${tool 'openjdk-11'}"
        env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

        try {
            testRunner()
        } catch (Exception err) {
            echo "Test step failed"
            if (throwException) {
                throw err
            }
        }
    }
}
