package au.edu.swin.sdmd.swinmealapp.datamodels

// Cart data model
data class CartItem(
    var itemID: String,
    var imageUrl: String,
    var itemName: String,
    var itemPrice: Float,
    var itemShortDesc: String,
    var itemStars: Float,
    var quantity: Int,
//    var foodID: String
)
