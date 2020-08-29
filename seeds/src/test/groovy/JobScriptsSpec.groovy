import javaposse.jobdsl.dsl.*
import javaposse.jobdsl.plugin.JenkinsJobManagement
import spock.lang.Unroll
import groovy.io.FileType
import jenkins.model.Jenkins
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification

class JobScriptsSpec extends Specification {

    @Shared
    @ClassRule
    private JenkinsRule jenkinsRule = new JenkinsRule()

    @Shared
    private File outputDir = new File('./build/debug-xml')

    def setupSpec() {
        outputDir.deleteDir()
        jenkinsRule.timeout = 300
    }

@Unroll
    void 'test functional test job script #file.name'(File file) {
        given:
        JobManagement jm = new JenkinsJobManagement(System.out, [:], new File('.'))

        when:
        GeneratedItems items = new DslScriptLoader(jm).runScript(file.text)
        writeItems items

        then:
        noExceptionThrown()

        where:
        file << getJobFiles("scripts/jobs/functional")
    }

    /**
     * Write the config.xml for each generated job and view to the build dir.
     */
    void writeItems(GeneratedItems items) {
        Jenkins jenkins = jenkinsRule.jenkins

        items.jobs.each { GeneratedJob generatedJob ->
            String jobName = generatedJob.jobName
            hudson.model.Item item = jenkins.getItemByFullName(jobName)
            String text = new URL(jenkins.rootUrl + item.url + 'config.xml').text
            writeFile new File(outputDir, 'jobs'), jobName, text
        }

        items.views.each { GeneratedView generatedView ->
            String viewName = generatedView.name
            hudson.model.View view = jenkins.getView(viewName)
            String text = new URL(jenkins.rootUrl + view.url + 'config.xml').text
            writeFile new File(outputDir, 'views'), viewName, text
        }
    }

    /**
     * Write a single XML file, creating any nested dirs.
     */
    void writeFile(File dir, String name, String xml) {
        List tokens = name.split('/')
        File folderDir = tokens[0..<-1].inject(dir) { File tokenDir, String token ->
            new File(tokenDir, token)
        }
        folderDir.mkdirs()

        File xmlFile = new File(folderDir, "${tokens[-1]}.xml")
        xmlFile.text = xml
    }

    static List<File> getJobFiles(String path) {
        List<File> files = []
        new File(path).eachFileRecurse(FileType.FILES) {
            if (it.name.endsWith('.groovy')) {
                files << it
            }
        }
        files
    }
}