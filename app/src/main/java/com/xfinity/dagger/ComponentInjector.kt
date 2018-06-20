package com.xfinity.dagger

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.xfinity.CharacterViewerApplication
import com.xfinity.dagger.components.DaggerApplicationComponent
import com.xfinity.ui.navigation.NavigationDrawerActivity
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection



object ComponentInjector {

    fun initInjection(app: CharacterViewerApplication) {

        DaggerApplicationComponent.builder().application(app).build().injectApplicationComponent(app)

        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleInjectable(activity)
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun handleInjectable(activity: Activity) {
        if (activity is Injectable) {
            AndroidInjection.inject(activity)

            if (activity is NavigationDrawerActivity) {
                (activity as FragmentActivity).supportFragmentManager
                        .registerFragmentLifecycleCallbacks(
                                object : FragmentManager.FragmentLifecycleCallbacks() {
                                    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                                        if (f is Injectable) {
                                            AndroidSupportInjection.inject(f)
                                        }
                                    }
                                }, true)
            }
        }
    }
}