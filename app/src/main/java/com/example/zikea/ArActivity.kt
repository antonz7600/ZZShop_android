package com.example.zikea

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
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

    lateinit var viewRenderable: ModelRenderable

    internal var selected = 100

    var currentColor = Color.rgb(0,0,0)

    override fun onClick(view: View?){
        Log.v("TAAAG", view!!.id.toString())
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

    private fun insertDatabase() {
//        val key1 = Key(21, "zaha_light", 21)
//        val key2 = Key(25, "drawer", 25)
//        val key3 = Key(23, "modern", 23)
//        val key4 = Key(24, "cadeira", 24)
//        val key5 = Key(22, "table", 22)
//        key1.save()
//        key2.save()
//        key3.save()
//        key4.save()
//        key5.save()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        val arguments = intent.extras
        val id = arguments!!["id"].toString().toInt()

        val key = com.orm.SugarRecord.find(Key::class.java, "key = ?", id.toString())[0]

        setupArray()
        setupClickListener()
        setupModel()

        key.name?.let { setupModelByName(it) }

        selected = id
        selfSetBackground(id)

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        arFragment.setOnTapArPlaneListener{hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
            if ((selected > 0) and (selected <= 5))
                createModel(anchorNode, selected)
            else
                createModelByName(anchorNode)
            startAnimation(TransformableNode(arFragment.transformationSystem), viewRenderable);
        }

        fab.setOnClickListener { arrayListener() }

        fab_color.setOnClickListener { colorListener() }
    }

    private fun colorListener() {
        ColorPickerDialog
            .Builder(this)
            .setTitle("Pick the color")   // Pass Activity Instance
            .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
            .setDefaultColor(Color.rgb(255, 255, 0))        	// Pass Default Color
            .setColorListener { color, colorHex ->
                currentColor = color
            }
            .show()
    }

    private fun arrayListener() {
        val array = findViewById<View>(R.id.array_choose)
        if (array.visibility == View.INVISIBLE)
            array.visibility = View.VISIBLE
        else{
            array.visibility = View.INVISIBLE
            selected = 100
        }
    }

    private fun setColor(rendereble: ModelRenderable) : ModelRenderable {
        val renderableCopy = rendereble.makeCopy()
        val changedMaterial = renderableCopy.material.makeCopy()
        changedMaterial.setFloat3("baseColorTint",
            com.google.ar.sceneform.rendering.Color(currentColor)
        )
        renderableCopy.material = changedMaterial
        return renderableCopy
    }

    private fun createModelByName(anchorNode: AnchorNode) {
        val modelNode = TransformableNode(arFragment.transformationSystem)
        modelNode.setParent(anchorNode)
        viewRenderable = setColor(viewRenderable)
        modelNode.renderable = viewRenderable
        modelNode.select()
    }

    private fun setupModelByName(name: String){
        val res: Resources = this.resources
        val modelId: Int = res.getIdentifier(name, "raw", this.packageName)
        ModelRenderable.builder().setSource(this, modelId).build()
            .thenAccept{ modelRenderable -> viewRenderable = modelRenderable}
            .exceptionally { throwable -> Toast.makeText(this@ArActivity,
                "Unable to load this model", Toast.LENGTH_SHORT).show()
                null
            }
    }

    private fun createModel(anchorNode: AnchorNode, selected: Int){
        if (selected == 1){
            val drawer = TransformableNode(arFragment.transformationSystem)
            drawer.setParent(anchorNode)
            drawerRenderable = setColor(drawerRenderable)
            drawer.renderable = drawerRenderable
            drawer.select()
        }
        if (selected == 2){
            val modern = TransformableNode(arFragment.transformationSystem)
            modern.setParent(anchorNode)
            modernRenderable = setColor(modernRenderable)
            modern.renderable = modernRenderable
            modern.select()
        }
        if (selected == 3){
            val zaha_light = TransformableNode(arFragment.transformationSystem)
            zaha_light.setParent(anchorNode)
            zaha_lightRenderable = setColor(zaha_lightRenderable)
            zaha_light.renderable = zaha_lightRenderable
            zaha_light.select()
        }
        if (selected == 4){
            val cadeira = TransformableNode(arFragment.transformationSystem)
            cadeira.setParent(anchorNode)
            cadeiraRenderable = setColor(cadeiraRenderable)
            cadeira.renderable = cadeiraRenderable
            cadeira.select()
        }
        if (selected == 5){
            val table = TransformableNode(arFragment.transformationSystem)
            table.setParent(anchorNode)
            tableRenderable = setColor(tableRenderable)
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
