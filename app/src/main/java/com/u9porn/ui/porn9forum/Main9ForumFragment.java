package com.u9porn.ui.porn9forum;


import androidx.fragment.app.Fragment;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class Main9ForumFragment extends BaseMainFragment {

    @Override
    public int getCategoryType() {
        return Category.TYPE_91PORN_FORUM;
    }

    public static Main9ForumFragment getInstance() {
        return new Main9ForumFragment();
    }
}
