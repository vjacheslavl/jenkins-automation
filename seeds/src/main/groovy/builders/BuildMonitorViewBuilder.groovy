package builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.views.BuildMonitorView

import static builders.OrderBy.DISPLAY_NAME

class BuildMonitorViewBuilder {

    String viewName
    OrderBy orderBy = DISPLAY_NAME

    private def orderConfig = { OrderBy orderBy ->
        return {
            it / 'config' {
                order(class: orderBy.value)
            }
        }
    }

    BuildMonitorView build(DslFactory dslFactory) {
        dslFactory.buildMonitorView(viewName) {
            configure orderConfig(orderBy)
        }
    }

    BuildMonitorView buildWithNamesList(DslFactory dslFactory, List jobNames) {
        def nameArray = jobNames.toArray(new String[jobNames.size()])
        dslFactory.buildMonitorView(viewName) {
            jobs {
                names(nameArray)
            }
            configure orderConfig(orderBy)
        }
    }

    BuildMonitorView buildWithRegex(DslFactory dslFactory, String jobsRegex) {
        dslFactory.buildMonitorView(viewName) {
            jobs {
                regex(jobsRegex)
            }
            configure orderConfig(orderBy)
        }
    }
}

enum OrderBy {
    DISPLAY_NAME("${PATH}.ByDisplayName"),
    NAME("${PATH}.ByName"),
    FULL_NAME("${PATH}.ByFullName"),
    LAST_BUILD_TIME("${PATH}.ByLastBuildTime"),
    STATUS("${PATH}.ByStatus"),
    ESTIMATED_DURATION("${PATH}.ByEstimatedDuration")

    OrderBy(String value) {
        this.value = value
    }

    private static final PATH = "com.smartcodeltd.jenkinsci.plugins.buildmonitor.order"
    public final String value
}