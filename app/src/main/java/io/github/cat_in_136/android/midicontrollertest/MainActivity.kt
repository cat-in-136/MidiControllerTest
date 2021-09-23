package io.github.cat_in_136.android.midicontrollertest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        ViewModelProvider(this).get(MainViewModel::class.java)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        viewPager.isUserInputEnabled = false
        val pagerFragments = arrayOf(
            Pair(R.string.panel_pad, PadFragment())
        )
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = pagerFragments.size

            override fun createFragment(position: Int): Fragment {
                return pagerFragments[position].second
            }
        }
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(pagerFragments[position].first)
        }.attach()

    }

    override fun onStart() {
        super.onStart()

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.connectMidi(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.disconnectMidi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_quit -> {
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}