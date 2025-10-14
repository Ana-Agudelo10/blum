package com.ecommerce.blum.vendedor

import android.content.Context
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
import com.ecommerce.blum.databinding.ActivityMainVendedorBinding
import com.ecommerce.blum.vendedor.Bottom_nav_fragments_vendedor.FragmentMisProductosV
import com.ecommerce.blum.vendedor.Bottom_nav_fragments_vendedor.FragmentOrdenesV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentInicioV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentMiTiendaV
import com.ecommerce.blum.vendedor.Nav_fragments_vendedor.FragmentResenaV
import com.google.android.material.navigation.NavigationView



class MainActivityVendedor : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var baiding: ActivityMainVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baiding = ActivityMainVendedorBinding.inflate(layoutInflater)

        setContentView(baiding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        baiding.navegationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            baiding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        baiding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        baiding.navegationView.setCheckedItem(R.id.op_inicio_v)

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
            R.id.op_cerrar_sesion_v -> {
                Toast.makeText(applicationContext, "Saliste de la aplicacion", Toast.LENGTH_SHORT).show()
            }
            R.id.op_mis_productos_v -> {
                replaceFragment(FragmentMisProductosV())
            }
            R.id.op_mis_ordenes_v -> {
                replaceFragment(FragmentOrdenesV())
            }
        }

        baiding.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}