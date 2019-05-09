package com.example.weeklyplanner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainScreen extends AppCompatActivity {
   ListView planner;
    ArrayList<String> dates;
   ImageButton addMeal;
   ImageView meals;
   ArrayList<String> newMeals;
   File allRecipiesFile;
   ArrayList<Recipe> allRecipies;
   ArrayList<Recipe> mealsInUse_Recipe;
   ArrayList<String> selectedDays;
   File currentMeals;
   ArrayList<String> mealsInUse;
    ArrayList<String> allRecipiesString;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.main_nav_seeList) {
                Intent i = new Intent(MainScreen.this, ShoppingList.class);

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
            if(item.getItemId() == R.id.addMeal){
                Intent i;
                i = new Intent(getApplicationContext(), MealAdder.class);
                Log.i(String.valueOf(allRecipiesString),"recipies sent from main");
                i.putExtra("allRecipies",allRecipiesString);
                startActivity(i);
            }
            return false;
        }
    };
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        BottomNavigationView navView = findViewById(R.id.main_nav);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        planner=findViewById(R.id.planner);
        currentMeals = new File(getApplicationContext().getFilesDir(),"currentMeals");

         mealsInUse=ShoppingList.loadArray(currentMeals);
        mealsInUse_Recipe = initialise(mealsInUse_Recipe);
        newMeals = (ArrayList<String>)getIntent().getStringArrayListExtra("usedMeals");
        Log.i(String.valueOf(newMeals),"meals received from mealAdder to Main");
       newMeals = initialise(newMeals);
       allRecipies = initialise(allRecipies);
       mealsInUse = initialise(mealsInUse);
       mealsInUse.addAll(newMeals);
       ShoppingList.saveArray(mealsInUse,currentMeals);
        for(int j=0; j<mealsInUse.size(); j++){
            mealsInUse_Recipe.add(Recipe.toRecipe(mealsInUse.get(j)));
        }
        allRecipiesString = initialise(allRecipiesString);
        allRecipiesString.addAll(newMeals);

        Log.i(String.valueOf(allRecipiesString),"recipies to be sent from Main");
        //checkForMeals = bundle.getStringArrayList("usedMeals");
       selectedDays = initialise(selectedDays);
        for(int i=0;i<mealsInUse_Recipe.size();i++){
            selectedDays.add(mealsInUse_Recipe.get(i).getDay());
        }
        dates=generateDays();
        planner.setAdapter(new PlannerAdapter(this,dates,addMeal,meals,selectedDays));
    }
     public static ArrayList<String> generateDays(){
         ArrayList<String> dates;
         Calendar calendar = Calendar.getInstance();
         dates = new ArrayList<>();
         SimpleDateFormat sdf = new SimpleDateFormat("EEE dd");
         String currentDay = sdf.format(calendar.getTime());
         dates.add(currentDay);
         for(int i = 0; i<6; i++){
             calendar.add(Calendar.DATE,1);
             String nextDay = sdf.format(calendar.getTime());
             dates.add(nextDay);
         }
         return dates;
     }
     public static ArrayList initialise(ArrayList arrayList){
        if (arrayList == null){
            return new ArrayList();
        }
        else{
            return  arrayList;
        }
     }
}
