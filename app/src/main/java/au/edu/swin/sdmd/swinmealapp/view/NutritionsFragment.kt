package au.edu.swin.sdmd.swinmealapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import au.edu.swin.sdmd.swinmealapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class NutritionsFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nutritions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val food_name = this.arguments?.getString("food_name").toString()
        val food_calories = this.arguments?.getString("food_calories").toString()
        val food_protein = this.arguments?.getString("food_protein").toString()
        val food_carbohydrate = this.arguments?.getString("food_carbohydrate").toString()
        val food_fat = this.arguments?.getString("food_fat").toString()

        Log.i("Food: ", food_name+food_calories )

        view.findViewById<TextView>(R.id.food_name).text = food_name
        view.findViewById<TextView>(R.id.nutrition_calories).text = "- Calories: ${food_calories} kcal"
        view.findViewById<TextView>(R.id.nutrition_protein).text = "- Protein: ${food_protein} g"
        view.findViewById<TextView>(R.id.nutrition_carbohydrate).text = "- Carbohydrate: ${food_carbohydrate} g"
        view.findViewById<TextView>(R.id.nutrition_fat).text = "- Fat: ${food_fat} g"

    }

}