package me.maxandroid.mainproject.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;

import me.maxandroid.common.ui.compoment.HiBaseFragment;
import me.maxandroid.mainproject.R;

public class HomePageFragment extends HiBaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutView.findViewById(R.id.profile).setOnClickListener(v->
                navigation("/profile/detail")
        );
        layoutView.findViewById(R.id.vip).setOnClickListener(v->
                navigation("/profile/vip")
        );
        layoutView.findViewById(R.id.auth).setOnClickListener(v->
                navigation("/profile/authentication")
        );
        layoutView.findViewById(R.id.unknow).setOnClickListener(v->
                navigation("/profile/unknow")
        );
    }

    void navigation(String path) {
        ARouter.getInstance().build(path).navigation();
    }
}
