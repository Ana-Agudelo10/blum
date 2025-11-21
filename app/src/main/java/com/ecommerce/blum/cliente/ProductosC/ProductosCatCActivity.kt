package com.ecommerce.blum.cliente.ProductosC

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
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

        binding.etBuscarProducto.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(filtro: CharSequence?, start: Int, before: Int, count: Int) {
                try {

                    val consulta = filtro.toString()
                    adaptadorProductos.filter.filter(consulta)

                }catch (e: Exception ){

                }
            }
        })

        binding.IbLimpiarCampo.setOnClickListener {
            val consulta = binding.etBuscarProducto.text.toString().trim()
            if (consulta.isNotEmpty()){
                binding.etBuscarProducto.setText("")
                Toast.makeText(this, "Campo Limpiado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "No se ha ingresado una consulta", Toast.LENGTH_SHORT).show()
            }
        }

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