package com.example.retea.licentapp.fragments;

        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.example.retea.licentapp.R;

public class FavouritesFragment extends Fragment {

    private static final String TAG = "FavouritesFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_tab_fragment,container,false);

        return view;
    }
}
