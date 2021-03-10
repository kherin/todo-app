package com.example.kherindo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val logOutItem: MenuItem = menu.findItem(R.id.action_logout)
        val currentUser = FirebaseAuth.getInstance().currentUser

        logOutItem.isVisible = currentUser == null
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> showAboutDialog()
            R.id.action_logout -> onSignOut()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog(): Boolean {
        return try {
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("About")
                .setMessage("Built by Kherin \nPS: stop procrastinating")
                .create()
                .show()
            return true
        } catch (e: Exception) {
            false
        }
    }

    private fun onSignOut(): Boolean {
        return true
    }
}