package com.stpl.apptour

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.stpl.apptour.lib.TourPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var tourCounter = 0

    //-----------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        b_f1.post({

            startTourActivity(b_f1)

        })

        b_f1.setOnClickListener(this)
        b_f2.setOnClickListener(this)
        b_f3.setOnClickListener(this)
        b_f4.setOnClickListener(this)
        b_f5.setOnClickListener(this)
    }

//-----------------------------------------------------------------------------------------

    override fun onClick(v: View?) {

        tourCounter = 0
        startTourActivity(b_f1)

    }

//-----------------------------------------------------------------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TourPresenter.APP_TOUR && resultCode == Activity.RESULT_OK) {

            when (tourCounter) {
                1 -> startTourActivity(b_f2)
                2 -> startTourActivity(b_f3)
                3 -> startTourActivity(b_f4)
                4 -> startTourActivity(b_f5, true)
            }

        }
    }

//-----------------------------------------------------------------------------------------

    private fun startTourActivity(view: View, isLast: Boolean = false) {

        TourPresenter.showAppTour(
                this,
                view,
                "Title",
                "Description",
                if (isLast) "Finish" else "Next")

        tourCounter++
    }

//-----------------------------------------------------------------------------------------
}
