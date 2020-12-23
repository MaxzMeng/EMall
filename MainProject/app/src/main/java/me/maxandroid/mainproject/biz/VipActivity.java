package me.maxandroid.mainproject.biz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

import me.maxandroid.mainproject.route.RouteFlag;

@Route(path = "/profile/vip", extras = RouteFlag.FLAG_VIP)
public class VipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
