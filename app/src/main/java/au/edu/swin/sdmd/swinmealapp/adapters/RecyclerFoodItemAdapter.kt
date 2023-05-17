package au.edu.swin.sdmd.swinmealapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import au.edu.swin.sdmd.swinmealapp.R
import au.edu.swin.sdmd.swinmealapp.datamodels.MenuItem
import java.util.*
import com.squareup.picasso.Picasso;

class RecyclerFoodItemAdapter(
    private var menuList: MutableList<MenuItem>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<RecyclerFoodItemAdapter.ItemListViewHolder>(), Filterable {

    private var fullItemList = ArrayList<MenuItem>(menuList)

    // Interface for defining click listener callbacks
    interface OnItemClickListener {
        fun onFoodClick(item: MenuItem)
        fun onPlusBtnClick(item: MenuItem)
        fun onMinusBtnClick(item: MenuItem)
    }

    // Create view holder for recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_menu_item, parent, false) as View
        return ItemListViewHolder(view)
    }

    // Bind data to view holder
    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val menu = menuList[position]
        holder.itemName.text = menu.itemName
        holder.itemPrice.text = "$${menu.itemPrice}"
        holder.itemStars.text = menu.itemStars.toString()
        holder.itemShortDesc.text = menu.itemShortDesc
        holder.itemCalories.text = menu.calories.toString() + " kcal"
        holder.itemQuantity.text = menu.quantity.toString()
        Picasso.get().load(menu.imageUrl).into(holder.itemImage)

        holder.itemView.setOnClickListener{
            listener.onFoodClick(menu)
        }

        holder.itemPlus.setOnClickListener {
            val number = menu.quantity
            holder.itemQuantity.text = (number + 1).toString()
            listener.onPlusBtnClick(menu)
        }

        holder.itemMinus.setOnClickListener {
            val number = menu.quantity
            if (number > 0) {
                holder.itemQuantity.text = (number - 1).toString()
                listener.onMinusBtnClick(menu)
            }
        }
    }

    override fun getItemCount(): Int = menuList.size

    // View holder class for holding view references
    class ItemListViewHolder(view: View) : ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val itemStars: TextView = view.findViewById(R.id.item_stars)
        val itemShortDesc: TextView = view.findViewById(R.id.item_shortDesc)
        val itemCalories: TextView = view.findViewById(R.id.item_calories)
        val itemQuantity: TextView = view.findViewById(R.id.item_quantity)
        val itemPlus: ImageView = view.findViewById(R.id.item_plus)
        val itemMinus: ImageView = view.findViewById(R.id.item_minus)
    }

    fun filterList(filteredList: MutableList<MenuItem>) {
        menuList = filteredList
        notifyDataSetChanged()
    }

    // Filter function
    override fun getFilter(): Filter {
        return searchFilter;
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<MenuItem>()
            if (constraint!!.isEmpty()) {
                filteredList.addAll(fullItemList)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                for (item in fullItemList) {
                    if (item.itemName.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            menuList.clear()
            menuList.addAll(results!!.values as ArrayList<MenuItem>)
            notifyDataSetChanged()
        }

    }
}