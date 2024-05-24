package com.example.calc

import android.content.Intent
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable


class MainActivity : AppCompatActivity() {
    public lateinit var AC: Button
    public lateinit var back: Button
    public lateinit var power: Button
    public lateinit var persent: Button
    public lateinit var slach: Button
    public lateinit var tarh: Button
    public lateinit var darp: Button
    public lateinit var gammaa: Button
    public lateinit var equal: Button
    public lateinit var one: Button
    public lateinit var two: Button
    public lateinit var three: Button
    public lateinit var four: Button
    public lateinit var five: Button
    public lateinit var six: Button
    public lateinit var seven: Button
    public lateinit var eight: Button
    public lateinit var nine: Button
    public lateinit var zero: Button
    public lateinit var dot: Button
    public lateinit var real: TextView
    public lateinit var history: TextView
    public lateinit var save : ImageButton
    private val calculationList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        real = findViewById(R.id.real)




        AC = findViewById(R.id.AC)
        back = findViewById(R.id.back)
        persent = findViewById(R.id.persentage)
        slach = findViewById(R.id.slach)
        tarh = findViewById(R.id.tarh)
        darp = findViewById(R.id.darp)
        gammaa = findViewById(R.id.gamaa)
        equal = findViewById(R.id.equal)
        one = findViewById(R.id.one)
        two = findViewById(R.id.two)
        three = findViewById(R.id.three)
        four = findViewById(R.id.four)
        five = findViewById(R.id.five)
        six = findViewById(R.id.six)
        seven = findViewById(R.id.seven)
        eight = findViewById(R.id.eight)
        nine = findViewById(R.id.nine)
        zero = findViewById(R.id.zero)
        dot = findViewById(R.id.Dot)
        power = findViewById(R.id.power)

        history = findViewById(R.id.Result)

        save=findViewById(R.id.imageButton2)




        val buttons = listOf(
            slach,
            power
            ,persent,
            tarh,
            darp,
            gammaa,
            one,
            two,
            three,
            four,
            five,
            six,
            seven,
            eight,
            nine,
            zero,
            dot
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                real.append(button.text)
            }
        }

        save.setOnClickListener {




                var intent = Intent(this,MainActivity2::class.java)

            intent.putStringArrayListExtra("calculationList", calculationList)

            startActivity(intent)



        }

        equal.setOnClickListener {
            val expression = real.text.toString()
            try {
                val result = evaluate(expression)
                history.text = expression + " = " + result.toString()
                real.text = result.toString()
                calculationList.add(history.text.toString())
                savee(history.text.toString())
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid Expression", Toast.LENGTH_SHORT).show()
            }
        }


        AC.setOnClickListener {
            real.text = ""
            history.text = ""
        }

        back.setOnClickListener {
            val currentText = real.text.toString()
            if (currentText.isNotEmpty()) {
                real.text = currentText.substring(0, currentText.length - 1)
            }
        }

    }

    private fun savee(text: String) {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("calculations").document()
        docRef.set(mapOf("calculation" to history.text.toString()))
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error to save this calculation",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun evaluate(expression: String): Number {
        val context: Context = Context.enter()
        context.optimizationLevel = -1
        val scope: Scriptable = context.initStandardObjects()
        val result = context.evaluateString(scope, expression, "<cmd>", 1, null)
        val resultValue = Context.toNumber(result)

        return if (resultValue == resultValue.toInt().toDouble()) {
            resultValue.toInt()
        } else {
            resultValue
        }
    }









}