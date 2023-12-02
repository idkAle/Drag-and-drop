package edu.farmingdale.alrajab.dragdropanimation_sc

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import edu.farmingdale.alrajab.dragdropanimation_sc.databinding.ActivityDragAndDropViewsBinding

class DragAndDropViews : AppCompatActivity() {
    lateinit var binding: ActivityDragAndDropViewsBinding
    lateinit var birdImageView: ImageView
    lateinit var animationDrawable: AnimationDrawable
    lateinit var startStopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragAndDropViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        birdImageView = findViewById(R.id.bird)
        animationDrawable = birdImageView.background as AnimationDrawable

        // Initialize the Start/Stop button after setContentView
        startStopButton = findViewById(R.id.startStopButton)

        // Set a click listener for the Start/Stop button
        startStopButton.setOnClickListener {
            toggleAnimation()
        }

        // Drag Listener for all holders
        binding.holder01.setOnDragListener(arrowDragListener)
        binding.holder02.setOnDragListener(arrowDragListener)
        binding.holder03.setOnDragListener(arrowDragListener)
        binding.holder04.setOnDragListener(arrowDragListener)
        binding.holder05.setOnDragListener(arrowDragListener)

        // Listeners for all arrows
        binding.upMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.downMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.forwardMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.backMoveBtn.setOnLongClickListener(onLongClickListener)

    }



    private val onLongClickListener = View.OnLongClickListener { view: View ->
        (view as? Button)?.let {

            val item = ClipData.Item(it.tag as? CharSequence)

            val dragData = ClipData( it.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val myShadow = ArrowDragShadowBuilder(it)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(dragData, myShadow, null, 0)
            } else {
                it.startDrag(dragData, myShadow, null, 0)
            }

            true
        }
        false
    }




    private val arrowDragListener = View.OnDragListener { view, dragEvent ->
        (view as? ImageView)?.let {
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Highlight the holder when hovering over it
                    view.setBackgroundResource(R.drawable.higlight_border)
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_EXITED-> {
                    // Unhighlight if the user is no hovering over holder
                    view.setBackgroundResource(R.drawable.border)
                    return@OnDragListener true
                }
                // No need to handle this for our use case.
                DragEvent.ACTION_DRAG_LOCATION -> {
                    return@OnDragListener true
                }

                DragEvent.ACTION_DROP -> {
                    // Read color data from the clip data and apply it to the card view background.
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    val lbl = item.text.toString()
                    view.setBackgroundResource(R.drawable.border)


                    Log.d("BCCCCCCCCCCC", "NOTHING > >  " + lbl)
                   when(lbl.toString()){
                       "UP"->view.setImageResource( R.drawable.ic_baseline_arrow_upward_24)
                       "DOWN"->view.setImageResource( R.drawable.ic_baseline_arrow_downward_24)
                       "BACK"->view.setImageResource( R.drawable.ic_baseline_arrow_back_24)
                       "FORWARD"->view.setImageResource( R.drawable.ic_baseline_arrow_forward_24)


                   }
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
        false
    }


    private class ArrowDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = view.background
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height
            shadow?.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }
        override fun onDrawShadow(canvas: Canvas) {
            shadow?.draw(canvas)
        }
    }


    private fun toggleAnimation() {
        if (animationDrawable.isRunning) {
            // If the animation is running, stop it and update the button text
            animationDrawable.stop()
            startStopButton.text = "Start"
        } else {
            // If the animation is not running, start it and update the button text
            animationDrawable.start()
            startStopButton.text = "Stop"
        }
    }
}