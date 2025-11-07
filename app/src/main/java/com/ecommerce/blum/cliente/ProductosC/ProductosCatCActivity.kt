package com.ecommerce.blum.cliente.ProductosC

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ecommerce.blum.Adaptadores.AdaptadorProductoC
import com.ecommerce.blum.Modelos.ModeloProducto
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ActivityProductosCatCactivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductosCatCActivity : AppCompatActivity() {

    private  lateinit var  binding : ActivityProductosCatCactivityBinding

    private lateinit var productosArrayList: ArrayList<ModeloProducto>
    private lateinit var adaptadorProductos : AdaptadorProductoC

    private var nombreCat = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatCactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Obtenemos el nombre de la categoria
        nombreCat = intent.getStringExtra("nombreCat").toString()
        listarProductos(nombreCat)

    }

    private fun listarProductos(nombreCat: String) {

        productosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.orderByChild("categoria").equalTo(nombreCat)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   productosArrayList.clear()
                    for (ds in snapshot.children){
                        val modeloProducto = ds.getValue(ModeloProducto::class.java)
                        productosArrayList.add(modeloProducto!!)
                    }

                    adaptadorProductos = AdaptadorProductoC(this@ProductosCatCActivity, productosArrayList)
                    binding.productosRV.adapter = adaptadorProductos

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}