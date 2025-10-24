package com.ecommerce.blum.cliente

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.ecommerce.blum.R
import com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaC
import com.ecommerce.blum.cliente.Nav_Fragments_Cliente.FragmenMiPerfilC
import com.ecommerce.blum.cliente.Nav_Fragments_Cliente.FragmentInicioC
import com.ecommerce.blum.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView

class MainActivityCliente : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainClienteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainClienteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.NavigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragmente(FragmentInicioC())
    }

    private fun replaceFragmente(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.op_inicio_c -> {
                replaceFragmente(FragmentInicioC())
            }
            R.id.op_mi_perfil_c -> {
                replaceFragmente(FragmenMiPerfilC())
            }
            R.id.op_cerrar_sesion_c -> {
                Toast.makeText(applicationContext, "Has cerrado sesión.", Toast.LENGTH_SHORT).show()
            }
            R.id.op_tienda_c -> {
                replaceFragmente(FragmentTiendaC())
            }
            R.id.op_mis_ordenes_c -> {
                replaceFragmente(FragmentMisOrdenesC())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}