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
import com.bumptech.glide.Glide
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.AccessToken
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.json.JSONObject

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
        val idWithAccessToken = AccessToken.getCurrentAccessToken()?.userId
        val photourl = "https://graph.facebook.com/$idWithAccessToken/picture?type=large"

//        val photourlOne = "https://graph.facebook.com/${auth.currentUser?.uid}/picture?type=large"
//        val userId = auth.currentUser?.uid
//        val photourltwo = "https://graph.facebook.com/$userId/picture?type=large"
//        Picasso.get().load(auth.currentUser?.photoUrl).into(imgafterlogin)

//        if (!photourl.isNullOrEmpty()){
//            Picasso.get().load(photourl).into(imgafterlogin)
//          Glide.with(this).load(photourl).into(imgafterlogin)
//        }

//        else{
//            Toast.makeText(this, "Unable to get profile picture", Toast.LENGTH_SHORT).show()
//        }

        fetchProfilePicture()

        textafterlogin.setText(auth.currentUser?.displayName)

    }

    var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){

        override fun handleOnBackPressed() {
            finish()
        }

    }

    fun fetchProfilePicture(){

        val accessToken = AccessToken.getCurrentAccessToken()

        val request = GraphRequest.newMeRequest(
            accessToken,
            GraphRequest.GraphJSONObjectCallback { jsonObject, response ->
                val profilePictureUrl = jsonObject?.getJSONObject("picture")
                    ?.getJSONObject("data")
                    ?.getString("url")

                Picasso.get().load(profilePictureUrl).into(imgafterlogin)
            }
        )

        val parameters = Bundle()
        parameters.putString("fields", "picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

}