package com.ayhanunal.apijsondovizcevirici;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cadText;
    TextView chfText;
    TextView usdText;
    TextView jpyText;
    TextView tryText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //MANIFEST E IZINLERI KOYMAMIZ GEREKIYOR. INTERNET IZNI
        //manifeste android:networkSecurityConfig="@xml/network_security_config" bu satiri ekle ve
        //res altinda xml dosyası olustur ve traffic permitted true yap.

        //eger hala olmazsa uygulamayi emülatorden kaldırıp tekrar yükle.

        cadText = findViewById(R.id.cadText);
        chfText = findViewById(R.id.chfText);
        usdText = findViewById(R.id.usdText);
        jpyText = findViewById(R.id.jpyText);
        tryText = findViewById(R.id.tryText);




    }

    public void getRates(View view){

        DownloadData downloadData = new DownloadData();

        try {

            String url = "http://data.fixer.io/api/latest?access_key=9ca193445654eb584d7ef0075d5138a8&format=1";

            downloadData.execute(url); //url yi ordan manuel girmekten se burdan doinbackgrounda yolladik.


        }catch (Exception e){

        }

    }


    private class DownloadData extends AsyncTask<String,Void,String>{

        //String parametre : verilen url adresi (verinin geleceği yer)
        //Progres dediği progress bar yani verinin indirilirken ilerleme durumu bizim ki küçuk oldugu için void diyoruz
        //3.parametre de string yani cevap gelen veriyi de string olarak alıcaz.

        //asyn demek senkronize olmayan demek , indirme işi gibi durumlar appi kitleyebileceğii için
        //async olarak kullanmamız gerekiyor.


        @Override
        protected String doInBackground(String... strings) {

            String sonuc = ""; //tüm alıcagımız veriler.
            URL url; //verileri alıcagımız url adresi
            HttpURLConnection httpURLConnection; //güvenli site olsaydi https olucakti

            //hata verme olasiligi yuksek oldugu icin try catch blogu aciyoruz.
            try {

                url = new URL(strings[0]); //direkt url adresini de verebilirdik ama sistematik olsun diye strings dizisinden aldik
                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0){
                    //0dan buyuk oldugu surece hala data var demek.

                    //veriyi karakter karakter olarak alicaz.
                    char character = (char) data;
                    sonuc += character;

                    data = inputStreamReader.read(); //bir sonraki karaktere gec.

                }

                return sonuc; //hata yoksa inen veriyi return edicez.

            }catch (Exception e){
                return e.toString(); // hata olursa null donduruyoruz.

            }





        }

        @Override
        protected void onPostExecute(String s) {
            //işlem bittikten sonra ne olacak.
            //yani gelen veriyi string olarak vericek.
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);

                String rates = jsonObject.getString("rates");
                JSONObject ratesObject = new JSONObject(rates);

                cadText.setText("CAD :"+ratesObject.getString("CAD"));
                chfText.setText("CHF :"+ratesObject.getString("CHF"));
                usdText.setText("USD :"+ratesObject.getString("USD"));
                jpyText.setText("JPY :"+ratesObject.getString("JPY"));
                tryText.setText("TRY :"+ratesObject.getString("TRY"));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




}