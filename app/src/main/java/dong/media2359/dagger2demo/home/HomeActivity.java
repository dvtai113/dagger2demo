package dong.media2359.dagger2demo.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import dong.media2359.dagger2demo.DemoApplication;
import dong.media2359.dagger2demo.R;
import dong.media2359.dagger2demo.di.AppComponent;
import dong.media2359.dagger2demo.imageloader.ImageLoader;
import dong.media2359.dagger2demo.itemlist.ItemListFragment;
import dong.media2359.dagger2demo.itemlist.ItemListFragment2;
import dong.media2359.dagger2demo.util.LogUtil;

/**
 * Created by Dong (nguyen.dong@2359media.com) on 11/10/17.
 */
// For demo purpose, we will let HomeActivity implements HasSupportFragmentInjector.
// In practice, we can let DemoApplication implements this for simplicity.
public class HomeActivity extends FragmentActivity implements HasSupportFragmentInjector {
    @Inject
    ImageLoader imageLoader;
    @Inject
    HomePresenter homePresenter;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingSupportFragmentInjector;

    private HomeComponent homeComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // imageLoader and homePresenter should not be null after this line
        AndroidInjection.inject(this);

        // still leave this part untouched for child fragments
        AppComponent appComponent = ((DemoApplication) getApplication()).getAppComponent();
        HomeComponent.Builder homeBuilder = appComponent.homeBuilder();
        homeBuilder.seedInstance(this);
        // retain homeComponent so that child fragments can use it
        homeComponent = (HomeComponent) homeBuilder.build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.bt_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ItemListFragment.class.getName());
            }
        });

        findViewById(R.id.bt_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ItemListFragment2.class.getName());
            }
        });

        LogUtil.logDependencies(this);
    }

    public HomeComponent getHomeComponent() {
        return homeComponent;
    }

    private void showFragment(String className) {
        Fragment fragment = Fragment.instantiate(this, className);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_list_content, fragment)
                .commit();
    }

    @Override
    public String toString() {
        return "HomeActivity{" +
                "imageLoader=" + imageLoader +
                ", homePresenter=" + homePresenter +
                '}';
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingSupportFragmentInjector;
    }
}
