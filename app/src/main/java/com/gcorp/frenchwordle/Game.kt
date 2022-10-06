package com.gcorp.frenchwordle

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gcorp.frenchwordle.MainActivity.Companion.difficulty
import kotlinx.android.synthetic.main.activity_game.*
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

const val apiUrl: String = "https://olktcvwka1.execute-api.us-west-1.amazonaws.com/dev"

class Game() : AppCompatActivity() {
    var buttonList: Array<Button> = emptyArray()
    var boxList: Array<EditText> = emptyArray()
    var word: String = ""
    var row: Int = 0
    var column: Int = 0
    var buttonMap: Map<Int, String> = emptyMap()
    var inputtedWords = Vector<String>();
    var currentWord: String = ""
    var charCtr: Int = 0
    var currentBox: String = "box"
    var boxCtr: Int = 0
    lateinit var textTimer : TextView
    var difficulty_time: Long = 0
    var secondsLeft = difficulty_time
    lateinit var tmr : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        print(difficulty)

        var i = Intent(this, MainActivity::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_game)

        textTimer = findViewById(R.id.textTimer)
        if (difficulty == 0)
            difficulty_time = 120000
        else if (difficulty == 1)
            difficulty_time = 90000
        else
            difficulty_time = 60000

        tmr = object : CountDownTimer(difficulty_time, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                secondsLeft = millisUntilFinished / 1000
                textTimer.setText("Time remaining: " + secondsLeft)
            }
            override fun onFinish() {
                textTimer.setText("You lose!")
                Handler().postDelayed({
                    startActivity(i)
                }, 2000)
            }
        }.start()

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.main_abs_layout);

    }
    override fun onStart() {
        buttonList = arrayOf(findViewById(R.id.buttonQ), findViewById(R.id.buttonW), findViewById(R.id.buttonE), findViewById(R.id.buttonR), findViewById(R.id.buttonT), findViewById(R.id.buttonY), findViewById(R.id.buttonU), findViewById(R.id.buttonI), findViewById(R.id.buttonO), findViewById(R.id.buttonP), findViewById(R.id.buttonA),findViewById(R.id.buttonS),findViewById(R.id.buttonD),findViewById(R.id.buttonF),findViewById(R.id.buttonG),findViewById(R.id.buttonH),findViewById(R.id.buttonJ), findViewById(R.id.buttonK),findViewById(R.id.buttonL),findViewById(R.id.buttonZ),findViewById(R.id.buttonX),findViewById(R.id.buttonC),findViewById(R.id.buttonV),findViewById(R.id.buttonB), findViewById(R.id.buttonN),findViewById(R.id.buttonM))
        boxList = arrayOf(findViewById(R.id.box0),findViewById(R.id.box1),findViewById(R.id.box2),findViewById(R.id.box3),findViewById(R.id.box4),findViewById(R.id.box5),findViewById(R.id.box6),findViewById(R.id.box7),findViewById(R.id.box8),findViewById(R.id.box9),findViewById(R.id.box10),findViewById(R.id.box11),findViewById(R.id.box12),findViewById(R.id.box13),findViewById(R.id.box14),findViewById(R.id.box15),findViewById(R.id.box16),findViewById(R.id.box17),findViewById(R.id.box18),findViewById(R.id.box19),findViewById(R.id.box20),findViewById(R.id.box21),findViewById(R.id.box22),findViewById(R.id.box23))

        var userLetters: Array<CharArray>
        val extras = intent.extras ?: throw Error("Null extras in Game class")
        sendRequest(0, apiUrl,
            fun (jsonObject: JSONObject?){if (jsonObject == null) {}
            else {
                val parsed = jsonObject[extras.get("key") as String] as String
                println("Word: " + parsed)
                //enable the return button
                word = parsed
            }
        })
        super.onStart()

        //Call on click listeners...
        buttonList.forEach {
            val buttn = it
            it.setOnClickListener{

                enterLetter(buttn)
            }
        }
        findViewById<ImageButton>(R.id.buttonEnter).setOnClickListener{
            //  Check chars that were typed
            // Update keyboard after submit
            if(charCtr >= 4)
            {
                checkInput(word, currentWord)
                currentWord = ""
                charCtr = 0
            }
            else
            {
                Toast.makeText(applicationContext,"Please enter four characters!",Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<ImageButton>(R.id.backspace).setOnClickListener {
            //  Check chars that were typed
            deleteLetter()
            }
        }

    fun removeLastChar(s: String?): String? {
        return if (s == null || s.length == 0) null else s.substring(0, s.length - 1)
    }


    private fun enterLetter(button: Button){
        charCtr++
        if(charCtr <= 4 && boxCtr <= 23){
            currentBox += boxCtr
            boxCtr++
            val letter = button.text
            currentWord += letter
            val resID = resources.getIdentifier(currentBox, "id", packageName)
            val editTextBox = findViewById<EditText>(resID)
            editTextBox.setText(letter)
            currentBox = "box"
        }
        else
        {
            Toast.makeText(applicationContext,"Character limit reached",Toast.LENGTH_SHORT).show()
        }

    }
    private fun deleteLetter(){
        charCtr--
        if(charCtr <= 4 && boxCtr <= 23){
            boxCtr--
            currentBox += boxCtr
            currentWord = currentWord.dropLast(1)
            println(currentWord)
            val resID = resources.getIdentifier(currentBox, "id", packageName)
            val editTextBox = findViewById<EditText>(resID)
            editTextBox.setText("")
            currentBox = "box"
        }
        else
        {
            Toast.makeText(applicationContext,"Character limit reached",Toast.LENGTH_SHORT).show()
        }

    }

    private fun winner(time: Long, name: String){
        //Show popup
    }

    private fun checkInput(word: String, input: String){
        if (word == ""){
            sendRequest(1, apiUrl)
            return
        }
        else if(word == input)
        {
            val winMessage = "Congratulations!\n\n\n\n\n\n\n\n\n\n You WON in " + ((difficulty_time/1000-secondsLeft)) + " seconds!"
            Toast.makeText(baseContext,winMessage,Toast.LENGTH_SHORT).show()
            tmr.cancel()
            textTimer.text = winMessage
            winner(difficulty_time-secondsLeft, "Player")
        }
        for (i in input.indices){
            var indexArr = emptyArray<Int>()
            if (input[i] == word[i]) {
                indexArr+=i
                for (button in buttonList)
                    if (button.text == input[i].toString())
                        button.setBackgroundColor(Color.argb(255,99,202,0))
                        boxList[i+row].setBackgroundResource(R.drawable.green_box)
            }
            else if (input[i] in word && input[i] != word[i] && indexArr.isEmpty()) {
                for (button in buttonList)
                    if (button.text == input[i].toString())
                        button.setBackgroundColor(Color.argb(255,202,99,0))
                        boxList[i+row].setBackgroundResource(R.drawable.orange_box)
            }
            else {
                for (button in buttonList)
                    if (button.text == input[i].toString())
                        button.setBackgroundColor(Color.argb(255, 10, 10, 10))

            }
        }
        row+= 4
    }
    private fun sendRequest(method: Int, urlStr: String, cb: (JSONObject?) -> Unit = {}) { //Calls api
// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
// Request a string response from the provided URL.
        var jsonMessage: JSONObject
        val stringRequest = StringRequest(
            method, urlStr,
            { response ->
                // Display the first 500 characters of the response string.
                jsonMessage = JSONTokener(response).nextValue() as JSONObject
                cb(jsonMessage)
            },
            { println("That didn't work!") })

        queue.add(stringRequest)
    }

}