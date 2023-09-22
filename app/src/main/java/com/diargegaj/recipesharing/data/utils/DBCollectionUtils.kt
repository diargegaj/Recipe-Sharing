
sealed class DBCollection(val collectionName: String) {

    object User : DBCollection("users")
    object Recipe : DBCollection("recipes")

}