package jp.eijenson.connpass_searcher.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.eijenson.connpass_searcher.R
import jp.eijenson.connpass_searcher.view.ui.fragment.DevFragment
import jp.eijenson.connpass_searcher.view.ui.fragment.EventListFragment
import jp.eijenson.connpass_searcher.view.ui.fragment.FavoriteFragment
import jp.eijenson.connpass_searcher.view.ui.fragment.PrefsFragment
import kotlinx.android.synthetic.main.activity_main.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tool_bar)

        setOnNavigationItemSelected()
        supportFragmentManager.beginTransaction().add(page.id, EventListFragment()).commit()
    }

    // 下タブタップ
    private fun setOnNavigationItemSelected() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            if (bottom_navigation.selectedItemId == it.itemId) {
                return@setOnNavigationItemSelectedListener true
            }
            supportFragmentManager.fragments.forEach {
                supportFragmentManager.beginTransaction().remove(it).commitNow()
            }
            when (it.itemId) {
                R.id.list -> {
                    supportFragmentManager.beginTransaction().add(page.id, EventListFragment()).commit()
                }
                R.id.search -> {
                }
                R.id.favorite -> {
                    supportFragmentManager.beginTransaction().add(page.id, FavoriteFragment.newInstance()).commit()
                }
                R.id.setting -> {
                    supportFragmentManager.beginTransaction().add(page.id, PrefsFragment()).commit()
                }
                R.id.dev -> {
                    supportFragmentManager.beginTransaction().add(page.id, DevFragment()).commit()
                }
            }
            true
        }
    }
}
