package io.github.ketzalv.validationedittext.sample;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.github.ketzalv.validationedittext.sample.base.BaseFragment;
import io.github.ketzalv.validationedittext.sample.base.Event;
import io.github.ketzalv.validationedittext.sample.base.IEvent;

public class MainActivity extends AppCompatActivity implements IEvent, BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadFragment(LoginFragment.newInstance());

    }

    private void initViews() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    protected void loadFragment(BaseFragment fragment) {
        String backStateName = fragment.getFragmentTag();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) { //fragment not in back stack, create it.
            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                if (!transition.equals(TransitionEnum.NONE)) {
//                    ft.setCustomAnimations(transition.getEnterAnimation(), transition.getExitAnimation(),
//                            transition.getEnterAnimation(), transition.getExitAnimation());
//                }
                ft.replace(R.id.frame_fragment_container, fragment, fragment.getFragmentTag());
//                if (addToBackStack) {
                ft.addToBackStack(fragment.getFragmentTag());
//                }
                ft.commit();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onEvent(Event event, Object object) {

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_login:
                if(!getCurrentFragment().getFragmentTag().equalsIgnoreCase(LoginFragment.TAG)){
                    loadFragment(LoginFragment.newInstance());
                }
                return true;
            case R.id.nav_register:
                if(!getCurrentFragment().getFragmentTag().equalsIgnoreCase(RegisterFragment.TAG)){
                    loadFragment(RegisterFragment.newInstance());
                }
                return true;
            case R.id.nav_form_1:
                if(!getCurrentFragment().getFragmentTag().equalsIgnoreCase(FormFragment.TAG)){
                    loadFragment(FormFragment.newInstance());
                }
                return true;
        }
        return false;
    }

    protected BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.frame_fragment_container);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
