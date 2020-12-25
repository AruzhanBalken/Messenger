package com.example.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.edit_user.*
import java.util.*

class EditUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_user)

        select_photo.setOnClickListener{
            Log.d("EditUserActivity", "show photo")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/jpg"
            startActivityForResult(intent, 0)
        }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == 0 && resultCode == Activity.RESULT_OK && data!=null)

        Log.d("EditUserActivity", "Photo is selected")
        selectedPhotoUri = data?.data
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
        select_photo_2.setImageBitmap(bitmap)
        select_photo_2.alpha = 0f
        uploadToFirebaseStorage()
    /*    val bitmapDrawable = BitmapDrawable(bitmap)
        select_photo.setBackgroundDrawable(bitmapDrawable)*/

    }
    private fun uploadToFirebaseStorage(){
        if(selectedPhotoUri==null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Edit", "successfully uploaded: ${it.metadata?.path}")
            }

    }
}

