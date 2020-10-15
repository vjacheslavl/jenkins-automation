import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class gitlabSimpleOrchestratorTest extends BasePipelineTest {
    def gitlabSimplerOrchestrator

    @Before
    void setUp() {
        super.setUp()
        registerMocks()
        gitlabSimplerOrchestrator = loadScript("vars/gitlabSimpleOrchestrator.groovy")
    }

    @Test
    void '@jenkins trigger all jobs'() {
        //Gitlab triggered comment is update-screenshots
        binding.setVariable('env', ['gitlabTriggerPhrase': "@jenkins trigger all jobs"])

        gitlabSimplerOrchestrator.call()
        
        Map jobsTriggered = helper.callStack.findAll {
            call -> call.methodName == "parallel"
        }.get(0).getArgs()[0]

        assertEquals(3, jobsTriggered.size())
        assertTrue(jobsTriggered.containsKey("job1"))
        assertTrue(jobsTriggered.containsKey("job2"))
        assertTrue(jobsTriggered.containsKey("job3"))

        assertJobStatusSuccess()
    }

    @Test
    void '@jenkins trigger job job1'() {
        //Gitlab triggered comment is update-screenshots
        binding.setVariable('env', ['gitlabTriggerPhrase': "@jenkins trigger job job1"])

        gitlabSimplerOrchestrator.call()

        Map jobsTriggered = helper.callStack.findAll {
            call -> call.methodName == "parallel"
        }.get(0).getArgs()[0]

        assertEquals(1, jobsTriggered.size())
        assertTrue(jobsTriggered.containsKey("job1"))

        assertJobStatusSuccess()
    }

    @Test
    void '@jenkins unknown command'() {
        //Gitlab triggered comment is update-screenshots
        binding.setVariable('env', ['gitlabTriggerPhrase': "@jenkins unknown command"])

        gitlabSimplerOrchestrator.call()

        assertEquals(0, helper.methodCallCount('parallel'))

        assertJobStatusFailure()
    }

    def registerMocks() {
        helper.registerAllowedMethod("gitlabCommitStatus",
                [LinkedHashMap, Closure],
                { params, body -> body.call() })
    }
}