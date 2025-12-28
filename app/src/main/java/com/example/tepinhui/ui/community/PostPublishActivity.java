package com.example.tepinhui.ui.community;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tepinhui.NetworkUtils;
import com.example.tepinhui.R;
import com.example.tepinhui.Result;
import com.example.tepinhui.network.UserApiService;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PostPublishActivity extends AppCompatActivity {

    public static final String EXTRA_SOURCE = "extra_source"; // HOT / RECOMMEND / HELP
    private static final int MAX_IMAGES = 9;

    private EditText etContent;
    private Button btnPublish;
    private TextView tvAddImage;
    private RecyclerView rvImages;

    private final List<Uri> selectedUris = new ArrayList<>();
    private SelectedImageAdapter imageAdapter;

    private ActivityResultLauncher<String> pickImagesLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_publish);

        etContent = findViewById(R.id.et_content);
        btnPublish = findViewById(R.id.btn_publish);
        tvAddImage = findViewById(R.id.tv_add_image);
        rvImages = findViewById(R.id.rv_images);

        initImagePicker();

        btnPublish.setOnClickListener(v -> {
            String content = etContent.getText() == null ? "" : etContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }

            String token = UserApiService.getToken(this);
            if (TextUtils.isEmpty(token)) {
                Toast.makeText(this, "请先登录再发帖", Toast.LENGTH_SHORT).show();
                return;
            }

            String source = getIntent().getStringExtra(EXTRA_SOURCE);
            if (TextUtils.isEmpty(source)) source = "HELP";

            btnPublish.setEnabled(false);
            tvAddImage.setEnabled(false);

            if (selectedUris.isEmpty()) {
                // 无图：直接发帖
                createPost(token, source, content, new ArrayList<>());
            } else {
                // 有图：先上传，再发帖
                uploadAllImagesThenPost(token, source, content);
            }
        });
    }

    private void initImagePicker() {
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        imageAdapter = new SelectedImageAdapter(selectedUris);
        imageAdapter.setOnRemoveClickListener(position -> {
            if (position >= 0 && position < selectedUris.size()) {
                selectedUris.remove(position);
                imageAdapter.notifyDataSetChanged();
            }
        });
        rvImages.setAdapter(imageAdapter);

        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.GetMultipleContents(),
                uris -> {
                    if (uris == null) return;
                    if (uris.isEmpty()) return;

                    // 追加选择（最多 MAX_IMAGES）
                    for (Uri u : uris) {
                        if (u == null) continue;
                        if (selectedUris.size() >= MAX_IMAGES) break;
                        selectedUris.add(u);
                    }
                    if (selectedUris.size() >= MAX_IMAGES) {
                        Toast.makeText(this, "最多选择 " + MAX_IMAGES + " 张图片", Toast.LENGTH_SHORT).show();
                    }
                    imageAdapter.notifyDataSetChanged();
                }
        );

        tvAddImage.setOnClickListener(v -> pickImagesLauncher.launch("image/*"));
    }

    private void uploadAllImagesThenPost(String token, String source, String content) {
        List<String> uploadedUrls = new ArrayList<>();
        uploadNext(0, token, uploadedUrls, () -> createPost(token, source, content, uploadedUrls));
    }

    private void uploadNext(int index, String token, List<String> uploadedUrls, Runnable onAllDone) {
        if (index >= selectedUris.size()) {
            onAllDone.run();
            return;
        }

        Uri uri = selectedUris.get(index);
        File file = copyUriToCacheFile(uri);
        if (file == null) {
            Toast.makeText(this, "读取图片失败，已跳过第 " + (index + 1) + " 张", Toast.LENGTH_SHORT).show();
            uploadNext(index + 1, token, uploadedUrls, onAllDone);
            return;
        }

        Toast.makeText(this, "正在上传图片 " + (index + 1) + "/" + selectedUris.size(), Toast.LENGTH_SHORT).show();

        Type type = new TypeToken<Result<String>>() {}.getType();
        NetworkUtils.uploadImage("/api/upload", file, token, type, new NetworkUtils.Callback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result) {
                if (result != null && result.isSuccess() && result.getData() != null) {
                    uploadedUrls.add(result.getData());
                    uploadNext(index + 1, token, uploadedUrls, onAllDone);
                } else {
                    btnPublish.setEnabled(true);
                    tvAddImage.setEnabled(true);
                    Toast.makeText(PostPublishActivity.this, result != null ? result.getMsg() : "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                btnPublish.setEnabled(true);
                tvAddImage.setEnabled(true);
                Toast.makeText(PostPublishActivity.this, "图片上传失败：" + (msg == null ? "" : msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPost(String token, String source, String content, List<String> imageUrls) {
        // 对齐后端 CommunityPostCreateRequest：{ content, images, source }
        Map<String, Object> body = new HashMap<>();
        body.put("content", content);
        body.put("images", imageUrls == null ? new ArrayList<>() : imageUrls);
        body.put("source", source);

        Type type = new TypeToken<Result<Object>>() {}.getType();
        NetworkUtils.post("/api/community/posts", body, token, type, new NetworkUtils.Callback<Result<Object>>() {
            @Override
            public void onSuccess(Result<Object> result) {
                btnPublish.setEnabled(true);
                tvAddImage.setEnabled(true);
                if (result != null && result.isSuccess()) {
                    Toast.makeText(PostPublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PostPublishActivity.this, result != null ? result.getMsg() : "发布失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                btnPublish.setEnabled(true);
                tvAddImage.setEnabled(true);
                Toast.makeText(PostPublishActivity.this, "发布失败：" + (msg == null ? "" : msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File copyUriToCacheFile(Uri uri) {
        if (uri == null) return null;
        ContentResolver resolver = getContentResolver();

        String displayName = queryDisplayName(resolver, uri);
        String ext = "";
        if (displayName != null && displayName.contains(".")) {
            ext = displayName.substring(displayName.lastIndexOf('.'));
            if (ext.length() > 10) ext = "";
        }
        if (TextUtils.isEmpty(ext)) ext = ".jpg";

        File outFile = new File(getCacheDir(), "upload_" + UUID.randomUUID() + ext);

        try (InputStream in = resolver.openInputStream(uri);
             FileOutputStream out = new FileOutputStream(outFile)) {
            if (in == null) return null;
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            return outFile;
        } catch (Exception e) {
            return null;
        }
    }

    private String queryDisplayName(ContentResolver resolver, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) return cursor.getString(idx);
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
