package com.pratamawijaya.blog.presentation.ui.home;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pratamawijaya.blog.R;
import com.pratamawijaya.blog.app.AppComponent;
import com.pratamawijaya.blog.presentation.base.BaseActivity;
import com.pratamawijaya.blog.presentation.pojo.event.PostSelectEvent;
import com.pratamawijaya.blog.presentation.pojo.event.ShowMessageEvent;
import com.pratamawijaya.blog.presentation.ui.home.di.DaggerHomeComponent;
import com.pratamawijaya.blog.presentation.ui.home.di.HomeModule;
import com.pratamawijaya.blog.presentation.ui.home.fragment.detail.DetailArticleFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeViewActivity extends BaseActivity {

  @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;
  @Bind(R.id.toolbar) Toolbar toolbar;

  private HomeFragmentOrganizer fragmentOrganizer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    fragmentOrganizer =
        new HomeFragmentOrganizer(getSupportFragmentManager(), R.id.fragmentContainer);
  }

  @Override protected void buildComponent(AppComponent appComponent) {
    DaggerHomeComponent.builder()
        .appComponent(appComponent)
        .homeModule(new HomeModule(this))
        .build()
        .inject(this);
  }

  @Override protected void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe public void onMessageEvent(ShowMessageEvent event) {
    Snackbar.make(rootLayout, event.message, Snackbar.LENGTH_SHORT);
  }

  @Subscribe public void onPostSelected(PostSelectEvent event) {
    fragmentOrganizer.openFragment(DetailArticleFragment.newInstance(event.post));
  }
}
