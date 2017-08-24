package com.cognitive.face.recognition.api.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cognitive.face.recognition.api.data.model.User;
import com.cognitive.face.recognition.api.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirebaseRegisterFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @BindView(com.cognitive.face.recognition.api.R.id.et_first_name)
    EditText mText;

    @BindView(com.cognitive.face.recognition.api.R.id.et_last_name)
    EditText mTest;

    @BindView(com.cognitive.face.recognition.api.R.id.et_dob)
    EditText mEditText;

    @BindView(com.cognitive.face.recognition.api.R.id.et_email)
    EditText mEtEmail;

    @BindView(com.cognitive.face.recognition.api.R.id.et_password)
    EditText mEtUserPassword3DSecure;

    public FirebaseRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(com.cognitive.face.recognition.api.R.layout.fragment_register,container,false);
        ButterKnife.bind(this, view);//initialize library

        mFirebaseAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @OnClick(com.cognitive.face.recognition.api.R.id.btn_register)
    public void onRegisterClick(){
    registerUser();

    }

    private void registerUser(){
        final String email = mEtEmail.getText().toString();
        String password = mEtUserPassword3DSecure.getText().toString();
        final String firstName = mText.getText().toString();
        final String lastName = mTest.getText().toString();
        final String dob = mEditText.getText().toString();

        if (email.matches("") || password.matches("")) {
            Toast.makeText(getActivity(),"Please input valid Email and Password.",Toast.LENGTH_SHORT).show();
            return;
        }


     mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
           if (task.isSuccessful())  {
               Toast.makeText(getActivity(),"Registration is successful.",Toast.LENGTH_SHORT).show();
               String userId = task.getResult().getUser().getUid();
               saveUserInfo(new User(firstName,lastName,dob,email),userId);
               Intent intent = new Intent(getActivity(), MainActivity.class);
               startActivity(intent);
           }
         }
     }).addOnFailureListener(getActivity(), new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
         }
     });


    }

    private void saveUserInfo(User user, String userId) {
        mDatabaseReference.child("users").child(userId).setValue(user);
    }

}
