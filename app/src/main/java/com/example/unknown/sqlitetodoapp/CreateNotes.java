package com.example.unknown.sqlitetodoapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.unknown.sqlitetodoapp.Helper.SQLitedataBaseHelperClass;
import com.example.unknown.sqlitetodoapp.Helper.Tag;
import com.example.unknown.sqlitetodoapp.Helper.ToDo;

import java.util.ArrayList;
import java.util.List;

public class CreateNotes extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText tag,note;
    Button save;
    Spinner spinner;
    SQLitedataBaseHelperClass helperClass ;
    ArrayAdapter adapter;

    Tag tagClass;
    ToDo todoClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        helperClass =  new SQLitedataBaseHelperClass(getApplicationContext());

        tag = (EditText)findViewById(R.id.tag);
        note= (EditText)findViewById(R.id.note);

        save = (Button)findViewById(R.id.saveINSERT);

        spinner = (Spinner)findViewById(R.id.spinner);

        initialize();

        save.setOnClickListener(this);

    }

    public void initialize(){

        int NumOfToDo = helperClass.getToDoCount();
        List<Tag> NumOfTag = helperClass.getAllTags();
        if(NumOfToDo > 0 && NumOfTag != null ){
            List<Tag> tagList = helperClass.getAllTags();

            List<String> list = new ArrayList<>();

            for(Tag tag : tagList){
                list.add(tag.getTag());
            }

            adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }
        else {
            return;
        }
    }

    @Override
    public void onClick(View v) {

        if(tag.getText().length() > 0 && note.getText().length() > 0){

           tagClass = new Tag(tag.getText().toString());
           todoClass = new ToDo(note.getText().toString(),0);

            long tag_id = helperClass.createTag(tagClass);

            long todo_id = helperClass.createToDo(todoClass ,new long [] {tag_id});

            long todo_tag_id = helperClass.createToDoTag(todo_id , tag_id);

            Toast.makeText(this,"Note Created",Toast.LENGTH_LONG).show();

        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Enter Both values to save Note");
            builder.setTitle("Information");
            builder.setPositiveButton("OK",null);
            builder.show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tag.setText(spinner.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
