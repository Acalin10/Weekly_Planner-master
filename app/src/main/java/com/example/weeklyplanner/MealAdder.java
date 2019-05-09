package com.example.weeklyplanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.MacAddress;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MealAdder extends AppCompatActivity {
    ListView ingredients_list;
    Button add_ingredient;
    ArrayList<String> ingredientsForDayTemp;
    ArrayList<String> recipies;
    EditText mEdit;
    String[] dates;
    boolean[] checkedDates;
    ArrayList<Integer> mSelectedDates;
    String name;
    File saveInUseRecipies;
    ArrayList<String> allRecipiesString;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_adder);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ingredients_list = findViewById(R.id.ingredients_list);
        mEdit = findViewById(R.id.mealName);
        recipies = new ArrayList<String>();
        allRecipiesString = getIntent().getStringArrayListExtra("allRecipies");
        allRecipiesString = MainScreen.initialise(allRecipiesString);
        mEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        ingredientsForDayTemp = new ArrayList<>();
        dates = new String[7];
        ArrayList<String> dummyDates = MainScreen.generateDays();
        for(int i=0;i<dummyDates.size();i++){
            dates[i] = dummyDates.get(i);
        }
        mSelectedDates = new ArrayList<>();
        checkedDates = new boolean[dates.length];
        BottomNavigationView navView = findViewById(R.id.meal_adder_nav);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        add_ingredient = (Button) findViewById(R.id.add_ingredientsBtn);
        add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(MealAdder.this).create();
                        alertDialog.setTitle("Add Ingredient");
                        alertDialog.setMessage("Write an ingredient below and click ok");
                        final EditText input;
                        input = new EditText(getApplicationContext());
                        alertDialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        alertDialog.setView(input);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ingredientsForDayTemp.add(ShoppingList.preferedCase(input.getText().toString()));
                                        Log.i("my message",input.getText().toString());

                                      //  ShoppingList shoppingList = new ShoppingList();
                                        //shoppingList.list_items.add(input.getText().toString());
                                       // shoppingList.list_items_days.add(ShoppingList.preferedCase(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("currentDay",null)));
                                      //  ShoppingList.saveArray(shoppingList.list_items,shoppingList.shopping_list);
                                        //ShoppingList.saveArray(shoppingList.list_items_days,shoppingList.shopping_list_days);
                                        //ShoppingList.saveArray(ingredientsForDay,ingredientsForDay_list);
                                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                        dialog.dismiss();
                                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                    }
                                });
                        alertDialog.show();
                    }
                });

        ingredients_list.setAdapter(new IngredientAdapter(this,ingredientsForDayTemp));
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.meal_cancel:
                    Intent i1= new Intent(MealAdder.this, MainScreen.class);
                    startActivity(i1);

                case R.id.meal_done:
                     name = ShoppingList.preferedCase(mEdit.getText().toString());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MealAdder.this);
                    builder.setTitle("Which days should this meal be added to?");
                    builder.setMultiChoiceItems(dates, checkedDates, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){

                                if(! mSelectedDates.contains(which)){
                                    mSelectedDates.add(which);
                                }
                            }else if (mSelectedDates.contains(which)){
                                mSelectedDates.remove(mSelectedDates.indexOf(which));
                            }
                        }
                    });
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String item = "";
                            for (int i=0;i<mSelectedDates.size();i++){
                                item = dates[mSelectedDates.get(i)];
                                Recipe recipe;
                                recipe = new Recipe(name,ingredientsForDayTemp,item);
                                String toSave = recipe.toString();
                                recipies.add(toSave);

                            }
                            Intent i3 = new Intent(MealAdder.this,MainScreen.class);
                            Log.i(String.valueOf(recipies),"recipies to be sent from mealAdder to Main");
                            i3.putExtra("usedMeals",recipies);
                            Log.i(String.valueOf(i3.getExtras()),"recipies actually sent from mealAdder to Main");
                            startActivity(i3);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel and Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i3 = new Intent(MealAdder.this,MainScreen.class);
                            startActivity(i3);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case R.id.recipe_book:
                    Intent i4 = new Intent(MealAdder.this,RecipeBook.class);
                    Log.i(String.valueOf(allRecipiesString),"recipies sent from mealadder to Recipe");
                    i4.putExtra("allRecipiesForBook",allRecipiesString);
                    startActivity(i4);
                    break;
            }
            return false;
        }
    };
}
