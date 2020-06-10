package com.example.complaintapp;

import notification.MyResponse;
import notification.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3vF6gyA:APA91bGTkMFzgWm5f5npb3v56fUFOEaCY6q5OuqQVhm38vj8Ax-zSPi5pOPRFzKfkHmEStjRwSUrulBD1eMsRSimSs9bA7odbxuz5Z2-e4MCGqLshY9vHlrkmVmWMmsYLsHT8j5vS1EX"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
