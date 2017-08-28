package com.example.unknown.sqlitetodoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.unknown.sqlitetodoapp.Helper.ToDo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button create,view,update,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button)findViewById(R.id.button);
        view = (Button)findViewById(R.id.button2);
        update = (Button)findViewById(R.id.button3);
        delete = (Button)findViewById(R.id.button4);

        create.setOnClickListener(this);
        view.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){

            case R.id.button:
               intent = new Intent(getApplicationContext(),CreateNotes.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Toast.makeText(this,"Not Implemented",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                Toast.makeText(this,"Not Implemented",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4:
                intent = new Intent(getApplicationContext(),DeleteNotes.class);
                startActivity(intent);
                break;

        }

    }
}
