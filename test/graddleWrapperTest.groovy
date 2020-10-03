import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class graddleWrapperTest extends BasePipelineTest {
    def gradleWrapper

    @Before
    void setUp() {
        super.setUp()
        gradleWrapper = loadScript("vars/gradleWrapper.groovy")
    }

    @Test
    void 'Gradle wrapper should call sh'() {
        gradleWrapper.call([tasks: ['clean', ':seeds:jar']])
        assertEquals(1, helper.methodCallCount('sh'))
        String shArguments = helper.callStack.findAll {
            call -> call.methodName == "sh"
        }.get(0).getArgs()[0]
        assertEquals("./gradlew --no-daemon clean :seeds:jar", shArguments.trim())
    }
}