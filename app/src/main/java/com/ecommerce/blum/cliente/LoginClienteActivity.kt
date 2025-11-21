package com.ecommerce.blum.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ecommerce.blum.Constantes
import com.ecommerce.blum.R
import com.ecommerce.blum.databinding.ActivityLoginClienteBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginClienteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginClienteBinding
    private  lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSingInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this )
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSingInClient = GoogleSignIn.getClient(this, gso)


        binding.btnLoginC.setOnClickListener {
            validarInfo()
        }

        /*Iniciar sesion con google*/
        binding.btnLoginGoogle.setOnClickListener {
            googleLogin()
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

    private fun googleLogin() {
        val googleSignInIntent = mGoogleSingInClient.signInIntent
        googleSignInARL.launch(googleSignInIntent )
    }

    private val googleSignInARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ resultado ->

        if(resultado.resultCode == RESULT_OK){
            //Si el usuario selecciono una cuenta del cuadro de dialogo
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                autenticacionGoogle(cuenta.idToken)

            }catch (e: Exception){
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "La operacion del logeo ha sido cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun autenticacionGoogle(idToken: String?) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { resultadoAuth->
                if (resultadoAuth.additionalUserInfo!!.isNewUser){
                    //Si el usuario es nuevo, resgistrar su informacion

                    llenarInfoBD()

                }else{
                    //Si el usuario ya esta registrado con anterioridad
                    startActivity(Intent(this, MainActivityCliente::class.java))
                    finishAffinity()
                }
            }

            .addOnFailureListener { e->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando Informacion")
        val uid = firebaseAuth.uid
        val nombreC = firebaseAuth.currentUser?.displayName
        val emailC = firebaseAuth.currentUser?.email
        val tiempoRegistro = Constantes().obtenerTiempoD()

        val datosCliente = HashMap<String, Any>()

        datosCliente["uid"] = "$uid"
        datosCliente["nombres"] = "$nombreC"
        datosCliente["email"] = "$emailC"
        datosCliente["tRegistro"] = "$tiempoRegistro"
        datosCliente["imagen"] = ""
        datosCliente["tipoUsuario"] = "cliente"

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()

            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}