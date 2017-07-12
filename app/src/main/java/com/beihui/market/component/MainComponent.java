package com.beihui.market.component;

import com.beihui.market.ui.activity.LoginActivity;
import com.beihui.market.ui.activity.RegisterActivity;
import com.beihui.market.ui.activity.RegisterPsdActivity;
import com.beihui.market.ui.fragment.TabHomeFragment;
import com.beihui.market.ui.fragment.TabLoanFragment;
import com.beihui.market.ui.fragment.TabMineFragment;
import com.beihui.market.ui.fragment.TabNewsFragment;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/7.
 * 用@Component表示这个接口是一个连接器，能用@Component注解的只能是interface或者抽象类
 */

@Component(dependencies = AppComponent.class)
public interface MainComponent {


    TabHomeFragment inject(TabHomeFragment fragment);
    TabLoanFragment inject(TabLoanFragment fragment);
    TabNewsFragment inject(TabNewsFragment fragment);
    TabMineFragment inject(TabMineFragment fragment);

    LoginActivity inject(LoginActivity activity);
    RegisterActivity inject(RegisterActivity activity);
    RegisterPsdActivity inject(RegisterPsdActivity activity);

}
