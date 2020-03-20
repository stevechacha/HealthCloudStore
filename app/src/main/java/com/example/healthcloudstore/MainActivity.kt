package com.example.healthcloudstore

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var providers : List<AuthUI.IdpConfig>	
    private val RC_SIGN_IN = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),   //Email Login
            AuthUI.IdpConfig.GoogleBuilder().build())  //Google Login


        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)

//        showSignInOption()
//
//    }
//
//    private fun showSignInOption() {
//        // Create and launch sign-in intent
//        startActivityForResult(AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser //get current user
                Toast.makeText(this, "Welcome" + user!!.email, Toast.LENGTH_SHORT).show()
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(this, "Oops!! Error signing in" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }






    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actions_menu, menu)

        //OR
/*        val inflater = menuInflater
        inflater.inflate(R.menu.actions_menu, menu)*/

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.log_out -> signOut()



            
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() : Boolean{

        var status: Boolean = false
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.signed_out), LENGTH_LONG).show()
                    status  =  true
                }
                else{
                    Toast.makeText(this, "Sign out failed", LENGTH_LONG).show()
                }

            }

        return status
    }

}
