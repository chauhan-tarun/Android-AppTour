package com.stpl.apptour.lib;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stpl.apptour.R;

/**
 * This Activity will display and overlay window on top of the calling
 * Activity and focus the view that is passed while calling this Activity
 * using a Cut-Out circle on the overlay.
 * <p>
 * This activity is only meant to be called by {@link TourPresenter}.
 * Do not call this activity directly from your activity as it may result
 * in crashes and unexpected behavior.
 * <p>
 * This activity will pass the activity result on {@link TourPresenter#APP_TOUR}
 * requestCode. So that you could get the result on your end when any action is performed
 * on this Activity using {@link android.app.Activity#onActivityResult(int, int, Intent)}.
 */
public class AppTourActivity extends AppCompatActivity {

//-----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the overlay content view
        setContentView(getTourView());
    }


//-----------------------------------------------------------------------------------------

    /**
     * Click listener to finish the tour overlay and pass
     * the result to the calling activity.
     */
    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_OK);
            finish();
        }
    };

//-----------------------------------------------------------------------------------------

    /**
     * This method creates the complete view of the App Tour.
     * Use this method in {@link #setContentView(View)}
     *
     * @return Final view for the App Tour
     */
    private View getTourView() {

        // Create root view
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Create background overlay with cut out circle
        OverlayWithHoleImageView iv = new OverlayWithHoleImageView(this, null);
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setCircle(TourPresenter.circlePos);

        // Add overlay background to root view
        layout.addView(iv);


        // Create Linear Layout to hold title and description
        LinearLayout titleAndDescLayout = getTitleAndDescLayout();

        // Create and add title if exists
        if (TourPresenter.title != null) {

            TextView tvTitle = new TextView(this);

            tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            tvTitle.setText(TourPresenter.title);
            tvTitle.setTextSize(25f);
            tvTitle.setTextColor(Color.WHITE);

            titleAndDescLayout.addView(tvTitle);
        }

        // Create and add description if exists
        if (TourPresenter.description != null) {

            TextView tvDesc = new TextView(this);

            // For Center
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // For TOP
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);

            params.weight = 1f;

            tvDesc.setLayoutParams(params);
            tvDesc.setText(TourPresenter.description);
            tvDesc.setTextSize(20f);
            tvDesc.setTextColor(Color.WHITE);

            titleAndDescLayout.addView(tvDesc);
        }


        // Add button to title and description layout
        layout.addView(getButton());

        // Add title and description layout to root layout
        layout.addView(titleAndDescLayout);

        // Return final view
        return layout;
    }

//-----------------------------------------------------------------------------------------

    /**
     * Use this method to create ready-to-add-child LinearLayout
     * with defined size and position on the root layout.
     * <p>
     * You can add title and description in this layout and any other view
     * that needs to be displayed below the title and description.
     *
     * @return LinearLayout ready to add child views.
     */
    private LinearLayout getTitleAndDescLayout() {

        // Initialize title and desc layout
        LinearLayout titleAndDescLayout = new LinearLayout(this);

        // Get the Relative Layout params
        RelativeLayout.LayoutParams params;

        // Set Layout size and position on basis of free space
        if (TourPresenter.maxFreeSpace == TourPresenter.FreeSpace.TOP) {

            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    (int) TourPresenter.maxFreeSpaceValue);

            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            titleAndDescLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        } else if (TourPresenter.maxFreeSpace == TourPresenter.FreeSpace.BOTTOM) {

            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    (int) TourPresenter.maxFreeSpaceValue);

            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            titleAndDescLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        } else if (TourPresenter.maxFreeSpace == TourPresenter.FreeSpace.LEFT) {

            params = new RelativeLayout.LayoutParams(
                    (int) TourPresenter.maxFreeSpaceValue,
                    RelativeLayout.LayoutParams.MATCH_PARENT);

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            titleAndDescLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        } else {

            params = new RelativeLayout.LayoutParams(
                    (int) TourPresenter.maxFreeSpaceValue,
                    RelativeLayout.LayoutParams.MATCH_PARENT);

            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            titleAndDescLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        // Set layout params
        titleAndDescLayout.setLayoutParams(params);

        // Set orientation of linear layout
        titleAndDescLayout.setOrientation(LinearLayout.VERTICAL);

        // Set padding
        int padding = (int) TourPresenter.getPxFromDp(this, 10);
        titleAndDescLayout.setPadding(padding, padding, padding, padding);

        // Return title and description layout
        return titleAndDescLayout;
    }

//-----------------------------------------------------------------------------------------

    /**
     * This method creates the button that needs to be displayed on the
     * root layout.
     * <p>
     * This button is basically used to ask user to move to
     * the next view or to finish App Tour.
     * <p>
     * On clicking this button the App tour(this activity) will finish
     * by passing the result as {@link #RESULT_OK} so that you could listen for
     * the event and continue your flow of application.
     * <p>
     * For more see {@link #buttonClickListener}
     */
    private Button getButton() {

        // Create button
        Button btn = new Button(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        btn.setText(TourPresenter.buttonName);
        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        btn.setTextColor(Color.WHITE);
        btn.setOnClickListener(buttonClickListener);

        // Get display metrics for width and height calculations
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // Set margins
        int margin = (int) TourPresenter.getPxFromDp(this, 15);
        params.setMargins(margin, margin, margin, margin);

        // Set button position
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (TourPresenter.circlePos.top > (metrics.heightPixels - TourPresenter.circlePos.bottom) && TourPresenter.circlePos.left > (metrics.widthPixels - TourPresenter.circlePos.right)) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }

        // Set the layout params to button
        btn.setLayoutParams(params);

        // Return button
        return btn;
    }

//-----------------------------------------------------------------------------------------
}
