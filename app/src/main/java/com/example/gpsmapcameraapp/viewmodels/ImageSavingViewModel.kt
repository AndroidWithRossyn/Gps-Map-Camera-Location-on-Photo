package com.example.gpsmapcameraapp.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PointF
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.SupportMapFragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageSavingViewModel : ViewModel() {

    var isDefaultFolderSelected = true
    var isSiteOneFolderSelected = true
    var isSiteTwoFolderSelected = true

    @SuppressLint("InflateParams")
    private fun captureImageDefault(context: Context, previewView: PreviewView, cardView1: View, cardView2: View, mapFragment: SupportMapFragment) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "Camera/IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector, preview, imageCapture)

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            outputFileResults.savedUri ?: Uri.fromFile(file)
                            val capturedBitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val combinedBitmap = Bitmap.createBitmap(capturedBitmap.width, capturedBitmap.height, Bitmap.Config.ARGB_8888)
                            val canvas = Canvas(combinedBitmap)
                            canvas.drawBitmap(capturedBitmap, 0f, 0f, null)

                            val scaleFactor = 3.5f
                            val card1Position = PointF(800f, 2480f)
                            val card2Position = PointF(30f, 2480f)

                            drawViewOnCanvas(cardView1, canvas, scaleFactor, card1Position)
                            drawViewOnCanvas(cardView2, canvas, scaleFactor, card2Position)

                            mapFragment.getMapAsync { googleMap ->
                                googleMap.snapshot { mapBitmap ->
                                    if (mapBitmap != null) {
                                        drawMapOnCanvas(
                                            mapBitmap,
                                            canvas,
                                            scaleFactor,
                                            card2Position
                                        )
                                        saveCombinedBitmap(combinedBitmap, file)
                                        Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                        mediaScanIntent.data = Uri.fromFile(file)
                                        context.sendBroadcast(mediaScanIntent)
                                    }
                                }
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(TAG, "Image capture failed: ${exception.message}", exception)
                            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error binding camera use cases: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun drawViewOnCanvas(view: View, canvas: Canvas, scaleFactor: Float, position: PointF) {
        val tempBitmap = Bitmap.createBitmap((view.width * scaleFactor).toInt(), (view.height * scaleFactor).toInt(), Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(tempBitmap)
        tempCanvas.scale(scaleFactor, scaleFactor)
        view.draw(tempCanvas)
        canvas.drawBitmap(tempBitmap, position.x, position.y, null)
    }

    private fun drawMapOnCanvas(
        mapBitmap: Bitmap,
        canvas: Canvas,
        scaleFactor: Float,
        position: PointF
    ) {
        val scaledMapBitmap = Bitmap.createScaledBitmap(mapBitmap, (mapBitmap.width * scaleFactor).toInt(), (mapBitmap.height * scaleFactor).toInt(), true)
        canvas.drawBitmap(scaledMapBitmap, position.x, position.y, null)
    }

    private fun saveCombinedBitmap(bitmap: Bitmap, file: File) {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    private fun captureImageSiteOne(context: Context, previewView: PreviewView, cardView1: View, cardView2: View, mapFragment: SupportMapFragment) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "GPS Map Camera App/Site 1/IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
                )
                file.parentFile?.mkdirs()
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector, preview, imageCapture)

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            outputFileResults.savedUri ?: Uri.fromFile(file)
                            val capturedBitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val combinedBitmap = Bitmap.createBitmap(capturedBitmap.width, capturedBitmap.height, Bitmap.Config.ARGB_8888)
                            val canvas = Canvas(combinedBitmap)
                            canvas.drawBitmap(capturedBitmap, 0f, 0f, null)

                            val scaleFactor = 3.5f
                            val card1Position = PointF(800f, 2480f)
                            val card2Position = PointF(30f, 2480f)

                            drawViewOnCanvas(cardView1, canvas, scaleFactor, card1Position)
                            drawViewOnCanvas(cardView2, canvas, scaleFactor, card2Position)

                            mapFragment.getMapAsync { googleMap ->
                                googleMap.snapshot { mapBitmap ->
                                    if (mapBitmap != null) {
                                        drawMapOnCanvas(
                                            mapBitmap,
                                            canvas,
                                            scaleFactor,
                                            card2Position
                                        )
                                        saveCombinedBitmap(combinedBitmap, file)
                                        Toast.makeText(context, "Image saved to DCIM/GPS Map Camera App/Site 1", Toast.LENGTH_SHORT).show()
                                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                        mediaScanIntent.data = Uri.fromFile(file)
                                        context.sendBroadcast(mediaScanIntent)
                                    }
                                }
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(TAG, "Image capture failed: ${exception.message}", exception)
                            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error binding camera use cases: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun captureImageSiteTwo(context: Context, previewView: PreviewView, cardView1: View, cardView2: View, mapFragment: SupportMapFragment) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "GPS Map Camera App/Site 2/IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
                )
                file.parentFile?.mkdirs()
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(context as LifecycleOwner, cameraSelector, preview, imageCapture)

                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            outputFileResults.savedUri ?: Uri.fromFile(file)
                            val capturedBitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val combinedBitmap = Bitmap.createBitmap(capturedBitmap.width, capturedBitmap.height, Bitmap.Config.ARGB_8888)
                            val canvas = Canvas(combinedBitmap)
                            canvas.drawBitmap(capturedBitmap, 0f, 0f, null)

                            val scaleFactor = 3.5f
                            val card1Position = PointF(800f, 2480f)
                            val card2Position = PointF(30f, 2480f)

                            drawViewOnCanvas(cardView1, canvas, scaleFactor, card1Position)
                            drawViewOnCanvas(cardView2, canvas, scaleFactor, card2Position)

                            mapFragment.getMapAsync { googleMap ->
                                googleMap.snapshot { mapBitmap ->
                                    if (mapBitmap != null) {
                                        drawMapOnCanvas(
                                            mapBitmap,
                                            canvas,
                                            scaleFactor,
                                            card2Position
                                        )
                                        saveCombinedBitmap(combinedBitmap, file)
                                        Toast.makeText(context, "Image saved to DCIM/GPS Map Camera App/Site 2", Toast.LENGTH_SHORT).show()
                                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                        mediaScanIntent.data = Uri.fromFile(file)
                                        context.sendBroadcast(mediaScanIntent)
                                    }
                                }
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(TAG, "Image capture failed: ${exception.message}", exception)
                            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error binding camera use cases: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun captureAndSaveImageDefault(context: Context, previewView: PreviewView, cardView1: View, cardView2: View, mapFragment: SupportMapFragment) {
        if (isDefaultFolderSelected) {
            captureImageDefault(context, previewView, cardView1, cardView2, mapFragment)
        } else {
            Toast.makeText(context, "Default folder not selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun captureAndSaveImageSiteOne(context: Context, previewView: PreviewView, cardView1: View, cardView2: View, mapFragment: SupportMapFragment) {
        if (isSiteOneFolderSelected) {
            captureImageSiteOne(context, previewView, cardView1, cardView2, mapFragment)
        } else {
            Toast.makeText(context, "Site 1 folder not selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun captureAndSaveImageSiteTwo(context: Context, previewView: PreviewView, cardView1: View, cardView2: View, mapFragment: SupportMapFragment) {
        if (isSiteTwoFolderSelected) {
            captureImageSiteTwo(context, previewView, cardView1, cardView2, mapFragment)
        } else {
            Toast.makeText(context, "Site 2 folder not selected", Toast.LENGTH_SHORT).show()
        }
    }



}