package com.example.emptyactivity

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import androidx.compose.foundation.layout.*
import com.google.maps.android.compose.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun MapScreen() {
    val ArequipaLocation = LatLng(-16.4040102, -71.559611) // Arequipa, Perú
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }
    val locations = listOf(
        LatLng(-16.433415, -71.5442652), // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994) // Zamacola
    )

    var mapType by remember { mutableStateOf(MapType.NORMAL) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                getUserLocation(fusedLocationClient) { location ->
                    userLocation = location
                    cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(location, 12f)
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUserLocation(fusedLocationClient) { location ->
                userLocation = location
                cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(location, 12f)
            }
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }




    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.TopCenter) {
            Row {
                Button(onClick = { mapType = MapType.NORMAL }) {
                    Text("Normal")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { mapType = MapType.HYBRID }) {
                    Text("Hybrid")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { mapType = MapType.SATELLITE }) {
                    Text("Satellite")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { mapType = MapType.TERRAIN }) {
                    Text("Terrain")
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = mapType)
            ) {
                userLocation?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Ubicación Actual"
                    )
                }
                Marker(
                    state = rememberMarkerState(position = ArequipaLocation),
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.mountain_map), // Icono de la imagen
                    title = "Arequipa, Perú"
                )

                locations.forEach { location ->
                    Marker(
                        state = rememberMarkerState(position = location),
                        title = "Ubicación",
                        snippet = "Punto de interés"
                    )
                }
                val mallAventuraPolygon = listOf(
                    LatLng(-16.432292, -71.509145),
                    LatLng(-16.432757, -71.509626),
                    LatLng(-16.433013, -71.509310),
                    LatLng(-16.432566, -71.508853)
                )

                val parqueLambramaniPolygon = listOf(
                    LatLng(-16.422704, -71.530830),
                    LatLng(-16.422920, -71.531340),
                    LatLng(-16.423264, -71.531110),
                    LatLng(-16.423050, -71.530600)
                )

                val plazaDeArmasPolygon = listOf(
                    LatLng(-16.398866, -71.536961),
                    LatLng(-16.398744, -71.536529),
                    LatLng(-16.399178, -71.536289),
                    LatLng(-16.399299, -71.536721)
                )

                //Ejemplos creados

                val TecsupPolygon = listOf(
                    LatLng(-16.42931280342968, -71.52054060331555),
                    LatLng(-16.429006516842023, -71.51960719306679),
                    LatLng(-16.428900494449465, -71.51920189651052),
                    LatLng(-16.42884159309527, -71.51876589567061),
                    LatLng(-16.431085722076205, -71.51827462711867),
                    LatLng(-16.431203523406126, -71.51830533140308),
                    LatLng(-16.43131543460416, -71.51837902168577),
                    LatLng(-16.431450905967253, -71.51907293851586),
                    LatLng(-16.43142145567893, -71.51919575565361),
                    LatLng(-16.431327214726423, -71.51926944593711),
                    LatLng(-16.430732317657046, -71.519386122218),
                    LatLng(-16.430261110764377, -71.51950893935579),
                    LatLng(-16.430420143218683, -71.52022127875651),
                    LatLng(-16.42931280342968, -71.52054060331555),
                )

                val CementerioLaApachetaPolygon = listOf(
                    LatLng(-16.43012038994172, -71.53531001915881),
                    LatLng(-16.433970737358337, -71.53611328014631),
                    LatLng(-16.435128080796815, -71.53652768264575),
                    LatLng(-16.43558400207499, -71.53410220919204),
                    LatLng(-16.430586916143227, -71.53303582557555),
                    LatLng(-16.43012038994172, -71.53531001915881)
                )

                Polygon(
                    points = plazaDeArmasPolygon,
                    strokeColor = Color.Red,
                    fillColor = Color.Blue,
                    strokeWidth = 5f
                )
                Polygon(
                    points = parqueLambramaniPolygon,
                    strokeColor = Color.Red,
                    fillColor = Color.Blue,
                    strokeWidth = 5f
                )
                Polygon(
                    points = mallAventuraPolygon,
                    strokeColor = Color.Red,
                    fillColor = Color.Blue,
                    strokeWidth = 5f
                )

                Polyline(
                    points = TecsupPolygon,
                    color = Color.Blue,
                    width = 10f
                )

                Polyline(
                    points = CementerioLaApachetaPolygon,
                    color = Color.Red,
                    width = 10f
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getUserLocation(fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (LatLng) -> Unit) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(LatLng(it.latitude, it.longitude))
        }
    }
}