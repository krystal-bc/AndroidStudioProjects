package com.example.admin.justjava;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView numOrdersTextView;
    private TextView costTextView;
    private TextView asterisk;
    private TextView nameError;
    private EditText fullName;
    private CheckBox checkBox1;
    private CheckBox checkBox2;

    private int numCoffees = 1;
    private final double costSingleCoffee = 2.75;
    private final double costTopping = 0.25;
    private double totalCost = 2.75;

    private String name = "";
    private boolean whippedCream = false;
    private boolean chocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numOrdersTextView = (TextView) findViewById(R.id.numOrders_textView);
        costTextView = (TextView) findViewById(R.id.cost_textView);
        asterisk = (TextView) findViewById(R.id.asterisk_textView);
        nameError = (TextView) findViewById(R.id.nameError_textView);
        fullName = (EditText) findViewById(R.id.name_editText);
        checkBox1 = (CheckBox) findViewById(R.id.whippedCream_checkBox);
        checkBox2 = (CheckBox) findViewById(R.id.chocolate_checkBox);

        checkBox1.setChecked(false);
        checkBox2.setChecked(false);

        asterisk.setVisibility(View.INVISIBLE);
        nameError.setVisibility(View.INVISIBLE);



    }

    public void increment(View view){
        if(numCoffees <= 100){
            numCoffees++;
            totalCost += costSingleCoffee;
        }
        numOrdersTextView.setText(Integer.toString(numCoffees));
        updateCost(totalCost);
    }

    public void decrement(View view){
        if(numCoffees > 1){
            numCoffees--;
            totalCost -= costSingleCoffee;
        }
        numOrdersTextView.setText(Integer.toString(numCoffees));
        updateCost(totalCost);
    }

    public void emailOrder(View view){
        name = fullName.getText().toString();
        Resources res = getResources();
        String emailSubject = String.format(res.getString(R.string.email_subject), name);
        String orderSummary = String.format(res.getString(R.string.order_summary),
                "\n"+name+"\n", numCoffees, generateToppings(), updateCost(totalCost));
        if (name.equals("")){
            asterisk.setVisibility(View.VISIBLE);
            nameError.setVisibility(View.VISIBLE);
        }else {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    private String generateToppings(){
        Resources res = getResources();
        String toppings = "\n"+res.getString(R.string.toppings_included);
        if(whippedCream||chocolate){
            if(whippedCream){
                toppings += "\n-"+res.getString(R.string.whipped_cream);
            }if(chocolate){
                toppings += "\n-"+res.getString(R.string.chocolate);
            }
            toppings += "\n";
        }else{
            toppings += res.getString(R.string.no_toppings)+"\n";
        }
        return toppings;
    }

    private String updateCost(double num){
        String formatted = String.format("$%.2f", num);
        costTextView.setText(formatted);
        return formatted;
    }

    public void includeWhippedCream (View view){
        whippedCream = checkBox1.isChecked();
        if(whippedCream)
            totalCost += costTopping;
        else
            totalCost -= costTopping;
        updateCost(totalCost);
    }

    public void includeChocolate (View view){
        chocolate = checkBox2.isChecked();
        if(chocolate)
            totalCost += costTopping;
        else
            totalCost -= costTopping;
        updateCost(totalCost);
    }

}
