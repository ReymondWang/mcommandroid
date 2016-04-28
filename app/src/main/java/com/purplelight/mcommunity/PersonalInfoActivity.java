package com.purplelight.mcommunity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.purplelight.mcommunity.application.MCommApplication;
import com.purplelight.mcommunity.component.view.CircleImageView;
import com.purplelight.mcommunity.component.view.ImageModeDialog;
import com.purplelight.mcommunity.component.widget.HeadBar;
import com.purplelight.mcommunity.constant.WebAPI;
import com.purplelight.mcommunity.provider.DomainFactory;
import com.purplelight.mcommunity.provider.dao.ILoginInfoDao;
import com.purplelight.mcommunity.provider.entity.LoginInfo;
import com.purplelight.mcommunity.task.BitmapDownloaderTask;
import com.purplelight.mcommunity.task.DownloadedDrawable;
import com.purplelight.mcommunity.util.HttpUtil;
import com.purplelight.mcommunity.util.ImageHelper;
import com.purplelight.mcommunity.util.Validation;
import com.purplelight.mcommunity.web.result.Result;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 个人信息界面
 * Created by wangyn on 16/4/27.
 */
public class PersonalInfoActivity extends BaseActivity {
    private static final String TAG = "PersonalInfoActivity";

    /**
     * 头像图片类型
     */
    private final class HeadImgType{
        public static final int CAMERA = 1;
        public static final int PHOTO = 2;
        public static final int CROP = 3;
    }

    @InjectView(R.id.headBar) HeadBar headBar;
    @InjectView(R.id.imgHeadImage) CircleImageView imgHeadImage;
    @InjectView(R.id.txtUserCode) TextView txtUserCode;
    @InjectView(R.id.txtUserName) TextView txtUserName;
    @InjectView(R.id.txtUserSex) TextView txtUserSex;
    @InjectView(R.id.txtUserEmail) TextView txtUserEmail;
    @InjectView(R.id.txtUserPhone) TextView txtUserPhone;
    @InjectView(R.id.lytHeadImg) LinearLayout lytHeadImg;
    @InjectView(R.id.lytUserName) LinearLayout lytUserName;
    @InjectView(R.id.lytUserSex) LinearLayout lytUserSex;
    @InjectView(R.id.lytUserEmail) LinearLayout lytUserEmail;
    @InjectView(R.id.lytUserPhone) LinearLayout lytUserPhone;

    // 截取图片的大小
    private int mHeadImageSize = 100;
    // 调用相机拍照时，临时存储文件。
    private Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.inject(this);

        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    /**
     * 接受调用相机和画册后，回调的数据。
     * @param requestCode     请求的任务编号  HeadImgType.CAMERA ／ HeadImgType.PHOTO
     * @param resultCode      结果编号       是否调用成功
     * @param data            回传数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            Bitmap bitmap;
            switch(requestCode){
                case HeadImgType.CAMERA: {
                    sendImageToCrop(imageUri);
                    break;
                }
                case HeadImgType.PHOTO: {
                    // 由于从系统图库中取图片时，如果图片大小小于设定值，则从data中直接返回。
                    // 否则使用外部输出文件来返回，因为不能判断用户选择的文件大小。
                    // 因此首先从data中取数据，如果取不到则从外部存储文件中取，用来保证获得用户选择的内容。
                    bitmap = decodeUriAsBitmap(data.getData());
                    if (bitmap == null){
                        bitmap = decodeUriAsBitmap(imageUri);
                    }
                    if(bitmap != null){
                        bitmap = ImageHelper.CompressImageToSize(bitmap, mHeadImageSize, mHeadImageSize);
                        imgHeadImage.setImageBitmap(bitmap);
                        UpdateImageTask task = new UpdateImageTask();
                        task.execute(bitmap);
                    }
                    break;
                }
                case HeadImgType.CROP: {
                    bitmap = decodeUriAsBitmap(imageUri);
                    if(bitmap != null){
                        bitmap = ImageHelper.CompressImageToSize(bitmap, mHeadImageSize, mHeadImageSize);
                        imgHeadImage.setImageBitmap(bitmap);
                        UpdateImageTask task = new UpdateImageTask();
                        task.execute(bitmap);
                    }
                    break;
                }
            }
        }
    }

    private void initView(){
        LoginInfo loginInfo = MCommApplication.getLoginInfo();
        if (loginInfo != null){
            BitmapDownloaderTask task = new BitmapDownloaderTask(imgHeadImage);
            DownloadedDrawable drawable = new DownloadedDrawable(task, getResources(), R.drawable.default_head_image);
            imgHeadImage.setImageDrawable(drawable);
            task.execute(WebAPI.getFullImagePath(loginInfo.getHeadImgPath()));

            txtUserCode.setText(loginInfo.getUserCode());
            txtUserName.setText(loginInfo.getUserName());
            if ("1".equals(loginInfo.getSex())){
                txtUserSex.setText("男");
            } else {
                txtUserSex.setText("女");
            }
            txtUserEmail.setText(loginInfo.getEmail());
            txtUserPhone.setText(loginInfo.getPhone());
        }
    }

    private void initEvents(){
        headBar.setLeftHeadButtonEvent(new HeadBar.HeadButtonClickedListener() {
            @Override
            public void onHeadButtonClicked() {
                PersonalInfoActivity.this.finish();
            }
        });
        lytUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("type", ModifyUserInfoActivity.USER_NAME);
                startActivity(intent);
            }
        });
        lytUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("type", ModifyUserInfoActivity.SEX);
                startActivity(intent);
            }
        });
        lytUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("type", ModifyUserInfoActivity.EMAIL);
                startActivity(intent);
            }
        });
        lytUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("type", ModifyUserInfoActivity.PHONE);
                startActivity(intent);
            }
        });
        lytHeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageModeDialog();
            }
        });
    }

    private void showImageModeDialog(){
        final ImageModeDialog dialog = new ImageModeDialog(this);
        dialog.setCameraListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, HeadImgType.CAMERA);
            }
        });
        dialog.setPhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("outputX", mHeadImageSize);
                intent.putExtra("outputY", mHeadImageSize);
                intent.putExtra("scale", true);

                // 如果文件大小小于设定的mHeadImageSize,则直接从data中返回
                intent.putExtra("return-data", true);
                // 如果文件大小大于设定的mHeadImageSize,则通过imageUri返回
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, HeadImgType.PHOTO);
            }
        });
        dialog.show();
    }

    /**
     * 将URI的内容转换为Bitmap
     * @param uri    资源定位器
     * @return       Bitmap
     */
    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }

    /**
     * 图片裁剪
     * @param uri   带裁剪的图片Uri
     */
    private void sendImageToCrop(Uri uri){
        if (uri == null) return;

        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mHeadImageSize);
        intent.putExtra("outputY", mHeadImageSize);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, HeadImgType.CROP);
    }

    private class UpdateImageTask extends AsyncTask<Bitmap, Void, Result> {
        @Override
        protected Result  doInBackground(Bitmap... params) {
            Result result = new Result();
            if (Validation.IsActivityNetWork(PersonalInfoActivity.this)){
                try {
                    String fileName = ImageHelper.upload(params[0]);
                    LoginInfo loginInfo = MCommApplication.getLoginInfo();
                    loginInfo.setHeadImgPath(fileName);
                    MCommApplication.setLoginInfo(loginInfo);

                    ILoginInfoDao loginInfoDao = DomainFactory.createLoginInfo(PersonalInfoActivity.this);
                    loginInfoDao.save(loginInfo);

                } catch (Exception ex){
                    result.setSuccess(Result.ERROR);
                    result.setMessage(ex.getMessage());
                    Log.e(TAG, ex.getMessage());
                }
            } else {
                result.setSuccess(Result.ERROR);
                result.setMessage(getString(R.string.do_not_have_network));
            }

            return result;
        }

        @Override
        protected void onPostExecute(Result s) {
            if (Result.SUCCESS.equals(s.getSuccess())){
                UpdateUserInfoTask task = new UpdateUserInfoTask();
                task.execute();
            } else {
                Toast.makeText(PersonalInfoActivity.this, s.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateUserInfoTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            LoginInfo loginInfo = MCommApplication.getLoginInfo();
            LoginInfo updateInfo = new LoginInfo();
            updateInfo.setId(loginInfo.getId());
            updateInfo.setHeadImgPath(loginInfo.getHeadImgPath());
            String json = new Gson().toJson(updateInfo);

            try{
                HttpUtil.PostJosn(WebAPI.getWebAPI(WebAPI.UPDATE_USER), json);
            } catch (IOException ex){
                Log.e(TAG, ex.getMessage());
            }

            return null;
        }
    }
}
