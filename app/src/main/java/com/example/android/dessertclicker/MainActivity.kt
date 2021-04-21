/*
 * Copyright 2020, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.dessertclicker

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log //log class for Log.d
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.example.android.dessertclicker.databinding.ActivityMainBinding


//상수는 여기다가 입력할 수 있구나...! 여기가 top level
const val TAG = "MainActivity"
const val KEY_REVENUE = "reveune_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
class MainActivity : AppCompatActivity() {

    private var revenue = 0 //수익
    private var dessertsSold = 0 //팔린 디저트 수

    // Contains all the views
    private lateinit var binding: ActivityMainBinding

    /** Dessert Data **/

    /**
     * Simple data class that represents a dessert. Includes the resource id integer associated with
     * the image, the price it's sold for, and the startProductionAmount, which determines when
     * the dessert starts to be produced.
     */
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

    // Create a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
            Dessert(R.drawable.cupcake, 5, 0),
            Dessert(R.drawable.donut, 10, 5),
            Dessert(R.drawable.eclair, 15, 20),
            Dessert(R.drawable.froyo, 30, 50),
            Dessert(R.drawable.gingerbread, 50, 100),
            Dessert(R.drawable.honeycomb, 100, 200),
            Dessert(R.drawable.icecreamsandwich, 500, 500),
            Dessert(R.drawable.jellybean, 1000, 1000),
            Dessert(R.drawable.kitkat, 2000, 2000),
            Dessert(R.drawable.lollipop, 3000, 4000),
            Dessert(R.drawable.marshmallow, 4000, 8000),
            Dessert(R.drawable.nougat, 5000, 16000),
            Dessert(R.drawable.oreo, 6000, 20000)
    )

    private var currentDessert = allDesserts[0]
    //onCreate(): 시스템이 활동을 생성할 때 실행되는 것, 필수적으로 구현해야함, MUST
    override fun onCreate(savedInstanceState: Bundle?) {
        //activity 생성을 complete 하기 위해 superclass implementation을 호출, MUST
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")
        //log의 tag를 변수 지정 후 설정할 수 있음.. top level에 설정함
        // Use Data Binding to get reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //바인딩 변수 설정 다음 코드 추가한다..
        if(savedInstanceState != null){
            //번들에 데이터가 있는 지 확인하여 앱이 새로 시작된건지 종료 후 다시 생성된건지 파악함.
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            //default value: 번들의 키에 해당하는 값이 존재하지 않을 때 쓸 값
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            showCurrentDessert() //팔린 수량에 따라서 디저트 이미지를 보여줌
        }
        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.imageId)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }
    //나머지 메소드들 추가.
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    //화면이 나와있을 때 소량의 데이터를 저장..
    override fun onSaveInstanceState(outState: Bundle) {
        //번들은 key-value 쌍인 컬렉션. key는 항상 string이다.
        outState.putInt(KEY_REVENUE, revenue)
        //putFloat, putString 가능..
        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
        super.onSaveInstanceState(outState)

        Log.d(TAG, "onSaveInstanceState Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    private fun onDessertClicked() {

        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Show the next dessert
        showCurrentDessert()

    }

    /**
     * 어떤 디저트를 보여줄 지 결정합니다.
     * Determine which dessert to show.
     */
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    /**
     * Menu methods
     */
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setText(getString(R.string.share_text, dessertsSold, revenue))
                .setType("text/plain")
                .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available),
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }
}
