package com.ecommerce.blum.vendedor.Productos

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import com.google.firebase.storage.FirebaseStorage

class AgregarProductoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAgregarProductoBinding
    private var imagenUri : Uri? = null

    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSet: AdaptadorImagenSeleccionada

    private lateinit var caegoriasArrayList: ArrayList<ModeloCategoria>

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarCategorias()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.etPrecioConDescuentoP.visibility = View.GONE
        binding.etNotaDescuentoP.visibility = View.GONE

        binding.descuentoSwitch.setOnCheckedChangeListener{buttonView, isChecked ->
            if(isChecked){
                //Si el Switch esta habilitado
                binding.etPrecioConDescuentoP.visibility = View.VISIBLE
                binding.etNotaDescuentoP.visibility = View.VISIBLE

            }else{
                //SI el Switch esta deshabilitado
                binding.etPrecioConDescuentoP.visibility = View.GONE
                binding.etNotaDescuentoP.visibility = View.GONE
            }
        }

        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImagen()
        }


        binding.Categoria.setOnClickListener {
            selecCategorias()
        }
        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }

        cargarImagenes()

    }

    private var nombreP=""
    private var descripcionP=""
    private var categoriaP =""
    private var precioP=""
    private var descuentoHab= false
    private var precioDescP = ""
    private var notaDescP = ""
    private fun validarInfo() {
        nombreP = binding.etNombresP.text.toString().trim()
        descripcionP = binding.etDescripcionP.text.toString().trim()
        categoriaP = binding.Categoria.text.toString().trim()
        precioP = binding.etPrecioP.text.toString().trim()
        descuentoHab = binding.descuentoSwitch.isChecked

        if(nombreP.isEmpty()){
            binding.etNombresP.error = "Ingrese Nombre"
            binding.etNombresP.requestFocus()

        }
        else if(descripcionP.isEmpty()){
            binding.etDescripcionP.error = "Ingrese Descripcion"
            binding.etDescripcionP.requestFocus()

        }
        else if(categoriaP.isEmpty()){
            binding.Categoria.error = "Seleccione Una Catergoria"
            binding.Categoria.requestFocus()

        }
        else if(precioP.isEmpty()){
            binding.etPrecioP.error = "Seleccione un precio"
            binding.etPrecioP.requestFocus()

        }
        else if(imagenUri == null){
            Toast.makeText(this, "Seleccione al menos una imagen", Toast.LENGTH_SHORT).show()

        }else{
            //Descuento Habilitado = true
            if (descuentoHab){

                precioDescP = binding.etPrecioConDescuentoP.text.toString().trim()
                notaDescP = binding.etNotaDescuentoP.text.toString().trim()
                if (precioDescP.isEmpty()){
                    binding.etPrecioConDescuentoP.error = "Ingrese precio con descuento"
                    binding.etPrecioConDescuentoP.requestFocus()

                }else if(notaDescP.isEmpty()){
                    binding.etNotaDescuentoP.text.toString().trim()
                    binding.etNotaDescuentoP.requestFocus()
                }else{
                    agregarProducto()
                }

            }
            //Descuento deshabilitado = False
            else{
                precioDescP = "0"
                notaDescP = ""
                agregarProducto()

            }


        }

    }

    private fun agregarProducto() {
        progressDialog.setMessage("Agregando Producto")
        progressDialog.show()

        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        val keyId = ref.push().key

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "${keyId}"
        hashMap["nombre"] = "${nombreP}"
        hashMap["descripcion"] = "${descripcionP}"
        hashMap["categoria"] = "${categoriaP}"
        hashMap["precio"] = "${precioP}"
        hashMap["precioDesc"] = "${precioDescP}"
        hashMap["notaDesc"]="${notaDescP}"

        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {

                subirImgsStorage(keyId)

            }
            .addOnFailureListener {e->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }




    }
    private fun subirImgsStorage(keyId: String) {
        for (i in imagenSelecArrayList.indices){
            val modeloImagenSel = imagenSelecArrayList[i]
            val nombreImagen = modeloImagenSel.id
            val rutaImagen = "Productos/$nombreImagen"

            val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
            storageRef.putFile(modeloImagenSel.imageUri!!)

                .addOnSuccessListener {taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val uriImgCargada = uriTask.result

                    if (uriTask.isSuccessful){
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = "${modeloImagenSel.id}"
                        hashMap["imagenUrl"] = "${uriImgCargada}"

                        val ref = FirebaseDatabase.getInstance().getReference("Productos")
                        ref.child(keyId).child("Imagenes")
                            .child(nombreImagen)
                            .updateChildren(hashMap)
                        progressDialog.dismiss()
                        Toast.makeText(this, "Se agrego el producto", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }


                }

                .addOnFailureListener {e->
                    progressDialog.dismiss()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
    private fun limpiarCampos() {
        imagenSelecArrayList.clear()
        adaptadorImagenSet.notifyDataSetChanged()
        binding.etNombresP.setText("")
        binding.etDescripcionP.setText("")
        binding.etPrecioP.setText("")
        binding.Categoria.setText("")
        binding.descuentoSwitch.isChecked = false
        binding.etPrecioConDescuentoP.setText("")
        binding.etNotaDescuentoP.setText("")

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
                binding.Categoria.text = tituloCat
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





