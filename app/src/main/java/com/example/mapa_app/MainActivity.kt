package com.example.mapa_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapa_app.ApiServiceBuilder.APIServiceBuilder
import com.example.mapa_app.adapters.SensorAdapter
import com.example.mapa_app.model.SensorResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var nav_host : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        nav_host = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        bottomNav = findViewById(R.id.bottom_bar)
        NavigationUI.setupWithNavController(bottomNav, nav_host.navController)



    }



}