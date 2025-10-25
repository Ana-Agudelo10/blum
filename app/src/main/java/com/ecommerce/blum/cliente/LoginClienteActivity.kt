package com.ecommerce.blum.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth

class LoginClienteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginClienteBinding
    private  lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this )
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginC.setOnClickListener {
            validarInfo()
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))

        }

    }

    private var email = ""
    private var password = ""
    private fun validarInfo() {

        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "Email Invalido"
            binding.etEmail.requestFocus()

        }else if(email.isEmpty()){
            binding.etEmail.error = "Ingrese Email"
            binding.etEmail.requestFocus()

        } else if(password.isEmpty()){
            binding.etPassword.error = "Ingrese Password"
            binding.etPassword.requestFocus()

        }else{
            loginCliente()
        }
    }
    private fun loginCliente(){
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
                Toast.makeText(this, "Bienvenido(a)",
                    Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(this, "No se pudo iniciar sesion debido a ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }
}