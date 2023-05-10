package au.edu.swin.sdmd.swinmealapp.datamodels

import java.util.*

data class Customer(
    val id: UUID,
    var email: String?,
    var password: String,
    var name: String?,
    var gender: String?,
    var age: Int?,
    var height: Float?,
    var weight: Float?,
    var activityLevel: String?
)