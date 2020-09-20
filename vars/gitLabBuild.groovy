def call(String name, Closure body) {
    gitlabCommitStatus(name: name) {
        try {
            body()
        } catch (err) {
            addGitlabMRComment comment: "${name} build failed"
            throw err
        }
    }
}