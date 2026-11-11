package com.example.praticazero

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.praticazero.api.WeatherService

import com.example.praticazero.db.fb.FBCity
import com.example.praticazero.db.fb.FBDatabase
import com.example.praticazero.db.fb.FBUser
import com.example.praticazero.db.fb.toFBCity

import com.example.praticazero.ui.model.City
import com.example.praticazero.ui.model.User
import com.google.android.gms.maps.model.LatLng

class MainViewModel(private val db: FBDatabase, private val service : WeatherService) : ViewModel(), FBDatabase.Listener {

    private val _user = mutableStateOf<User?>(null)

    val user: User?
        get() = _user.value

    private val _cities: SnapshotStateList<City> =
        mutableListOf<City>().toMutableStateList()

    val cities: List<City>
        get() = _cities.toList()

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(
            City(
                name = name,
                location = location
            ).toFBCity()
        )
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
    }

    override fun onCityAdded(city: FBCity) {
        _cities.add(city.toCity())
    }

    override fun onCityUpdated(city: FBCity) {
        // Sem implementação por enquanto
    }

    override fun onCityRemoved(city: FBCity) {
        _cities.removeAll {
            it.name == city.name
        }
    }
    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name=name, location=LatLng(lat, lng)).toFBCity())
            }
        }
    }
    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location).toFBCity())
            }
        }
    }
}

class MainViewModelFactory(private val db: FBDatabase, private val service : WeatherService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db, service) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}