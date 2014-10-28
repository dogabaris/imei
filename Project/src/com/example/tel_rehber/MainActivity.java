package com.example.tel_rehber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int PICK_CONTACT_REQUEST = 0;
	Button btn_rehber_ac,btn_odemeli,btn_gizli,btn_listeye_gec;
	TextView tv_numara,tv_ad;
	String operator;
	
	Intent i_liste;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		i_liste = new Intent(this,ListeActivity.class);
		
		btn_rehber_ac = (Button)findViewById(R.id.button1);
		btn_odemeli = (Button)findViewById(R.id.odemeli_ara);
		btn_gizli = (Button)findViewById(R.id.gizli_ara);
		btn_listeye_gec = (Button)findViewById(R.id.listeye_gec);
		
		tv_numara = (TextView)findViewById(R.id.numara);
		tv_ad = (TextView)findViewById(R.id.ad);
		
		TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		operator = manager.getNetworkOperatorName();
		
		
		btn_rehber_ac.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RehberAc();
			}
		});

		btn_odemeli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OdemeliAra();
			}
		});
		
		btn_gizli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GizliAra();
			}
		});
		
		btn_listeye_gec.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(i_liste);
			}
		});
	}
	
	@SuppressLint("ShowToast")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // TODO Auto-generated method stub
	    super.onActivityResult(requestCode, resultCode, data);
	
	    if(requestCode == PICK_CONTACT_REQUEST){
	    	if(resultCode == RESULT_OK){
	    		
	    		// rehberden seçilen kiþinin Uri'sinden giderek veritabanbýnda o satýrý çekiyoruz.
	            Cursor cursor = getContentResolver().query(data.getData(),null,null,null,null);

	            // çektiðimiz satýrýn numara ve isim bilgisinin tutulduðu kolonlarý çekiyoruz.
	            String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
	            String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
	            
	            tv_numara.setText(number);
	            tv_ad.setText(name);
	            btn_odemeli.setEnabled(true);
	            btn_gizli.setEnabled(true);
	            btn_odemeli.setBackgroundResource(R.drawable.aktif);
	            btn_gizli.setBackgroundResource(R.drawable.aktif);
	    	}
	     }
	}
	
	void RehberAc(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		startActivityForResult(intent, PICK_CONTACT_REQUEST);
	}
	
	void OdemeliAra(){
		String phone = OdemeliKodu(tv_numara.getText().toString());
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
		startActivity(intent);
	}
	
	void GizliAra(){
		String phone = "#31#" +tv_numara.getText().toString();
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
		startActivity(intent);
	}
	
	String OdemeliKodu(String no){
		if(operator.equals("VODAFONE TR"))
			return "*149*"+no+"#";
		if(operator.equals("AVEA") || operator.equals("TURKCELL"))
			return "*135*"+no+"#";
		return null;
	}

}
