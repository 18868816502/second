package com.beihui.market.component;

import com.beihui.market.ui.activity.LoginActivity;
import com.beihui.market.ui.activity.RegisterActivity;
import com.beihui.market.ui.activity.RegisterPsdActivity;
import com.beihui.market.ui.fragment.Main1Fragment;
import com.beihui.market.ui.fragment.Main2Fragment;
import com.beihui.market.ui.fragment.Main3Fragment;
import com.beihui.market.ui.fragment.Main4Fragment;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/7.
 * 用@Component表示这个接口是一个连接器，能用@Component注解的只能是interface或者抽象类
 */

@Component(dependencies = AppComponent.class)
public interface MainComponent {


    Main1Fragment inject(Main1Fragment fragment);
    Main2Fragment inject(Main2Fragment fragment);
    Main3Fragment inject(Main3Fragment fragment);
    Main4Fragment inject(Main4Fragment fragment);

    LoginActivity inject(LoginActivity activity);
    RegisterActivity inject(RegisterActivity activity);
    RegisterPsdActivity inject(RegisterPsdActivity activity);

}
