package gerber.apress.esctransition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Income extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> data1 = new ArrayList<String>();

    private TableLayout table;

    EditText intype, inprice, intotal;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        intype = findViewById(R.id.intype);
        inprice = findViewById(R.id.inprice);

        intotal = findViewById(R.id.intotal);

        btn1 = findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    public void add()
    {

        String inType = intype.getText().toString();
        int amount = Integer.parseInt(inprice.getText().toString());

        data.add(inType);
        data1.add(String.valueOf(amount));

        TableLayout table = (TableLayout) findViewById(R.id.tb1);

        TableRow row = new TableRow(this);
        TextView t1 = new TextView(this);
        TextView t2 = new TextView(this);

        int sum = 0;

        for (int i = 0; i< data.size(); i ++)
        {
            String itype = data.get(i);
            String iprice = data1.get(i);

            t1.setText(itype);
            t2.setText(iprice);

            sum = sum + Integer.parseInt(data1.get(i).toString());
        }

        row.addView(t1);
        row.addView(t2);
        table.addView(row);

        intotal.setText(String.valueOf(sum));
        intype.setText("");
        inprice.setText("");
        intype.requestFocus();
    }
    public void home (View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class)); //sends the user to the Main Activity Page
        finish();
    }
}
