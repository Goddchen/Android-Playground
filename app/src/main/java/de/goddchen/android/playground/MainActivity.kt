package de.goddchen.android.playground

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.NavigationUI
import de.goddchen.android.playground.playgrounds.PlayGroundList
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        loadPlayGroundsMenu()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
        } else {
            super.onBackPressed()
        }

    }

    private fun loadPlayGroundsMenu() {
        PlayGroundList.playGrounds
            .forEach {
                val generatedDestinationId = ViewCompat.generateViewId()
                val generatedActionId = ViewCompat.generateViewId()
                navController.graph.addDestination(FragmentNavigator(
                    this,
                    supportFragmentManager,
                    R.id.nav_host_fragment
                )
                    .createDestination().apply {
                        id = generatedDestinationId
                        className = it.framgentClassName
                    })
                navController.graph.putAction(generatedActionId, generatedDestinationId)
                navigation_view.menu.add(it.title).setOnMenuItemClickListener {
                    navController.navigate(generatedActionId)
                    drawer_layout.closeDrawers()
                    true
                }
            }
    }
}

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_main, container, false)
}