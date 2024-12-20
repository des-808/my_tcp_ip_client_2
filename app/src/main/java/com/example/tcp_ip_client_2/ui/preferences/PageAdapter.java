package com.example.tcp_ip_client_2.ui.preferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {
    int pages  = Pages.values().length;


    public PageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PageId.setIdPage(position);
        return(Preference_Page_Fragment.newInstance(position));
    }

    @Override
    public int getItemCount() {
        return pages;
    }
}
/*public enum Pages {
    PAGE1("Serial"),
    PAGE2("Terminal"),
    PAGE3("Receive"),
    PAGE4("Send"),
    PAGE5("Misc."),
    PAGE6("xz");

    private final String name;

    Pages(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}*/
