import groovy.json.JsonSlurper
import groovy.json.JsonSlurperClassic

class AllureResultParser implements Serializable {

    private Script script
    private String allureReportPath

    private AllureResultContainer resultContainer

    private AllureResultParser(Script script, String allureReportPath, AllureResultContainer allureResultContainer) {
        this.script = script
        this.allureReportPath = allureReportPath
        this.resultContainer = allureResultContainer
    }

    AllureResultContainer getResultContainer() {
        resultContainer
    }

    /**
     * Parse allure suites.json file
     * @param script jenkins script context
     * @param allureReportPath optional report path
     * @return
     */
    static AllureResultParser parseAllureSuites(Script script, String allureReportPath = 'allure-report') {
        println "Parsing suites.json"
        String allureSuites = script.readFile(file: "${allureReportPath}/data/suites.json".toString())
        def results = new JsonSlurper().parseText(allureSuites)
        List<AllureTestCase> tests = getChildren(results.children).collect { testCase ->
            new AllureTestCase(
                    name: testCase.name,
                    status: testCase.status,
                    flaky: testCase.flaky,
                    newFailed: testCase.newFailed,
                    parameters: testCase.parameters,
                    start: testCase.time.start,
                    stop: testCase.time.stop,
                    duration: testCase.time.duration,
                    uid: testCase.uid,
                    parentUid: testCase.parentUid
            )
        }
        List<AllureTestCase> failedTests = tests.findAll { testCase ->
            testCase.status == "failed" || testCase.status == "broken"
        }
        List<AllureTestCase> skippedTests = tests.findAll { testCase ->
            testCase.status == "skipped"
        }
        List<AllureTestCase> disabledTests = tests.findAll { testCase ->
            testCase.status == "unknown"
        }
        return new AllureResultParser(
                script,
                allureReportPath,
                new AllureResultContainer(script.env.JOB_URL, script.env.BUILD_NUMBER, tests, failedTests, skippedTests, disabledTests)
        )
    }

    /**
     * Recursively get flattened children elements from suites.json
     * @param suites
     * @return
     */
    private static List getChildren(List suites) {
        if (suites && suites.first().containsKey('children')) {
            return getChildren(suites.collect { it.children }.flatten())
        } else {
            return suites
        }
    }

    static String extractFailureText(Script script, String allureReportPath = 'allure-report') {
        String failureText = ""
        def failureCategories = readCategoriesFile(script, allureReportPath)
        def parsedJson = new JsonSlurperClassic().parseText(failureCategories)
        parsedJson.children.each {
            it.children.eachWithIndex { nodeItem, i ->
                failureText += "${++i}) ${(nodeItem.name.indexOf("\n") > 0 ? nodeItem.name.substring(0, nodeItem.name.indexOf("\n")) : nodeItem.name)}\n\n"
            }
        }
        return failureText;
    }

    static def readCategoriesFile(Script script, String allureReportPath = 'allure-report') {
        def json = script.readFile(file: "${allureReportPath}/data/categories.json".toString())
        println(json.getClass())
        return json
    }

}
