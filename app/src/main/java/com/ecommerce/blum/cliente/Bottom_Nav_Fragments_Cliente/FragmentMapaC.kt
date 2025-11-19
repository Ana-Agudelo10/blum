
package com.ecommerce.blum.cliente.Bottom_Nav_Fragments_Cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ecommerce.blum.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng

class FragmentMapaC : Fragment(), OnMapReadyCallback {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragment (asegúrate que en fragment_mapa_c exista un contenedor con id map_fragment)
        return inflater.inflate(R.layout.fragment_mapa_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(requireContext(), "Aquí estamos", Toast.LENGTH_SHORT).show()

        // Inicializa el Maps SDK
        MapsInitializer.initialize(requireContext())

        // Busca el SupportMapFragment contenido en el layout o lo crea si no existe
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
            ?: SupportMapFragment.newInstance().also { sf ->
                childFragmentManager.beginTransaction()
                    .replace(R.id.map_fragment, sf)
                    .commit()
            }

        // Solicita el mapa asincrónicamente
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Configura el mapa: habilita controles y mueve la cámara a una ubicación inicial
        googleMap.uiSettings.isZoomControlsEnabled = true
        val initialLocation = LatLng(-34.0, 151.0) // Cambiar por la ubicación deseada
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))
    }
}