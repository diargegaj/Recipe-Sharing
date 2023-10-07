sealed class DBCollection(val collectionName: String) {

    object User : DBCollection("users")
    object Recipe : DBCollection("recipes")
    object Feedbacks : DBCollection("feedbacks")

    object Following : DBCollection("following")
    object Followers : DBCollection("followers")

    object UserFollowing : DBCollection("userFollowing")
    object UserFollowers : DBCollection("userFollowers")
}