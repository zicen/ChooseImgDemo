package com.zhenquan.chooseimgdemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ChooseImageUtil mChooseImageUtil;
    public static final int CHOOSE_IMAGE_FROM_GALLRY = 100;
    public static final int CHOOSE_IMAGE_FROM_CAMERA = 200;
    public static final int CHOOSE_IMAGE_FROM_GALLRY2 = 300;
    public static final int CHOOSE_IMAGE_FROM_CAMERA2 = 400;
    public static final int CROP_SUCCESS = 10;
    private TextView mTxt_path;
    private String attachFilePath;
    private Uri mUri;
    private ImageView mImage;
    private Button mBtn_choose_image;
    private Button mBtn_choose_imageandCrop;
    private File path;
    private File profileIamge;
    //文件夹名称
    private String folderName = "ChooseImgDemo";
    //第一个拍照保存的文件名
    private String demoImage1 = "demo1.jpg";
    //第二个拍照保存的文件名
    private String demoImage2 = "demo2.jpg";
    //裁剪保存的文件名
    private String demoImage = "demo.jpg";
    private Button btn_third_part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooseImageUtil = new ChooseImageUtil(this);
        initView();
        initPath();
    }

    private void initView() {
        mBtn_choose_image = (Button) findViewById(R.id.btn_choose_image);
        mBtn_choose_imageandCrop = (Button) findViewById(R.id.btn_choose_imageandCrop);
        btn_third_part = (Button) findViewById(R.id.btn_third_part);
        mTxt_path = (TextView) findViewById(R.id.txt_path);
        mImage = (ImageView) findViewById(R.id.image);
        mBtn_choose_imageandCrop.setOnClickListener(this);
        mBtn_choose_image.setOnClickListener(this);
        btn_third_part.setOnClickListener(this);
    }

    private void initPath() {
        path = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + folderName + "/");
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*attach image*/
            case CHOOSE_IMAGE_FROM_GALLRY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    Cursor c = cr.query(uri, null, null, null, null);
                    c.moveToFirst();
                    attachFilePath = c.getString(c.getColumnIndex("_data"));
                    mTxt_path.setText(attachFilePath);
                }
                break;
            case CHOOSE_IMAGE_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    File temp = new File(path + File.separator + demoImage1);
                    attachFilePath = temp.getAbsolutePath();
                    mTxt_path.setText(attachFilePath);
                }
                break;

            //crop image
            case CHOOSE_IMAGE_FROM_GALLRY2:
                if (resultCode == RESULT_OK) {
                    mUri = data.getData();
                    mChooseImageUtil.cropPhoto(mUri, CROP_SUCCESS);
                }
                break;
            case CHOOSE_IMAGE_FROM_CAMERA2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(path + File.separator + demoImage2);
                    mUri = Uri.fromFile(temp);
                    mChooseImageUtil.cropPhoto(mUri, CROP_SUCCESS);
                }
                break;
            case CROP_SUCCESS:
                if (data != null) {
                    Bundle extras1 = data.getExtras();
                    Bitmap head = extras1.getParcelable("data");
                    if (head != null) {
                        profileIamge = new File(path + File.separator + demoImage);
                        Uri uri = mChooseImageUtil.storeCropImage(head, profileIamge);
                        mImage.setImageBitmap(head);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_image:
                mChooseImageUtil.showTypeDialog(CHOOSE_IMAGE_FROM_GALLRY, CHOOSE_IMAGE_FROM_CAMERA, demoImage1);
                break;
            case R.id.btn_choose_imageandCrop:
                mChooseImageUtil.showTypeDialog(CHOOSE_IMAGE_FROM_GALLRY2, CHOOSE_IMAGE_FROM_CAMERA2, demoImage2);
                break;
            case R.id.btn_third_part:
                break;
            default:
                break;

        }
    }
}
