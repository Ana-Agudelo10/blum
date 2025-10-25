package com.ecommerce.blum


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ecommerce.blum.cliente.LoginClienteActivity
import com.ecommerce.blum.databinding.ActivitySeleccionarTipoBinding
import com.ecommerce.blum.vendedor.LoginVendedorActivity


class SeleccionarTipoActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySeleccionarTipoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tipoVendedor.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginVendedorActivity::class.java))
        }

        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginClienteActivity::class.java))
        }
    }
}