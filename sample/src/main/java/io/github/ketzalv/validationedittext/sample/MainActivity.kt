package io.github.ketzalv.validationedittext.sample

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.ketzalv.validationedittext.sample.FormFragment.Companion.newInstance
import io.github.ketzalv.validationedittext.sample.base.BaseFragment
import io.github.ketzalv.validationedittext.sample.base.Event
import io.github.ketzalv.validationedittext.sample.base.IEvent

class MainActivity : AppCompatActivity(), IEvent, BottomNavigationView.OnNavigationItemSelectedListener {
    private var navigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        loadFragment(LoginFragment.newInstance())
    }

    private fun initViews() {
        navigationView = findViewById(R.id.nav_view)
        navigationView?.setOnNavigationItemSelectedListener(this)
    }

    private fun loadFragment(fragment: BaseFragment) {
        val backStateName = fragment.fragmentTag
        val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            try {
                val ft = supportFragmentManager.beginTransaction()
                //                if (!transition.equals(TransitionEnum.NONE)) {
//                    ft.setCustomAnimations(transition.getEnterAnimation(), transition.getExitAnimation(),
//                            transition.getEnterAnimation(), transition.getExitAnimation());
//                }
                ft.replace(R.id.frame_fragment_container, fragment, fragment.fragmentTag)
                //                if (addToBackStack) {
                ft.addToBackStack(fragment.fragmentTag)
                //                }
                ft.commit()
            } catch (e: Exception) {
            }
        }
    }

    override fun onEvent(event: Event?, any: Any?) {}
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_login -> {
                if (!currentFragment!!.fragmentTag.equals(LoginFragment.TAG, ignoreCase = true)) {
                    loadFragment(LoginFragment.newInstance())
                }
                return true
            }
            R.id.nav_register -> {
                if (!currentFragment!!.fragmentTag.equals(RegisterFragment.TAG, ignoreCase = true)) {
                    loadFragment(RegisterFragment.newInstance())
                }
                return true
            }
            R.id.nav_form_1 -> {
                if (!currentFragment!!.fragmentTag.equals(FormFragment.TAG, ignoreCase = true)) {
                    loadFragment(newInstance())
                }
                return true
            }
        }
        return false
    }

    protected val currentFragment: BaseFragment?
        protected get() = supportFragmentManager.findFragmentById(R.id.frame_fragment_container) as BaseFragment?

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}