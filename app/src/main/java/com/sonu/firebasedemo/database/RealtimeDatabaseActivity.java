package com.sonu.firebasedemo.database;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sonu.firebasedemo.R;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.sonu.firebasedemo.models.Word;
import com.google.firebase.database.*;
import com.google.android.gms.tasks.*;
import android.widget.*;
import android.view.View;
import java.util.*;

public class RealtimeDatabaseActivity extends AppCompatActivity
implements View.OnClickListener{
		
		EditText edtWord,edtMeaning,edtDesc;
		Button btnSave,btnGet;
		TextView tvWordData;
		ProgressBar pdSave;
		FirebaseAuth mAuth;
	  DatabaseReference mDbRef;
		List<Word> wordList;

		@Override
		protected void onCreate(Bundle savedInstanceState){
				// TODO: Implement this method
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_realtime_database);
				
				//init
				init();
				
				mAuth=FirebaseAuth.getInstance();
				mDbRef=FirebaseDatabase.getInstance().getReference("test/"+mAuth.getCurrentUser()
				.getUid());
				btnSave.setOnClickListener(this);
				btnGet.setOnClickListener(this);
				
				readData();
				
				
		}
		
		private void init(){
				wordList=new ArrayList();
				edtDesc=findViewById(R.id.id_edtv_desc);
				edtWord=findViewById(R.id.id_edtv_word);
				edtMeaning=findViewById(R.id.id_edtv_meaning);
				pdSave=findViewById(R.id.id_pb_save);
				btnSave=findViewById(R.id.id_btn_save);
				btnGet=findViewById(R.id.id_btn_get);
				tvWordData=findViewById(R.id.id_tv_wordData);
				
		}

		@Override
		public void onClick(View p1)
		{
				switch(p1.getId()){
						case R.id.id_btn_save:
								if(edtWord.getText().toString().isEmpty() || 
								edtMeaning.getText().toString().isEmpty()){
										edtWord.setError("Field could not be empty");
								}else{
										uploadData(new Word(edtWord.getText().toString(),edtMeaning.getText().toString()
										,edtDesc.getText().toString()));
								}
								break;
						case R.id.id_btn_get:
								readSingleData();
								break;
				}
		}

		
		
		private void uploadData(Word word){
				pdSave.setVisibility(View.VISIBLE);
				DatabaseReference dbRefWord=mDbRef.child("words");
				dbRefWord.child(dbRefWord.push().getKey()).setValue(new Word(edtWord.getText().toString(),
				edtMeaning.getText().toString(),edtDesc.getText().toString())).
						addOnCompleteListener(new OnCompleteListener<Void>(){

								@Override
								public void onComplete(Task<Void> p1)
								{
										if(p1.isSuccessful()){
												toast("data saved");
										}else{
												toast(p1.getException().toString());
										}
										pdSave.setVisibility(View.GONE);
								}
								
						
				});
				
		}
		
		private void readData(){
				pdSave.setVisibility(View.VISIBLE);
				mDbRef.child("words").addValueEventListener(new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot p1)
								{
										StringBuilder data=new StringBuilder();
										for(DataSnapshot ds : p1.getChildren()){
												String wordID=ds.getKey();
												Word word=ds.getValue(Word.class);
		                    wordList.add(new Word(ds.getKey(),word.getWord(),word.getMeaning(),
												word.getDesc()));
												
										}
										toast("Data read successfully");
										pdSave.setVisibility(View.GONE);
								}

								@Override
								public void onCancelled(DatabaseError p1)
								{
										// TODO: Implement this method
										toast(p1.getMessage().toString());
										pdSave.setVisibility(View.GONE);
								}
								
						
				});
		}
		private void readSingleData(){
				pdSave.setVisibility(View.VISIBLE);
				mDbRef.child("words").child(wordList.get(1).getId()).
				addListenerForSingleValueEvent((new ValueEventListener() {

								@Override
								public void onDataChange(DataSnapshot p1)
								{
														Word word=p1.getValue(Word.class);
														String data =word.getWord()+"\n"+
														word.getMeaning()+"\n"+word.getDesc();
														tvWordData.setText(data);
										pdSave.setVisibility(View.GONE);
								}

								@Override
								public void onCancelled(DatabaseError p1)
								{
										// TODO: Implement this method
										toast(p1.getMessage());
										pdSave.setVisibility(View.GONE);
								}
								
						
				}));
		}
		private void toast(String msg){
				Toast.makeText(RealtimeDatabaseActivity.this,msg,Toast.LENGTH_LONG)
				.show();
		}
		
}
