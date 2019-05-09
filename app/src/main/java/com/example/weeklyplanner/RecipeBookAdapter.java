package com.example.weeklyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class RecipeBookAdapter  extends BaseAdapter {
    ArrayList<String> myRecipiesString;
    ArrayList<Recipe> myRecipies;
    LayoutInflater mInflater;
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
        delete_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String name = myRecipies.get(position).getName();
        recipeTextView.setText(name);
        return v;
    }
}


