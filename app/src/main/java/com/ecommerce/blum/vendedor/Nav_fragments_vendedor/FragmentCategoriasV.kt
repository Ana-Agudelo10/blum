package com.ecommerce.blum.vendedor.Nav_fragments_vendedor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.FragmentCategoriasVBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FragmentCategoriasV : Fragment() {

    private lateinit var binding : FragmentCategoriasVBinding
    private lateinit var mContext : Context
    private lateinit var progressDialog : ProgressDialog

    private var imageUri : Uri? = null


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.imgCategorias.setOnClickListener {
            seleccionarImg()
        }

        binding.btnAgregarCat.setOnClickListener {
            validarInfo()
        }
        return binding.root
    }

    private fun seleccionarImg() {
        ImagePicker.with(requireActivity())
            .crop()	    			// Cortar la imagen
            .compress(1024)			// comprimir la imagen a menos de 1 MB
            .maxResultSize(1080, 1080)	// resolucion maximo 1080x1080
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }

    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado->
             if (resultado.resultCode == Activity.RESULT_OK){
                 // Resultado OK
                 val data = resultado.data
                 imageUri = data!!.data
                 // establecer la imagen seleccionada en el ImageView
                 binding.imgCategorias.setImageURI(imageUri)
             } else if (resultado.resultCode == ImagePicker.RESULT_ERROR){
                 Toast.makeText(mContext, "Accion cacelada", Toast.LENGTH_SHORT).show()
             } else {
                 Toast.makeText(mContext, "Tarea cancelada", Toast.LENGTH_SHORT).show()
             }
        }

    private var categoria = ""

    private fun validarInfo() {
        categoria = binding.etCategoria.text.toString().trim()

        if (categoria.isEmpty()) {
            Toast.makeText(context, "Ingrese una categoria", Toast.LENGTH_SHORT).show()
        }else if (imageUri == null){
            Toast.makeText(context, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
        }        else {
            binding.etCategoria.error = null
            agregarCatBD()
        }
    }

    private fun agregarCatBD() {
        progressDialog.setMessage("Agregando categoria...")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        val keyId = ref.push().key // Identificador para relacionar las categorias en ld bd

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "${keyId}"
        hashMap["categoria"] = "${categoria}"


        ref.child("${keyId}")
            .setValue(hashMap)
            .addOnSuccessListener {
                //progressDialog.dismiss()
                //Toast.makeText(context, "Categoria agregada correctamente", Toast.LENGTH_SHORT).show()
                //binding.etCategoria.setText("")
                subirImgStorage(keyId!!)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "Fallo al agregar categoria debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subirImgStorage(keyId: String) {

        progressDialog.setMessage("Subiendo imagen...")
        progressDialog.show()

        val nombreImagen = keyId
        val nombreCarpeta = "Categorias/${nombreImagen}"
        val storageReference = FirebaseStorage.getInstance().getReference(nombreCarpeta)
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot ->
                progressDialog.dismiss()
                val uriTask =  taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val urlImgCargada = uriTask.result

                if(uriTask.isSuccessful){
                    val hashMap = HashMap<String, Any>()
                    hashMap["imagenUrl"] = "${urlImgCargada}"

                    val ref = FirebaseDatabase.getInstance().getReference("Categorias")
                    ref.child(nombreImagen).updateChildren(hashMap)
                    Toast.makeText(context, "Categoria agregada correctamente", Toast.LENGTH_SHORT).show()
                    binding.etCategoria.setText("")
                    imageUri = null
                    binding.imgCategorias.setImageURI(imageUri)
                    binding.imgCategorias.setImageResource(R.drawable.categorias)

                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "Fallo al subir imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}
















