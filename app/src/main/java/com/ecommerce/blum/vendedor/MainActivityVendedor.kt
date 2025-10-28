package com.ecommerce.blum.vendedor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ecommerce.blum.R
import com.ecommerce.blum.SeleccionarTipoActivity
import com.ecommerce.blum.databinding.ActivityMainVendedorBinding
import com.ecommerce.blum.vendedor.Bottom_nav_fragments_vendedor.FragmentMisProductosV
import com.ecommerce.blum.vendedor.Bottom_nav_fragments_vendedor.FragmentOrdenesV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentCategoriasV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentInicioV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentMiTiendaV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentResenaV
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivityVendedor : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainVendedorBinding
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainVendedorBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()



        binding.navegationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        binding.navegationView.setCheckedItem(R.id.op_inicio_v)

    }

    private fun cerrarSesion() {
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "Has cerrado sesion", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion() {
        /*Si el usuario no ha iniciado sesión*/
        if(firebaseAuth!!.currentUser== null){
            //Ir a la pantalla de login
            startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
        } else {
            Toast.makeText(applicationContext, "Vendedor en linea", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.op_inicio_v -> {
                replaceFragment(FragmentInicioV())
            }

            R.id.op_mi_tienda -> {
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.op_reseñas_v -> {
                replaceFragment(FragmentResenaV())
            }
            R.id.op_categorias_v -> {
                replaceFragment(FragmentCategoriasV())
            }
            R.id.op_cerrar_sesion_v -> {
                cerrarSesion()
            }
            R.id.op_mis_productos_v -> {
                replaceFragment(FragmentMisProductosV())
            }
            R.id.op_mis_ordenes_v -> {
                replaceFragment(FragmentOrdenesV())
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}