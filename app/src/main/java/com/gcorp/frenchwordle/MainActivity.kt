package com.gcorp.frenchwordle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBar
import com.gcorp.frenchwordle.ui.login.LoginActivity
import com.gcorp.frenchwordle.ui.login.RegisterActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

//// Image views
//import kotlinx.android.synthetic.main.activity_main.imageView_chart
//import kotlinx.android.synthetic.main.activity_main.imageView_help
import kotlinx.android.synthetic.main.activity_main.button_4letters
import kotlinx.android.synthetic.main.activity_main.button_5letters
import kotlinx.android.synthetic.main.activity_main.button_6letters


class MainActivity : AppCompatActivity() {

    private var Logged: Boolean = false;

    companion object  {
        var difficulty = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.main_abs_layout)

        //  Buttons
        button_4letters.setOnClickListener {
            difficulty = 0;
            val intent = Intent(this, Game()::class.java).apply{}
            intent.putExtra("key","four");
            startActivity(intent)
        }
        button_5letters.setOnClickListener {
            difficulty = 1;
            val intent = Intent(this, Game()::class.java).apply{}
            intent.putExtra("key","four");
            startActivity(intent)
        }
        button_6letters.setOnClickListener {
            difficulty = 2;
            val intent = Intent(this, Game()::class.java).apply{}
            intent.putExtra("key","four");
            startActivity(intent)
        }

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.statistics -> {
                    startActivity(Intent(this, StatisticsActivity::class.java))
                    true
                }
                R.id.help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }
                else -> false
            }
        }
//        findViewById<BottomNavigationView>(R.id.bottom_nav).itemIconTintList;

        if (!Logged) {
            startActivity(Intent(this, LoginActivity::class.java))

            Logged = true
        }
    }
}