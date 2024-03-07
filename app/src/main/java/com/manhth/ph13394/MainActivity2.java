package com.manhth.ph13394;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {
    TextView tvKQ;
    FirebaseFirestore database;
    Context context = this;
    String strKQ = "";

    ToDo toDo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvKQ = findViewById(R.id.tvKQ);
        database = FirebaseFirestore.getInstance(); // khởi tạo
        //insert();
        //update();
        //select();
        delete();
    }

    void insert(){
        String id = UUID.randomUUID().toString();   // lấy chuỗi ngẫu nhiên
        toDo = new ToDo(id, "title 11", "content 11");  //tạo đối tượng mới để insert
        database.collection("TODO") //truy cập đến bảng dữ liệu
                .document(id).  //truy cập đến dòng dữ liệu
                set(toDo.convertToHashMap())// đưa dữ liệu vào bảng
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {// thanh cong
                        Toast.makeText(context, "insert thanh cong", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {   // thất bại
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "insert that bai", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void update(){
        String id = "358d8e7c-71d4-4bf2-98de-68a962cf7dbd";
        toDo = new ToDo(id, "title 11 update", "content 11 update"); // nội dung cần update
        database.collection("TODO") // lấy bảng dữ liệu
                .document(id)// lấy id
                .update(toDo.convertToHashMap()) // thực hiện update
                .addOnSuccessListener(new OnSuccessListener<Void>() {   // thanh cong
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "update thanh cong", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() { // that bai
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "update that bai", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    void delete(){
        String id = "358d8e7c-71d4-4bf2-98de-68a962cf7dbd";
        database.collection("TODO")// truy cập vào bảng dữ liệu
                .document(id)// truy cập id
                .delete() // thực hiện xóa
                .addOnCompleteListener(new OnCompleteListener<Void>() { // xoa thanh cong
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() { // that bai
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    ArrayList<ToDo> select(){
        ArrayList<ToDo> list = new ArrayList<>();
        database.collection("TODO") // truy cap vao bang dư lieu
                .get() //lay ve du lieu
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            strKQ = "";
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                ToDo t = doc.toObject(ToDo.class); // chuyển dữ liệu đọc được sang ToDo
                                list.add(t);
                                strKQ += "id: "+ t.getId() + "\n";
                                strKQ += "title: "+ t.getTitle() + "\n";
                                strKQ += "content: "+ t.getContent() + "\n";

                            }
                            tvKQ.setText(strKQ);

                        }else {
                            Toast.makeText(context, "select that bai", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        return list;
    }
}