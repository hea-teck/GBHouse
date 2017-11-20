package com.example.gbhouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 김희택 on 2017-11-16.
 */

public class RestaurantRegistrationActivity extends AppCompatActivity {

    final static String TAG="GBHouse";

    EditText mName;
    EditText mAddress;
    EditText mPhone;
    ImageButton mPicture;

    private DBHelper1 mDbHelper1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);

        mName = (EditText) findViewById(R.id.edit_name);
        mAddress = (EditText) findViewById(R.id.edit_address);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        mPicture = (ImageButton) findViewById(R.id.imageButton3);

        mDbHelper1 = new DBHelper1(this);

        ImageButton btn = (ImageButton) findViewById(R.id.imageButton3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button button = (Button) findViewById(R.id.veiwall);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                startActivity(intent);//버튼 클릭시 다음 엑티비티로 넘어가면서 DB에서 자료 읽어와야됨
                insertRecord2(); //DB에 자료 저장완료
            }
        });
    }

    private void insertRecord2() {                          //레코드 추가 삽입 가능
        EditText name = (EditText) findViewById(R.id.edit_name);
        EditText address = (EditText) findViewById(R.id.edit_address);
        EditText phone = (EditText) findViewById(R.id.edit_phone);

        long nOfRows = mDbHelper1.insertUserByMethod1(name.getText().toString(), address.getText().toString(), phone.getText().toString());   //이미지 받아오는것 확인 / 수정!!!
        if (nOfRows > 0)
            Toast.makeText(this, "맛집이 등록되었습니다.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "다시삽입해주세요.", Toast.LENGTH_SHORT).show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

                ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
                imageButton3.setImageURI(Uri.fromFile(mPhotoFile));
            }
        }
    }

    String mPhotoFileName;
    File mPhotoFile;

    final int REQUEST_IMAGE_CAPTURE = 100;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.gbhouse", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
}