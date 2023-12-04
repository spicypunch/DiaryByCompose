package com.jm.diarybycompose.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jm.diarybycompose.ui.MainViewModel
import com.jm.diarybycompose.util.getMyLocation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(
    viewModel: MainViewModel,
    onClicked: (Int) -> Unit
) {
    viewModel.getAllItem()
    val allItems = viewModel.allItem.value
    val latLng = getMyLocation(LocalContext.current)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 17f)
    }
    val uiSettings = remember {
        MapUiSettings(myLocationButtonEnabled = true)
    }
    val properties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        for (i in allItems) {
            if (i.imageUri != null) {
                Marker(
                    state = MarkerState(position = LatLng(i.latitude, i.longitude)),
                    title = i.title,
                    icon = uriToBitmapDescriptor(LocalContext.current, Uri.parse(i.imageUri)),
                    onClick = {
                        i.id?.let { id -> onClicked(id) }
                        true
                    }
                )
            } else {
                Marker(
                    state = MarkerState(position = LatLng(i.latitude, i.longitude)),
                    title = i.title,
                    onClick = {
                        i.id?.let { id -> onClicked(id) }
                        true
                    }
                )
            }
        }
    }
}

private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun uriToBitmapDescriptor(context: Context, uri: Uri): BitmapDescriptor? {
    val bitmap = uriToBitmap(context, uri)
    return bitmap?.let {
        val scaledBitmap = Bitmap.createScaledBitmap(it, 150, 150, false)
        BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }
}