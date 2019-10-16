package com.sonu.firebasedemo.storage;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sonu.firebasedemo.R;
import com.google.firebase.storage.FirebaseStorage;
import android.widget.ImageView;
import com.google.firebase.storage.StorageReference;
import android.widget.Button;
import android.view.View;
import android.content.*;
import javax.io.reactivex.functions.*;
import android.net.*;
import android.graphics.*;
import android.provider.*;
import java.io.*;
import android.widget.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.storage.*;

public class StorageActivity extends AppCompatActivity
implements View.OnClickListener{

	private final int CHOOSE_IMG=123;
	FirebaseStorage firebaseStorage;
	Uri imgUriLocal;
	ImageView imgView;
	Button btnUploadImg;
	ProgressBar pbImgUpload;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage);
		init();
		setOnClickListerns();
	}
	
	private void init(){
		firebaseStorage=FirebaseStorage.getInstance();
		imgView=findViewById(R.id.id_imgView);
		btnUploadImg=findViewById(R.id.id_btn_upload);
		pbImgUpload=findViewById(R.id.id_pb_uploadImg);
		
	}
	
	private void setOnClickListerns(){
		imgView.setOnClickListener(this);
		btnUploadImg.setOnClickListener(this);
	}
	
	private void uploadFileToFirebaseStorage(){
		pbImgUpload.setVisibility(View.VISIBLE);
		StorageReference storageRef=firebaseStorage.getReference();
		storageRef=storageRef.child("profilepics/myphoto.jpg");
		storageRef.putFile(imgUriLocal).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>(){

				@Override
				public void onComplete(Task<UploadTask.TaskSnapshot> p1)
				{
					pbImgUpload.setVisibility(View.GONE);
					if(p1.isSuccessful()){
						showInToast("Image uploaded successfull");
					}else{
						showInToast(p1.getException().toString());
					}
				}
				
			
		});
		
	}
	
	
	

	@Override
	public void onClick(View p1)
	{
		switch(p1.getId()){
			case R.id.id_imgView:
				imgChooser();
				break;
			case R.id.id_btn_upload:
				 uploadFileToFirebaseStorage();
				break;
		}
	}

	private void imgChooser(){
		Intent intent=new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,
													"Choose a picture to upload"),CHOOSE_IMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==CHOOSE_IMG && resultCode==RESULT_OK && data !=null 
		&& data.getData() != null){
			imgUriLocal=data.getData();
			
		 try{
			 Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),
															 imgUriLocal);
			 imgView.setImageBitmap(bitmap);
		 }catch (IOException e) {
			 e.printStackTrace();
		 }

		}else{
			showInToast("Image Not found");
		}
	}
	
	private void showInToast(String msg){
		Toast.makeText(StorageActivity.this,msg,Toast.LENGTH_SHORT).
		show();
	}
	
	
	
	
}
