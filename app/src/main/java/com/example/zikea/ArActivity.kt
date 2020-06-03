package com.example.zikea

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*


class ArActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var arrayView: Array<View>

    lateinit var arFragment: ArFragment

    lateinit var drawerRenderable: ModelRenderable
    lateinit var zaha_lightRenderable: ModelRenderable
    lateinit var modernRenderable: ModelRenderable
    lateinit var tableRenderable: ModelRenderable
    lateinit var cadeiraRenderable: ModelRenderable

    internal var selected = 1

    override fun onClick(view: View?){
        if (view!!.id == R.id.drawer){
            selected = 1
            selfSetBackground(view.id)
        }
        if (view!!.id == R.id.modern ){
            selected = 2
            selfSetBackground(view.id)
        }
        if (view!!.id == R.id.zaha_light ){
            selected = 3
            selfSetBackground(view.id)
        }
        if (view!!.id == R.id.cadeira ){
            selected = 4
            selfSetBackground(view.id)
        }
        if (view!!.id == R.id.table ){
            selected = 5
            selfSetBackground(view.id)
        }
    }

    private fun selfSetBackground(id: Int) {
        for (i in arrayView.indices){
            if (arrayView[i].id == id){
                arrayView[i].setBackgroundColor(android.graphics.Color.parseColor("#80333639"))
            }
            else{
                arrayView[i].setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        setupArray()
        setupClickListener()
        setupModel()

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener{hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            createModel(anchorNode, selected)
            startAnimation(TransformableNode(arFragment.transformationSystem), drawerRenderable);
        }
    }

    private fun createModel(anchorNode: AnchorNode, selected: Int){
        if (selected == 1){
            val drawer = TransformableNode(arFragment.transformationSystem)
            drawer.setParent(anchorNode)
            drawer.renderable = drawerRenderable
            drawer.select()
        }
        if (selected == 2){
            val modern = TransformableNode(arFragment.transformationSystem)
            modern.setParent(anchorNode)
            modern.renderable = modernRenderable
            modern.select()
        }
        if (selected == 3){
            val zaha_light = TransformableNode(arFragment.transformationSystem)
            zaha_light.setParent(anchorNode)
            zaha_light.renderable = zaha_lightRenderable
            zaha_light.select()
        }
        if (selected == 4){
            val cadeira = TransformableNode(arFragment.transformationSystem)
            cadeira.setParent(anchorNode)
            cadeira.renderable = cadeiraRenderable
            cadeira.select()
        }
        if (selected == 5){
            val table = TransformableNode(arFragment.transformationSystem)
            table.setParent(anchorNode)
            table.renderable = tableRenderable
            table.select()
        }
    }

    fun togglePauseAndResume(animator: ModelAnimator) {
        if (animator.isPaused) {
            animator.resume()
        } else if (animator.isStarted) {
            animator.pause()
        } else {
            animator.start()
        }
    }

    private fun setupModel(){
        ModelRenderable.builder().setSource(this, R.raw.drawer).build()
            .thenAccept{ modelRenderable -> drawerRenderable = modelRenderable}
            .exceptionally { throwable -> Toast.makeText(this@ArActivity,
                "Unable to load drawer", Toast.LENGTH_SHORT).show()
                null
            }

        ModelRenderable.builder().setSource(this, R.raw.modern).build()
            .thenAccept{ modelRenderable -> modernRenderable = modelRenderable}
            .exceptionally { throwable -> Toast.makeText(this@ArActivity,
                "Unable to load mordern", Toast.LENGTH_SHORT).show()
                null
            }

        ModelRenderable.builder().setSource(this, R.raw.zaha_light).build()
            .thenAccept{ modelRenderable -> zaha_lightRenderable = modelRenderable}
            .exceptionally { throwable -> Toast.makeText(this@ArActivity,
                "Unable to load zaha", Toast.LENGTH_SHORT).show()
                null
            }

        ModelRenderable.builder().setSource(this, R.raw.cadeira).build()
            .thenAccept{ modelRenderable -> cadeiraRenderable = modelRenderable}
            .exceptionally { throwable -> Toast.makeText(this@ArActivity,
                "Unable to load cadeira", Toast.LENGTH_SHORT).show()
                null
            }

        ModelRenderable.builder().setSource(this, R.raw.table).build()
            .thenAccept{ modelRenderable -> tableRenderable = modelRenderable}
            .exceptionally { throwable -> Toast.makeText(this@ArActivity,
                "Unable to load table", Toast.LENGTH_SHORT).show()
                null
            }
    }

    private fun startAnimation(node: TransformableNode?, renderable: ModelRenderable?) {
        if (renderable == null || renderable.animationDataCount == 0) {
            return
        }
        for (i in 0 until renderable.animationDataCount) {
            val animationData = renderable.getAnimationData(i)
        }
        val animator = ModelAnimator(renderable.getAnimationData(0), renderable)
        animator.start()
        node!!.setOnTapListener { hitTestResult, motionEvent -> togglePauseAndResume(animator) }
    }

    private fun setupClickListener(){
        for (i in arrayView.indices){
            arrayView[i].setOnClickListener(this)
        }
    }

    private fun setupArray() {
        arrayView = arrayOf(drawer, modern, zaha_light, cadeira, table)
    }
}
