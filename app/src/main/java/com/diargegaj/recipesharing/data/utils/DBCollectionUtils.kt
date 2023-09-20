package com.diargegaj.recipesharing.data.utils

object DBCollectionUtils {

    object COLLECTIONS {
        object User {
            const val COLLECTION_NAME = "users"

            object FIELDS {
                const val UID = "userUUID"
                const val NAME = "name"
                const val LASTNAME = "lastName"
                const val EMAIL = "email"
                const val PROFILE_URL = "profilePhotoUrl"
            }
        }

        object Recipe {
            const val COLLECTION_NAME = "recipes"

            object FIELDS {

            }
        }
    }
}
