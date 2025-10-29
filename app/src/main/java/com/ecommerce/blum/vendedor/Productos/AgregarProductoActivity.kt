package com.ecommerce.blum.vendedor.Productos

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ecommerce.blum.Adaptadores.AdaptadorImagenSeleccionada
import com.ecommerce.blum.Constantes
import com.ecommerce.blum.Modelos.ModeloCategoria
import com.ecommerce.blum.Modelos.ModeloImagenSeleccionada
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ActivityAgregarProductoBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AgregarProductoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAgregarProductoBinding
    private var imagenUri : Uri? = null

    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSet: AdaptadorImagenSeleccionada

    private lateinit var caegoriasArrayList: ArrayList<ModeloCategoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarCategorias()

        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImagen()
        }


        binding.Categria.setOnClickListener {
            selecCategorias()
        }

        cargarImagenes()

    }

    private fun cargarCategorias() {

        caegoriasArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                caegoriasArrayList.clear()
                for(ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    caegoriasArrayList.add(modelo!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private var idCat = ""
    private var tituloCat = ""

    private fun  selecCategorias(){

        val categoriasArray = arrayOfNulls<String>(caegoriasArrayList.size)
        for(i in caegoriasArrayList.indices) {
            categoriasArray[i] = caegoriasArrayList[i].categoria
        }

        val bulder = AlertDialog.Builder(this)
        bulder.setTitle("Seleccione una categoría")
            .setItems(categoriasArray) { dialog, which ->
                idCat = caegoriasArrayList[which].id
                tituloCat = caegoriasArrayList[which].categoria
                binding.Categria.text = tituloCat
            }
            .show()
    }

    private fun cargarImagenes() {
        adaptadorImagenSet = AdaptadorImagenSeleccionada(this, imagenSelecArrayList)
        binding.RVImagenesProducto.adapter = adaptadorImagenSet
    }

    private fun seleccionarImagen() {
       ImagePicker.with(this)
           .crop() // Recortar la imagen
           .compress(1024) // Comprimir la imagen (inferior a 1Mb)
           .maxResultSize(1080, 1080) // Tamaño máximo de la imagen
           .createIntent { intent ->
                resultadoImg.launch(intent)
           }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado->
            if(resultado.resultCode == Activity.RESULT_OK){
                val data = resultado.data
                imagenUri = data!!.data
                val tiempo = "${Constantes().obtenerTiempoD()}"
                val modeloImgSel = ModeloImagenSeleccionada(tiempo, imagenUri, null, false)
                imagenSelecArrayList.add(modeloImgSel)
                cargarImagenes()
            } else{
                Toast.makeText(this, "Acción cancelada", Toast.LENGTH_SHORT).show()
            }
        }
}