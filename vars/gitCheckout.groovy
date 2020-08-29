def call(Map args) {
    String repository = args.repository ?: error('Repository name must be provided!')
    String branch = args.branch ?: 'master'
    git branch: branch, url: repository
}
