package com.example.proyectofinal_moviles

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proyectofinal_moviles.crudFragments.AgregarFragment
import com.example.proyectofinal_moviles.crudFragments.EliminarFragment
import com.example.proyectofinal_moviles.crudFragments.ListarFragment
import com.example.proyectofinal_moviles.crudFragments.ModificarFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(
        AgregarFragment(),
        ModificarFragment(),
        EliminarFragment(),
        ListarFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}