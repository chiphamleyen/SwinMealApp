package au.edu.swin.sdmd.swinmealapp.datamodels

// Menu data model
data class MenuItem (
    var itemID: String,
    var imageUrl: String,
    var itemName: String,
    var itemPrice: Float,
    var itemShortDesc: String,
    var itemTag: String,
    var itemStars: Float,
    var quantity: Int = 0
)
