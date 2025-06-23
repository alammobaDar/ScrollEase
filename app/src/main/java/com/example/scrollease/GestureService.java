package com.example.scrollease;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;

public class GestureService extends AccessibilityService {
    public GestureService() {
    }

    /**
     * Callback for {@link AccessibilityEvent}s.
     *
     * @param event The new event. This event is owned by the caller and cannot be used after
     *              this method returns. Services wishing to use the event after this method returns should
     *              make a copy.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    /**
     * Callback for interrupting the accessibility feedback.
     */
    @Override
    public void onInterrupt() {

    }

    public void swipeUp(){
        Path path = new Path();

        path.moveTo(500, 1500);
        path.lineTo(500, 500);

        GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(path, 0, 500);

        GestureDescription gesture = new GestureDescription.Builder().addStroke(stroke).build();

        dispatchGesture(gesture, null, null);
    }

}