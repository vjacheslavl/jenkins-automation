package utils

import groovy.json.JsonSlurper

class CommonUtils {

    static def parseJson(String text) {
        new JsonSlurper().parseText(text)
    }
}
