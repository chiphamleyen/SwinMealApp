package au.edu.swin.sdmd.myapp.datamodels.order

// Cart data model
data class CartItem(
    var itemID: Long,
    var imageUrl: String,
    var itemName: String,
    var itemPrice: Float,
    var itemShortDesc: String,
    var itemStars: Float,
    var quantity: Int,
    var foodID: Long
)
