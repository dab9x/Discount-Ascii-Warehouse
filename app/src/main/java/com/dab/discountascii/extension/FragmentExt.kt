package com.dab.discountascii.extension

import android.util.Log
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, message, duration).show()
}

fun Fragment.toast(messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, messageResId, duration).show()
}

fun Fragment.log(message: String) {
    Log.e("test", message)
}

fun Fragment.replaceFragment(
    @IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = false, tag: String = fragment::class.java.simpleName
) {
    fragmentManager?.transact {
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }
}

fun Fragment.addFragment(
    fragment: Fragment, addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName
) {
    fragmentManager?.transact {
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(fragment, tag)
    }
}

fun Fragment.goBackFragment(): Boolean {
    if (fragmentManager != null) {
        var isShowPreviousPage = false
        fragmentManager?.backStackEntryCount.let {
            if (it != null) {
                if (it > 0) {
                    isShowPreviousPage = true
                }
            }
        }
        if (isShowPreviousPage) {
            fragmentManager?.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
    return false
}

inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}
