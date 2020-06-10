package notification;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Objects;

import androidx.annotation.NonNull;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.d("npl","$$$$$$$$$$$$$$$$$$$$$$$$");
                            return;
                        }

                        String refreshToken = Objects.requireNonNull(task.getResult()).getToken();
                        if(firebaseUser != null){
                            updateToken(refreshToken);
                        }
                    }
                });

    }

    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Token token = new Token(refreshToken);
        assert firebaseUser != null;
        databaseReference.child(firebaseUser.getUid()).setValue(token);
    }

}
