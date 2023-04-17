package datamodels

// Menu data model
data class MenuItem (
    var itemID: Long,
    var imageUrl: String,
    var itemName: String,
    var itemPrice: Float,
    var itemShortDesc: String,
    var itemTag: String,
    var itemStars: Float,
    var quantity: Int = 0
)
