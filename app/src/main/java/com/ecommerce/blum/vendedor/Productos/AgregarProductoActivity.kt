package com.ecommerce.blum.vendedor.Productos

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ecommerce.blum.Adaptadores.AdaptadorImagenSeleccionada
import com.ecommerce.blum.Constantes
import com.ecommerce.blum.Modelos.ModeloImagenSeleccionada
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ActivityAgregarProductoBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class AgregarProductoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAgregarProductoBinding
    private var imagenUri : Uri? = null

    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSet: AdaptadorImagenSeleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImagen()
        }

        cargarImagenes()

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