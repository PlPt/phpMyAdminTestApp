package de.plpt.phpmyadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import de.plpt.MaterialAdapter.MaterialView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //region init
        MaterialView materialView = new MaterialView(this);//Init MaterialView
        materialView.getToolbar().setLogo(R.mipmap.ic_launcher);
        Button b = (Button)findViewById(R.id.button);
        Ion.getDefault(this).configure().setLogging("ION", Log.DEBUG);//set ION Debug
        //endregion
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiRequest.newInstance(MainActivity.this).getDatabases(new ApiRequest.ApiResponse() {
                    @Override
                    public void onFinish(Exception x, ApiFrame<?> frame) {
                        if(x!=null)
                        {
                            x.printStackTrace(); //Error
                            Toast.makeText(MainActivity.this,x.toString(),Toast.LENGTH_LONG).show();
                            return;
                        }
                        ApiFrame<DataTable> tblFrame = (ApiFrame<DataTable>)frame;

                        if(!tblFrame.hasErrorData())
                        {
                            DataTable tbl = tblFrame.getData();
                            TextView tv = (TextView)findViewById(R.id.textView2);
                            final List<String> dbs = new ArrayList<String>();
                            ListView lv = (ListView)findViewById(R.id.listView);


                            for(List<String> s : tbl)
                            {
                                dbs.add(s.get(0));

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,dbs);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    requestTables(dbs.get(position));
                                }
                            });



                        }
                        else
                        {
                            //handle frame error
                            Toast.makeText(MainActivity.this,"ERROR: " + tblFrame.getErrorData().toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });

    }


    public void requestTables(String Database)
    {
        ApiRequest.newInstance(MainActivity.this).getTables(Database,new ApiRequest.ApiResponse() {
            @Override
            public void onFinish(Exception x, ApiFrame<?> frame) {
                if(x!=null)
                {
                    x.printStackTrace(); //Error on network
                    return;
                }
                ApiFrame<DataTable> tblFrame = (ApiFrame<DataTable>)frame;

                if(!tblFrame.hasErrorData())
                {
                    DataTable tbl = tblFrame.getData();
                    TextView tv = (TextView)findViewById(R.id.textView2);
                    List<String> dbs = new ArrayList<String>();
                    ListView lv = (ListView)findViewById(R.id.listView);


                    for(List<String> s : tbl)
                    {
                        dbs.add(s.get(0));

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,dbs);
                    lv.setAdapter(adapter);
                lv.setOnItemClickListener(null);



                }
                else
                {
                    //handle frame error
                    Toast.makeText(MainActivity.this,tblFrame.getErrorData().toString(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }


}
