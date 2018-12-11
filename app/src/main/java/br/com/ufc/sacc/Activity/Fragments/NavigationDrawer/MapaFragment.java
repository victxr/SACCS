package br.com.ufc.sacc.Activity.Fragments.NavigationDrawer;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng posicaoUfc = getCoordenadaEndereco("Av. José de Freitas Queiroz, 5003 - Cedro, Quixadá - CE, 63900-000");
        if (posicaoUfc != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoUfc, 17);
            googleMap.moveCamera(update);
            MarkerOptions pino = new MarkerOptions();
            pino.position(posicaoUfc);
            pino.title("UFC - Universidade Federal do Ceará");
            pino.snippet("Campus Quixadá");
            googleMap.addMarker(pino);
        }
    }

    private LatLng getCoordenadaEndereco(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultadosEndereco =
                    geocoder.getFromLocationName(endereco, 1);

            if (!resultadosEndereco.isEmpty()) {
                LatLng posicao = new LatLng(resultadosEndereco.get(0).getLatitude(), resultadosEndereco.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
