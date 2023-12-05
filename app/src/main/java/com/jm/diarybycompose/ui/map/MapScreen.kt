package com.jm.diarybycompose.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
    viewModel: MainViewModel = hiltViewModel(),
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
            var image: BitmapDescriptor? = null
            loadBitmapFromUri(LocalContext.current, Uri.parse(i.imageUri)) { bitmapDescriptor ->
                image = bitmapDescriptor
            }
            if (i.imageUri != null) {
                Marker(
                    state = MarkerState(position = LatLng(i.latitude, i.longitude)),
                    title = i.title,
                    icon = image,
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

private fun loadBitmapFromUri(context: Context, uri: Uri, callBack: (BitmapDescriptor?) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(uri)
        .override(100, 100)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resource)
                callBack(bitmapDescriptor)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                callBack(null)
            }
        })
}