package com.ep.linkedlist.view.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ep.linkedlist.R;
import com.ep.linkedlist.bo.auth.AuthBO;
import com.ep.linkedlist.bo.profile.ProfileBO;
import com.ep.linkedlist.chat.util.Util;
import com.ep.linkedlist.view.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jiwon on 2016-09-19.
 */
@EFragment(R.layout.activity_profile)
public class ProfileFragment extends Fragment {
    private static final String tag = ProfileFragment.class.getName();
    private static final int IMAGE_GALLERY_REQUEST = 1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private AuthBO authBO;
    public ProgressDialog mProgressDialog;
    @ViewById
    ImageView profile_user_photo_background;
    @ViewById
    ImageView profile_user_photo_background2;
    @ViewById
    CircleImageView profile_circleView_userPhoto;
    @ViewById
    TextView profile_TextView_UserNickName;

    @AfterViews
    void afterViews() {
            if (StringUtils.isNotBlank(ProfileBO.getMyProfile().getPhotoURI())) {
                Glide.with(getContext())
                        .load(ProfileBO.getMyProfile().getPhotoURI())
                        .into(profile_circleView_userPhoto);

                ///Glide.with(getContext())
                        ///.load(ProfileBO.getMyProfile().getPhotoURI())
                        ///.into(profile_user_photo_background);

            }
            else {
                profile_circleView_userPhoto.setImageResource(R.drawable.profile_default_image);
                profile_user_photo_background.setImageResource(R.drawable.profile_default_bg);
            }
        profile_user_photo_background2.setAlpha(0.85f);
        profile_TextView_UserNickName.setText(ProfileBO.getMyProfile().getNickname());

    }
    @Override
    public void onStop(){
        super.onStop();
        authBO.destory();
    }
    @Override
    public void onResume() {
        super.onResume();
        profile_TextView_UserNickName.setText(ProfileBO.getMyProfile().getNickname());
        authBO = new AuthBO(getActivity(), authStateListener);
        authBO.init();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    AuthBO.AuthStateListener authStateListener = new AuthBO.AuthStateListener() {

        @Override
        public void onLogin(FirebaseUser firebaseUser) {

        }
        @Override
        public void onLogout() {
            hideProgressDialog();
            Intent intent = null;
            intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    //사진 등록하기 메소드
    void registPhoto()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }
    //로그아웃 메소드
    void DoLogout()
    {
        showProgressDialog();

        authBO.logout();
    }
    void modifyProfie()
    {
        Intent intent = null;
        intent = new Intent(getActivity(), ModifyProfileActivity.class);
        startActivity(intent);
    }
    /**
     * Envia o arvquivo para o firebase
     */
    private void sendFileFirebase(StorageReference storageReference, final byte[] bytes) {
        if (storageReference != null) {
            final String name = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference imageGalleryRef = storageReference.child(name);
            UploadTask uploadTask = imageGalleryRef.putBytes(bytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(tag, "onFailure sendFileFirebase " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(tag, "onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    ProfileBO.getMyProfile().setPhotoURI(downloadUrl.toString());
                    ProfileBO profileBO = new ProfileBO();
                    profileBO.updateProfile(ProfileBO.getMyProfile()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()==false){
                                Log.d(tag, "업데이트 실패.");
                                return;
                            }
                            if (StringUtils.isNotBlank(ProfileBO.getMyProfile().getPhotoURI())) {
                                Glide.with(getContext())
                                        .load(ProfileBO.getMyProfile().getPhotoURI())
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                hideProgressDialog();
                                                return false;
                                            }
                                        })
                                        .into(profile_circleView_userPhoto);
                                //Glide.with(getContext())
                                        //.load(ProfileBO.getMyProfile().getPhotoURI())
                                        //.into(profile_user_photo_background);
                            }
                            else {
                                profile_circleView_userPhoto.setImageResource(R.drawable.profile_default_image);
                                profile_user_photo_background.setImageResource(R.drawable.profile_default_bg);
                            }

                        }
                    });
                }
            });
        } else {
            //IS NULL
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        StorageReference storageRef = storage.getReferenceFromUrl(Util.URL_STORAGE_REFERENCE).child(Util.FOLDER_STORAGE_IMG);
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    showProgressDialog();
                    try {
                        sendFileFirebase(storageRef, rotateImageIfRequired(selectedImageUri, 800));
                    } catch (IOException e) {
                        hideProgressDialog();
                        Toast.makeText(getContext(), "이미지 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    } catch (IllegalStateException e) {
                        hideProgressDialog();
                        Toast.makeText(getContext(), "이미지 사이즈가 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //URI IS NULL
                }
            }
        }
    }

    public byte[] rotateImageIfRequired(Uri uri, int requiredSize) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
        int scaledWidth = bitmap.getWidth(), scaledHeight = bitmap.getHeight();
        while (true) {
            if (scaledWidth < requiredSize || scaledHeight < requiredSize) {
                break;
            }
            scaledWidth /= 2;
            scaledHeight /= 2;
        }

        ByteArrayOutputStream compressOutputStream = new ByteArrayOutputStream(1024 * 1024 * 5);
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream(1024 * 1024 * 5);
        bitmap = bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, compressOutputStream);
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(compressOutputStream.toByteArray(), 0, compressOutputStream.size());
        compressedBitmap = rotateImageIfRequired(compressedBitmap, uri);
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, resultOutputStream);
        byte[] result = resultOutputStream.toByteArray();
        IOUtils.closeQuietly(compressOutputStream);
        IOUtils.closeQuietly(resultOutputStream);
        return result;
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = getContext().getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    //프로필수정 클릭 연결 - 병우
    @Click(R.id.profile_button_modify_profile)
    void profileChangeButton() {
        Log.d(tag, "profileChangeButton");
        modifyProfie();
    }
    // 사진변경 클릭 연결 - 병우
    @Click(R.id.profile_button_modify_photo)
    void modifyPhotoButton() {
        Log.d(tag, "interestChangeButton");
        registPhoto();
    }
    // 로그아웃 클릭 연결 - 병우
    @Click(R.id.profile_button_logout)
    void logoutButton() {
        Log.d(tag, "settingButton");
        DoLogout();
    }


}
