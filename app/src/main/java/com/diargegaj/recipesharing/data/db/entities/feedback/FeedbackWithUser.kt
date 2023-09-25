package com.diargegaj.recipesharing.data.db.entities.feedback

import androidx.room.Embedded
import androidx.room.Relation
import com.diargegaj.recipesharing.data.db.entities.UserEntity

data class FeedbackWithUser(
    @Embedded val feedback: FeedbackEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: UserEntity?
)