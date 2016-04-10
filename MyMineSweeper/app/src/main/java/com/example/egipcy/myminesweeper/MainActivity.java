package com.example.egipcy.myminesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener
{
    Button btn_reset;
    Button btn_mode;

    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btn_reset = (Button) findViewById(R.id.btn_reset);
        this.btn_reset.setOnClickListener(this);

        this.btn_mode = (Button) findViewById(R.id.btn_mode);
        this.btn_mode.setOnClickListener(this);

        this.grid = (GridView)findViewById(R.id.gridView);

        Toast toast_win = Toast.makeText(getApplicationContext(), "YOU WIN!!!", Toast.LENGTH_SHORT);
        Toast toast_lose = Toast.makeText(getApplicationContext(), "YOU LOSE - Try again", Toast.LENGTH_SHORT);
        this.grid.set_toats(toast_win, toast_lose);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_reset:
                this.grid.reset();
                break;
        }
    }
}
