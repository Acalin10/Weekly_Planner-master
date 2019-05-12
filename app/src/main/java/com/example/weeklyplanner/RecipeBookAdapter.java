package com.example.weeklyplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeBookAdapter  extends BaseAdapter {
    ArrayList<String> myRecipiesString;
    ArrayList<Recipe> myRecipies;
    LayoutInflater mInflater;

    public ArrayList<Recipe> getMyRecipies() {
        return myRecipies;
    }

    RecipeBookAdapter(Context context, ArrayList<String> recipeBook, File file){
        this.myRecipiesString=recipeBook;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myRecipies = new ArrayList<>();
        for(int i=0;i<myRecipiesString.size();i++){
            myRecipies.add(Recipe.toRecipe(myRecipiesString.get(i)));
        }

    }
    @Override
    public int getCount() {
        return myRecipiesString.size();
    }

    @Override
    public Object getItem(int position) {
        return myRecipiesString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.recipe_book_view, null);

        TextView recipeTextView = v.findViewById(R.id.recipeText);
        ImageButton delete_recipe = v.findViewById(R.id.recipe_delete);
        delete_recipe.setFocusable(false);
        delete_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String name = myRecipies.get(position).getName();
        recipeTextView.setText(name);
        return v;
    }
    public static class OnItemAutoCompleteAdder implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                String autocomplete;
                final String selected = (String) a.getItemAtPosition(position);
                Log.i(selected,"selecteditem");
                Recipe temp = Recipe.toRecipe(selected);

                autocomplete = temp.getName()+";";
            for(int i=0;i<temp.getIngredients().size();i++){
                if(i==temp.getIngredients().size()-1){
                    autocomplete = autocomplete;
                }
                autocomplete = autocomplete + temp.getIngredients().get(i)+ "<";
            }
                Context context = v.getContext();
                Intent i = new Intent(context,MealAdder.class);
                i.putExtra("autocompletion",autocomplete);
            Log.i(autocomplete,"before sent to main, recipe autocmplet");
                context.startActivity(i);

        }
    }
}


