package com.example.calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {
    public lateinit var list: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val db = FirebaseFirestore.getInstance()
    public lateinit var imgbtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        list = findViewById(R.id.list)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        list.adapter = adapter
        imgbtn = findViewById(R.id.imageButton)

        list.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position)
            showDeleteConfirmationDialog(item!!)
        }

        imgbtn.setOnClickListener {
            showDeleteConfirmationDialog2()
        }

        fetchDataFromFirestore()
    }

    private fun showDeleteConfirmationDialog(item: String) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this calculation?")
            .setPositiveButton("Yes") { dialog, which ->
                // Delete the item
                deleteItemFromFirestore(item)
            }
            .setNegativeButton("No") { dialog, which ->
                // Do nothing
            }
            .show()
    }

    private fun showDeleteConfirmationDialog2() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to clear this list ? ")
            .setPositiveButton("Yes") { dialog, which ->

                adapter.clear()
                deleteItemFromFirestore2()
            }
            .setNegativeButton("No") { dialog, which ->
                // Do nothing
            }
            .show()
    }


    private fun deleteItemFromFirestore(item: String) {
        db.collection("calculations")
            .whereEqualTo("calculation", item)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
                adapter.remove(item)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Calculation deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to delete calculation: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun deleteItemFromFirestore2() {
        db.collection("calculations")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
                adapter.clear()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "All calculations deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to delete calculations: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun fetchDataFromFirestore() {
        db.collection("calculations")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val calculation = document.getString("calculation")
                    if (calculation != null) {
                        adapter.add(calculation)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch data: $exception", Toast.LENGTH_SHORT).show()
            }
    }
}