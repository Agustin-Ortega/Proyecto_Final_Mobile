package com.example.mapa_app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mapa_app.fragments.Fragment_ListadoEstaciones
import com.example.mapa_app.fragments.Fragment_Mapa

class ViewPagerAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return TAB
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0 -> Fragment_ListadoEstaciones()
            1 -> Fragment_Mapa()
            else -> Fragment_Mapa()
        }
    }

    companion object{
        private const val TAB =2
    }

}