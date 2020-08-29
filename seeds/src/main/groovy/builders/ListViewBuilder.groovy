package builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.views.ListView

class ListViewBuilder {
    String viewName

    ListView build(DslFactory dslFactory) {
        listView(dslFactory)
    }

    ListView buildWithNameList(DslFactory dslFactory, List<String> jobNames) {
        def nameArray = jobNames.toArray(new String[jobNames.size()])
        listView(dslFactory).with {
            jobs {
                names(nameArray)
            }
        }
    }

    ListView buildWithRegex(DslFactory dslFactory, String jobsRegex) {
        listView(dslFactory).with {
            jobs {
                regex(jobsRegex)
            }
        }
    }

    private Closure<ListView> listView = { DslFactory dslFactory ->
        dslFactory.listView(viewName) {
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
            filterBuildQueue()
            filterExecutors()
        }
    }
}
