package ykk.httpurlconnection.com.httpurlconnection;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private TextView textView;
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String data= (String) msg.obj;
            textView.setText(data);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button_Id);
        textView= (TextView) findViewById(R.id.textView_Id);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NetThread thread=new NetThread();
        thread.start();
    }

    class NetThread extends Thread
    {
        @Override
        public void run() {
            HttpURLConnection connection=null;

            try {
                URL url=new URL("http://www.baidu.com");
                connection=(HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream inputStream=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder=new StringBuilder();
                String line;
                while((line=reader.readLine())!=null)
                {
                    builder.append(line);
                }
                Message message=new Message();
                message.obj=builder.toString();
                handler.sendMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null)
                {
                    connection.disconnect();
                }
            }
        }
    }
}
