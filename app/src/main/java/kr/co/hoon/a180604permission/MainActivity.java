package kr.co.hoon.a180604permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 권한 요청이 왔을 때 호출되는 메소드, 콜백메소드
    // requestCode는 요청한 권한을 구분하기 위한 정수
    // permissions는 요청한 권한들의 배열
    // grantResult는 사용자의 응답
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 전화걸기 요청을 한 경우
        if(requestCode == 1000) {
            // 유저가 사용을 허가한 경우
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 전화걸기
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-3219-3274"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            } else {
                // 유저가 사용을 허가하지 않은 경우
                Toast.makeText(MainActivity.this, "전화걸기 권한 부여안함", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 운영체제 버전 확인
                // 6.0이상일 때와 아닐 때를 구분
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    // 전화걸기 권한이 있는지 확인
                    int permissionResult = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if(permissionResult == PackageManager.PERMISSION_DENIED){
                        // 권한이 없으면
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                            // 권한을 거부한 적이 있으면
                            // 왜 권한이 필요한지 설명하는 UI를 화면에 출력하도록 권장
                            // 대화상자(Dialog)를 출력해서 설명하는 경우가 많음
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            // 메소드체이닝을 이용해서 생성
                            dialog
                                    .setTitle("전화 걸기 권한이 필요합니다.")
                                    .setMessage("전화걸기 기능이 없으면 전화를 걸 수 없습니다. 계속 하시겠습니까?")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[] {Manifest.permission.CALL_PHONE},1000);
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this, "전화걸기 권한 부여안함", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .create()
                                    .show();

                        }else{
                            // 권한을 거부한 적이 없으면 - 권한을 처음 요청
                            // 첫번째 매개변수는 필요한 권한들의 배열
                            // 두번쨰 매개변수는 구분하기 위한 숫자
                            requestPermissions(new String[] {Manifest.permission.CALL_PHONE},1000);
                        }
                    }else{
                        // 권한이 있으면
                        // 전화걸기
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-3219-3274"));
                        startActivity(intent);
                    }

                }else{
                    // 전화걸기
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-3219-3274"));
                    startActivity(intent);
                }
            }
        });
    }
}
