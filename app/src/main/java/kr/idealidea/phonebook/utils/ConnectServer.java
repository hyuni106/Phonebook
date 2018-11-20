package kr.idealidea.phonebook.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;


public class ConnectServer {
//    private final static String BASE_URL = "http://172.30.1.57:5000/";
    private final static String BASE_URL = "http://192.168.0.149:5000/";
//    private final static String BASE_URL = "http://172.30.1.11:5000/";

    public static boolean checkIntenetSetting(final Context context) {
        boolean isConnected = false;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {
            Log.i("연결됨", "연결이 되었습니다.");
            isConnected = true;
        } else {
            Log.i("연결 안 됨", "연결이 다시 한번 확인해주세요");
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }, 0);
            isConnected = false;
        }

        return isConnected;
    }


    //    JSON 처리 부분 인터페이스
    public interface JsonResponseHandler {
        void onResponse(JSONObject json);
    }

    public static void getRequestUserInfo(Context context, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //URL에 포함할 Query문 작성 Name&Value
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "auth").newBuilder();
        String requestUrl = urlBuilder.build().toString();

        //Query문이 들어간 URL을 토대로 Request 생성
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(requestUrl)
                .build();

        //만들어진 Request를 서버로 요청할 Client 생성
        //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void getRequestAllContact(Context context, String phone, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //URL에 포함할 Query문 작성 Name&Value
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "contact").newBuilder();
        urlBuilder.addEncodedQueryParameter("phone", phone);
        String requestUrl = urlBuilder.build().toString();

        //Query문이 들어간 URL을 토대로 Request 생성
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(requestUrl)
                .build();

        //만들어진 Request를 서버로 요청할 Client 생성
        //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getRequestAllCallLogs(Context context, String phone, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //URL에 포함할 Query문 작성 Name&Value
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "call_log").newBuilder();
        urlBuilder.addEncodedQueryParameter("phone", phone);
        String requestUrl = urlBuilder.build().toString();

        //Query문이 들어간 URL을 토대로 Request 생성
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(requestUrl)
                .build();

        //만들어진 Request를 서버로 요청할 Client 생성
        //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getRequestAllMessage(Context context, String phone, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //URL에 포함할 Query문 작성 Name&Value
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "message").newBuilder();
        urlBuilder.addEncodedQueryParameter("phone", phone);
        String requestUrl = urlBuilder.build().toString();

        //Query문이 들어간 URL을 토대로 Request 생성
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(requestUrl)
                .build();

        //만들어진 Request를 서버로 요청할 Client 생성
        //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void postRequestCallNumInfo(Context context, String phone, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder()
                .add("phone", phone)
                .build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(BASE_URL + "popup")
                .post(requestBody)
                .build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("aaaa", "Response Body is " + response.body().string());
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void postRequestLogin(Context context, String user_id, String auth, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", user_id)
                .add("auth", auth)
                .build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        Request request = new Request.Builder()
                .url(BASE_URL + "auth")
                .post(requestBody)
                .build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("aaaa", "Response Body is " + response.body().string());
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void putRequestSignUp(Context context, String user_id, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        //Request Body에 서버에 보낼 데이터 작성
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", user_id)
                .build();

        //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
        Request request = new Request.Builder()
                .url(BASE_URL + "auth")
                .put(requestBody)
                .build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("aaaa", "Response Body is " + response.body().string());
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void putRequestCallLog(Context context, List<String> callLogs, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        // Initialize Builder (not RequestBody)
        FormBody.Builder builder = new FormBody.Builder();

        // Add Params to Builder
        String value = "";

        for (int i = 0; i<callLogs.size(); i++) {
            if (i != callLogs.size() - 1) {
                value = value + callLogs.get(i) + "\n";
            } else {
                value = value + callLogs.get(i);
            }
        }

        builder.add("call_logs", value);

        // Create RequestBody
        RequestBody requestBody = builder.build();

        // Create Request (same)
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(BASE_URL + "call_log")
                .put(requestBody)
                .build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("aaaa", "Response Body is " + response.body().string());
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void putRequestMessage(Context context, List<String> callLogs, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        // Initialize Builder (not RequestBody)
        FormBody.Builder builder = new FormBody.Builder();

        // Add Params to Builder
        String value = "";
        for (int i = 0; i<callLogs.size(); i++) {
            if (i != callLogs.size() - 1) {
                value = value + callLogs.get(i) + "\n";
            } else {
                value = value + callLogs.get(i);
            }
        }
        builder.add("messages", value);

        // Create RequestBody
        RequestBody requestBody = builder.build();

        // Create Request (same)
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(BASE_URL + "message")
                .put(requestBody)
                .build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("aaaa", "Response Body is " + response.body().string());
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void putRequestContacts(Context context, List<String> callLogs, final JsonResponseHandler handler) {
        if (!checkIntenetSetting(context)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        // Initialize Builder (not RequestBody)
        FormBody.Builder builder = new FormBody.Builder();

        // Add Params to Builder
        String value = "";
        for (int i = 0; i<callLogs.size(); i++) {
            if (i != callLogs.size() - 1) {
                value = value + callLogs.get(i) + "\n";
            } else {
                value = value + callLogs.get(i);
            }
        }
        builder.add("contacts", value);

        // Create RequestBody
        RequestBody requestBody = builder.build();

        // Create Request (same)
        Request request = new Request.Builder()
                .header("X-Http-Token", ContextUtils.getUserToken(context))
                .url(BASE_URL + "contact")
                .put(requestBody)
                .build();

        //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error", "Connect Server Error is " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("aaaa", "Response Body is " + response.body().string());
                String body = response.body().string();
                Log.d("log", "서버에서 응답한 Body:" + body);
                try {
                    JSONObject json = new JSONObject(body);
                    if (handler != null)
                        handler.onResponse(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
