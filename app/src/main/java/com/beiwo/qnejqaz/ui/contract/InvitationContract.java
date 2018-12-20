package com.beiwo.qnejqaz.ui.contract;


import com.beiwo.qnejqaz.base.BasePresenter;
import com.beiwo.qnejqaz.base.BaseView;
import com.beiwo.qnejqaz.entity.Invitation;

import java.util.List;

public interface InvitationContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showInvitationCode(String code);

        void showInvitations(List<Invitation.Row> list);
    }
}
