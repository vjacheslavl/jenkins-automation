def call(list, body) {
    def variants = [:]

    list.each { item -> variants[item] = { body(item) } }
    parallel variants
}
