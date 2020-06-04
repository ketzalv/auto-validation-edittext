package io.github.ketzalv.validationedittext.sample.base

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    private var mEvent: IEvent? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mEvent = context as IEvent
    }

    abstract val fragmentTag: String?
    val event: IEvent?
        get() = mEvent

    protected fun sendEvent(event: Event?) {
        mEvent?.let {
            it.onEvent(event, null)
        }
    }

    protected fun sendEvent(event: Event?, any: Any?) {
        mEvent?.let {
            it.onEvent(event, any)
        }
    }
}