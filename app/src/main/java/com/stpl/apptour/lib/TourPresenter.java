package com.stpl.apptour.lib;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Use this class to display the App Tour for any view on layout.
 * <p>
 * This class will invoke {@link AppTourActivity}  which will display an
 * overlay window on the calling activity keeping the passed view focused.
 * <p>
 * Use method {@link #showAppTour(AppCompatActivity, View, String, String, String)}
 * to invoke the App Tour on the view that is passed in the method.
 * <p>
 * This class will start {@link AppTourActivity} for result with Result code
 * {@link #APP_TOUR} which you can get in your {@link android.app.Activity#onActivityResult(int, int, Intent)}
 */
public class TourPresenter {

    // Request Code of calling App Tour Activity
    public static final int APP_TOUR = 111;

    public static String title = "";
    public static String description = "";
    public static String buttonName = "";
    public static RectF circlePos;

    // Direction for Max Free Space in the Screen
    // Used to display title and description in the free space
    public static FreeSpace maxFreeSpace;

    // Value of free space to define the size of title and description
    public static float maxFreeSpaceValue;

//-----------------------------------------------------------------------------------------

    /**
     * Enum to hold the directions
     * of free space in the screen after displaying
     * cut-out circle.
     */
    public enum FreeSpace {
        RIGHT, LEFT, TOP, BOTTOM
    }

//-----------------------------------------------------------------------------------------

    /**
     * Used to display {@link AppTourActivity} with the passed
     * view as the focused view in the app tour.
     *
     * @param activity    Activity Context used to call AppTour Activity for Result
     * @param view        View that needs to be focused
     * @param title       Title to be displayed for the view
     * @param description Description to be displayed for the view
     * @param buttonName  Name of the button to be displayed on the Tour (For ex: Next/Finish)
     */
    public static void showAppTour(AppCompatActivity activity, View view, String title, String description, String buttonName) {

        // Setup variables
        TourPresenter.title = title;
        TourPresenter.description = description;
        TourPresenter.buttonName = buttonName;
        TourPresenter.circlePos = getCoordinates(view);

        // Calculate the maximum free space available on the screen
        // for title and description after displaying the circle.
        TourPresenter.setMaxFreeSpace(activity);

        // Start app tour activity
        activity.startActivityForResult(new Intent(activity, AppTourActivity.class), APP_TOUR);
    }

//-----------------------------------------------------------------------------------------

    /**
     * Returns the {@link RectF} coordinates of cut-out circle from the view.
     * <p>
     * This function is to be used by {@link TourPresenter#showAppTour(AppCompatActivity, View, String, String, String)}
     * to calculate coordinates for the cut-out circle.
     * <p>
     * To get the circle position calculated by this method use {@link TourPresenter#circlePos}.
     *
     * @param view View that needs to be focused by cut-out circle
     * @return RectF position of the Cut-out Circle
     */
    private static RectF getCoordinates(View view) {

        // Get status bar height
        float statusBarHeight = getStatusBarHeight(view.getContext());

        // Create Array of 2 to get view position
        int[] viewPos = new int[2];

        // Get the view position in viewPos
        view.getLocationOnScreen(viewPos);

        // Calculate the size of circle (Width/Height of view whichever is max)
        int size = Math.max(view.getMeasuredWidth(), view.getMeasuredHeight()) + 10;

        // Calculate the center points of X and Y
        float centerX = viewPos[0] + (view.getMeasuredWidth() / 2);
        float centerY = viewPos[1] + (view.getMeasuredHeight() / 2);

        // Get left point
        float left = centerX - (size / 2);

        // Get right point
        float right = centerX + (size / 2);

        // Get top point
        float top = (centerY - (size / 2)) - statusBarHeight;

        // Get bottom point
        float bottom = (centerY + (size / 2)) - statusBarHeight;

        // Return position of circle
        return new RectF(left, top, right, bottom);
    }

//-----------------------------------------------------------------------------------------

    /**
     * Use this method to get the height of status bar.
     * <p>
     * Positions got by {@link View#getLocationOnScreen(int[])} includes Status-Bar.
     * So this method is required to reduce the status-bar height from the view position.
     *
     * @param context Context
     * @return Height of the Status Bar
     */
    private static float getStatusBarHeight(Context context) {

        // Get the status bar height resource id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            // Return Status bar height
            return context.getResources().getDimensionPixelSize(resourceId);
        }

        // Return 0 if no status bar height is found
        return 0;
    }

//-----------------------------------------------------------------------------------------

    /**
     * Use this method to get Pixels from DP
     *
     * @param context Context
     * @param dp      Density Pixels Value in float
     * @return DP value in Pixels
     */
    public static float getPxFromDp(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

//-----------------------------------------------------------------------------------------

    /**
     * Calculates the maximum free space on the screen
     * after Cut-Out circle is drawn.
     * <p>
     * This calculated free space is used to dynamically display the
     * Title and Description on the layout.
     * <p>
     * The calculated values are stored in {@link TourPresenter#maxFreeSpace}
     * and {@link TourPresenter#maxFreeSpaceValue}.
     *
     * @param context Context
     */
    private static void setMaxFreeSpace(Context context) {

        // Get display metrics for width and height calculations
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        // Get all sides spaces
        float topSpace = TourPresenter.circlePos.top;
        float bottomSpace = metrics.heightPixels - TourPresenter.circlePos.bottom;
        float leftSpace = TourPresenter.circlePos.left;
        float rightSpace = metrics.widthPixels - TourPresenter.circlePos.right;


        // Check max space between width or height
        if ((topSpace >= leftSpace && topSpace >= rightSpace)
                || (bottomSpace >= leftSpace && bottomSpace >= rightSpace)) {

            if (topSpace >= bottomSpace) {

                TourPresenter.maxFreeSpace = FreeSpace.TOP;
                maxFreeSpaceValue = topSpace;

            } else {

                TourPresenter.maxFreeSpace = FreeSpace.BOTTOM;
                maxFreeSpaceValue = bottomSpace;
            }

        } else {

            if (leftSpace >= rightSpace) {

                TourPresenter.maxFreeSpace = FreeSpace.LEFT;
                maxFreeSpaceValue = leftSpace;

            } else {

                TourPresenter.maxFreeSpace = FreeSpace.RIGHT;
                maxFreeSpaceValue = rightSpace;
            }

        }

    }

//-----------------------------------------------------------------------------------------
}
