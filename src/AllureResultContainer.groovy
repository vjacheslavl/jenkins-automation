class AllureResultContainer {

    private final List<AllureTestCase> tests
    private final List<AllureTestCase> failedTests
    private final List<AllureTestCase> skippedTests
    private final List<AllureTestCase> disabledTests
    private final String jobUrl
    private final String buildNumber

    AllureResultContainer(
            String jobUrl,
            String buildNumber,
            List<AllureTestCase> tests,
            List<AllureTestCase> failedTests,
            List<AllureTestCase> skippedTests,
            List<AllureTestCase> disabledTests) {
        this.jobUrl = jobUrl
        this.buildNumber = buildNumber
        this.tests = tests
        this.failedTests = failedTests
        this.skippedTests = skippedTests
        this.disabledTests = disabledTests
    }

    String getSummary() {
        String summary = "TOTAL scenarios: ${tests.size()}."
        if (failedTests) {
            summary += " FAILED: ${failedTests.size()}."
        }
        if (skippedTests) {
            summary += " SKIPPED: ${skippedTests.size()}."
        }
        if (disabledTests) {
            summary += " DISABLED: ${disabledTests.size()}."
        }
        return summary
    }

    String getFailedTestsSummary() {
        String summary = ""
        if (failedTests) {
            summary += """```${printFailedTestsList(failedTests)}```"""
        }
        if (skippedTests && skippedTests.size() != tests.size()) {
            summary += """\nSkipped: ```${printFailedTestsList(skippedTests)}```"""
        }
        if (disabledTests) {
            summary += """\nDisabled: ```${printFailedTestsList(disabledTests)}```"""
        }
        return summary
    }

    String printFailedTestsList(List<AllureTestCase> testCaseList) {
        return testCaseList.collect {
            it.name + (it.flaky ? " (flaky)" : "")
        }.join('\n')
    }

    String generateTestCaseLink(AllureTestCase testCase) {
        return "${this.jobUrl}${this.buildNumber}/allure/#suites/${testCase.parentUid}/${testCase.uid}"
    }

    List<AllureTestCase> getExecutedTests() {
        tests
    }

    List<AllureTestCase> getFailedTests() {
        failedTests
    }

    List<AllureTestCase> getSkippedTests() {
        skippedTests
    }

    List<AllureTestCase> getDisabledTests() {
        disabledTests
    }

    Double getTotalDuration() {
        Double start = tests.min { it.start }.start
        Double stop = tests.max { it.stop }.stop

        stop - start
    }
}
