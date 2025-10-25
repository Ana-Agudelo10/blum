package com.ecommerce.blum.vendedor

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecommerce.blum.databinding.ActivityLoginVendedorBinding
import com.google.firebase.auth.FirebaseAuth



class LoginVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginVendedorBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginV.setOnClickListener{
            validarInfo()
        }

        binding.tvRegistrarV.setOnClickListener{
            startActivity(Intent(applicationContext, RegistroVendedorActivity::class.java))
        }

    }

    private var email = ""
    private var password = ""

    private fun validarInfo() {
        email=binding.etEmail.text.toString().trim()
        password=binding.etPassword.text.toString().trim()
        if (email.isEmpty()){
            binding.etEmail.error = "Ingrese su email"
            binding.etEmail.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "Ingrese un email valido"
            binding.etEmail.requestFocus()
        }
        else if (password.isEmpty()){
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
        }
        else{
            loginVendedor()
        }

    }

    private fun loginVendedor() {

        progressDialog.setMessage("Iniciando sesión...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finishAffinity()
                Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo iniciar sesion debidoa a: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}