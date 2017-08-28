package com.example.unknown.sqlitetodoapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.unknown.sqlitetodoapp.Helper.SQLitedataBaseHelperClass;
import com.example.unknown.sqlitetodoapp.Helper.Tag;
import com.example.unknown.sqlitetodoapp.Helper.ToDo;

import java.util.ArrayList;
import java.util.List;

public class DeleteNotes extends AppCompatActivity implements View.OnClickListener {

    SQLitedataBaseHelperClass helperClass;

    Spinner tagSpinner,todoSpinner;
    Button tagBtn,todoBtn,ttBtn;
    List<Tag> tagList;
    List<ToDo> todoList;
    ArrayAdapter adapter,adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notes);

        helperClass = new SQLitedataBaseHelperClass(this);

        tagSpinner = (Spinner) findViewById(R.id.spinner_del_tag);
        todoSpinner = (Spinner)findViewById(R.id.spinner_del_todo);

        tagBtn = (Button)findViewById(R.id.del_tag);
        todoBtn = (Button)findViewById(R.id.del_note);
        ttBtn = (Button)findViewById(R.id.del_tag_note);

        tagBtn.setOnClickListener(this);
        todoBtn.setOnClickListener(this);
        ttBtn.setOnClickListener(this);

        initialize();

    }

    public void initialize (){

        int numOfTodos = helperClass.getToDoCount();
        List<Tag> numOfTags = helperClass.getAllTags();
        if(numOfTodos > 0 && numOfTags != null){

            tagList = helperClass.getAllTags();
            todoList= helperClass.getAllToDos();

            List<String> listTODO = new ArrayList<>();
            List<String> listTAG = new ArrayList<>();

            for(Tag tag : tagList){
                listTAG.add(tag.getTag());
            }

            for(ToDo todo : todoList){
                listTODO.add(todo.getNote());
            }

            adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listTAG);
            tagSpinner.setAdapter(adapter);

            adapter = null;

            adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listTODO);
            todoSpinner.setAdapter(adapter);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No any message to delete");
            builder.setTitle("Information");
            builder.setPositiveButton("OK",null);
            builder.show();
            tagBtn.setEnabled(false);
            todoBtn.setEnabled(false);
            ttBtn.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.del_note:

                ToDo toDo = helperClass.getToDoId(todoSpinner.getSelectedItem().toString());
                int deletedRows = helperClass.deleteToDo(toDo);

                Toast.makeText(this,deletedRows + " row deleted",Toast.LENGTH_SHORT).show();
                break;

            case R.id.del_tag:

                break;

            case R.id.del_tag_note:

                boolean should_delete_tag_and_todo;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to delete all notes containig " + tagSpinner.getSelectedItem().toString()
                        + "tag as well" );
                builder.setTitle("Waring!!");
                builder.setCancelable(true);
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tag tag = helperClass.getTagId(tagSpinner.getSelectedItem().toString());
                        int deletedTag = helperClass.deleteTag(tag,true);
                        Toast.makeText(DeleteNotes.this,"Tag and Notes deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tag tag = helperClass.getTagId(tagSpinner.getSelectedItem().toString());
                        int deletedTag = helperClass.deleteTag(tag,false);
                        Toast.makeText(DeleteNotes.this,"Tag deleted",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }

    }
}
