package com.example.q.plzshow.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.q.plzshow.R;
import com.example.q.plzshow.adapter.RoundedImageView;
import com.example.q.plzshow.App;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

public class CTabFragment extends Fragment {
    private ImageView user_pic;
    private TextView user_name, joindate, user_phone, user_coin;
    private String user_id, user_pic_url;
    private Bitmap upload_img;

    private final int SELECT_PHOTO = 1;

    public CTabFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CTabFragment newInstance() {
        return new CTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ctab, container, false);
        user_pic = (ImageView) rootView.findViewById(R.id.imgView_profile);
        user_name = (TextView) rootView.findViewById(R.id.txtView_user_name);
        joindate = (TextView) rootView.findViewById(R.id.txtView_joindate);
        user_phone = (TextView) rootView.findViewById(R.id.txtView_user_phone);
        user_coin = (TextView) rootView.findViewById(R.id.txtView_user_coin);

        user_pic.setOnClickListener(userPicOnClicked);

        user_name.setTypeface(App.NanumBarunGothic);

        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        user_id =  pref.getString("user_id", "");
        Log.e("user_id", user_id);

        getUserData();

        return rootView;
    }

    View.OnClickListener userPicOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 1. SHOW IMAGE SELECTOR
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        user_pic.setImageBitmap(selectedImage);

                        // 2. IF USER SELECTS AN IMAGE AND CLICKS DONE, MAKE IMAGE UPLOAD REQ
                        // 3. SEND THE REQ AND GET IMG URL FROM RES
                        upload_img = selectedImage;
                        String url = user_pic_url;
                        if (upload_img != null)
                            url = uploadImage(upload_img);

                        // 4. SET IMAGE USING LOAD IMAGE FUNCTION
                        try {
                            Bitmap bmp = new loadImage().execute(url).get();
                            user_pic.setImageBitmap(bmp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        this.onResume();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int result = getUserData();
        Log.e("GET_USER", String.valueOf(result));
    }

    private String base64Encode(Bitmap bmp) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, ByteStream);
        byte[] b = ByteStream.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.NO_WRAP);
        encoded = "data:image/jpeg;base64," + encoded;
        return encoded;
    }

    private String uploadImage(Bitmap bmp) {
        String url = "";
        try {
            // GENERATE REQUEST
            JSONObject req = new JSONObject();
            req.put("type", "UPLOAD_USER_PIC");
            req.put("user_id", user_id);
            req.put("img", base64Encode(bmp));

            // GET RESPONSE
            JSONObject res = new sendJSON("http://52.78.200.87:3000", req.toString(), "application/json").execute().get();
            if (res == null) {
                Log.e("null response", "res = null");
                return "";
            } else if (!res.has("result") || res.get("result").equals("failed")) {
                Log.e("failed", res.toString());
                return "";
            }

            // PARSE RESPONSE
            if (res.has("url")) url = res.getString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private int getUserData() {
        try {
            // GENERATE REQUEST
            JSONObject req = new JSONObject();
            req.put("type", "GET_USER");
            req.put("user_id", user_id);

            // GET RESPONSE
            JSONObject res = new sendJSON("http://52.78.200.87:3000", req.toString(), "application/json").execute().get();
            if (res == null) {
                Log.e("null response", "res = null");
                return -1;
            } else if (!res.has("result") || res.get("result").equals("failed")) {
                Log.e("failed", res.toString());
                return -1;
            }

            // PARSE RESPONSE
            SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            if (res.has("name")) {
                user_name.setText(res.getString("name"));
                editor.putString("name", res.getString("name"));
            }
            if (res.has("joindate")) joindate.setText(formatDateString(res.getString("joindate")));
            if (res.has("phone")) {
                user_phone.setText(res.getString("phone"));
                editor.putString("phone", res.getString("phone"));
            }
            if (res.has("coin")) {
                user_coin.setText(res.getString("coin") + "원");
                editor.putString("coin", res.getString("coin"));
            }
            if (res.has("pic")) {
                try {
                    user_pic_url = res.getString("pic");
                    Bitmap bmp = new loadImage().execute(user_pic_url).get();
                    user_pic.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            editor.commit();

            SharedPreferences pp = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            Log.e("name,phone,coin", pp.getString("name","")+pp.getString("phone","")+pp.getString("coin",""));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private String trimZero (String number) {
        return String.valueOf(Integer.parseInt(number));
    }

    private String formatDateString (String datetime) {
        String yyyy = datetime.substring(0,4);
        String mm = datetime.substring(5,7);
        String dd = datetime.substring(8,10);
        String formatted = trimZero(yyyy) + "년 " + trimZero(mm) + "월 " + trimZero(dd) + "일";
        return formatted;
    }

    private class loadImage extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
                image = RoundedImageView.getCroppedBitmap(image, image.getWidth());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }
    }

    private class sendJSON extends AsyncTask<Void, Void, JSONObject> {
        String urlstr;
        String data;
        String contentType;

        public sendJSON(String url, String data, String contentType) {
            this.urlstr = url;
            this.data = data;
            this.contentType = contentType;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            HttpURLConnection conn;
            OutputStream os;
            InputStream is;
            BufferedReader reader;
            JSONObject json;
            try {
                URL url = new URL(urlstr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);

                // If sending to our DB
                if (urlstr.contains("52.78.200.87")) {
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", contentType);
                    conn.setRequestProperty("Accept-Charset", "UTF-8");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(data.getBytes());
                    os.flush();
                    os.close();
                }

                int statusCode = conn.getResponseCode();
                Log.e("STATUS CODE", String.valueOf(statusCode));

                is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }
                reader.close();
                json = new JSONObject(response.toString());
                conn.disconnect();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                return null;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }
    }
}
