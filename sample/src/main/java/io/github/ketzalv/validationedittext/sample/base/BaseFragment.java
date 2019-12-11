package io.github.ketzalv.validationedittext.sample.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private IEvent mEvent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mEvent = (IEvent) context;
    }

    public abstract String getFragmentTag();

    public IEvent getEvent() {
        return mEvent;
    }

    protected void sendEvent(Event event) {
        if (mEvent != null) {
            mEvent.onEvent(event, null);
        }
    }

    protected void sendEvent(Event event, Object object) {
        if (mEvent != null) {
            mEvent.onEvent(event, object);
        }
    }
}
