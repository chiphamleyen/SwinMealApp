package au.edu.swin.sdmd.swinmealapp.datamodels

// Menu data model
data class MenuItem (
    var itemID: Int,
    var imageUrl: String,
    var itemName: String,
    var itemPrice: Float,
    var itemShortDesc: String,
    var itemTag: String,
    var itemStars: Float,
    var calories: Float,
    var protein: Float,
    var carbohydrate: Float,
    var fat: Float,
    var quantity: Int = 0
)
