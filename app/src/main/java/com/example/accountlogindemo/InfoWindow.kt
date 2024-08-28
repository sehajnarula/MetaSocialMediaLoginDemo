package com.example.accountlogindemo

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.AccessToken
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class InfoWindow : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var imgafterlogin: ImageView
    private lateinit var textafterlogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info_window)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inits()

    }

    fun inits(){

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        imgafterlogin = findViewById(R.id.imgafterlogin)
        textafterlogin = findViewById(R.id.textafterlogin)
        val facebookUserId = AccessToken.getCurrentAccessToken()?.userId
        val photourl = "https://graph.facebook.com/$facebookUserId/picture?type=large"
        //Picasso.get().load(auth.currentUser?.photoUrl).into(imgafterlogin)
        Log.d("showurl",photourl)

        if (!photourl.isNullOrEmpty()){
            Picasso.get().load(photourl).into(imgafterlogin)
        }
        else{
            Toast.makeText(this, "Unable to get profile picture", Toast.LENGTH_SHORT).show()
        }

        textafterlogin.setText(auth.currentUser?.displayName)

    }

    var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){

        override fun handleOnBackPressed() {
            finish()
        }

    }

}