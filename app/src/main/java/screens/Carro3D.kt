package com.example.leyesmx.screens

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.github.sceneview.SceneView
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode



@Composable
fun Carro3D(modifier: Modifier = Modifier) {
    var sceneViewRef by remember { mutableStateOf<SceneView?>(null) }

    AndroidView(
        factory = { context ->
            val sceneView = SceneView(context)



            val lightNode = io.github.sceneview.node.LightNode(sceneView.engine)
            sceneView.addChildNode(lightNode)

            sceneViewRef = sceneView
            sceneView
        },

                modifier = modifier
    )

    LaunchedEffect(sceneViewRef) {
        sceneViewRef?.let { sceneView ->
            try {
                val modelLoader = ModelLoader(sceneView.engine, sceneView.context)

                val modelInstance = modelLoader.loadModel("file:///android_asset/models/carro.glb")

                modelInstance?.let {
                    val modelNode = ModelNode(
                        modelInstance = it.instance,
                        autoAnimate = true,
                        scaleToUnits = 15.0f,
                        centerOrigin = Position(0f, 0f, 0f)
                    ).apply {
                        position = Position(0f, -1f, -3f)
                        scale = Scale(15f)
                    }


                    sceneView.addChildNode(modelNode)
                } ?: Log.e("Carro3D", "No se pudo cargar el modelo: es null")
            } catch (e: Exception) {
                Log.e("Carro3D", "Error cargando modelo: ${e.localizedMessage}")
            }
        }
    }
}
