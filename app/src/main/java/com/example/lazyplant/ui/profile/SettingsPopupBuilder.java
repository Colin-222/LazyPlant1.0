package com.example.lazyplant.ui.profile;

import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;

public class SettingsPopupBuilder {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SettingsPopupBuilder(SharedPreferences pref) {
        this.pref = pref;
        this.editor = pref.edit();
    }

    public void buildSettingsPopup(final View root, LayoutInflater inflater){
        final View popupView = inflater.inflate(R.layout.layout_get_user_info, null, false);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        root.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
                final EditText edit_name = (EditText)popupView.findViewById(R.id.get_user_name_edit);
                final EditText edit_postcode = (EditText)popupView.findViewById(R.id.get_user_postcode_edit);
                popupView.findViewById(R.id.get_user_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean name_passed = setUsername(edit_name.getText().toString());
                        boolean postcode_passed = setPostcode(edit_postcode.getText().toString());
                        if(!name_passed){
                            Toast toast = Toast.makeText(popupView.getContext(),
                                    "Please enter a username", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        if(!postcode_passed){
                            Toast toast = Toast.makeText(popupView.getContext(),
                                    "Invalid postcode punk", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        if(name_passed && postcode_passed){
                            popupWindow.dismiss();
                        }
                    }
                });
            }
        });
    }

    private boolean setUsername(String username){
        if (!username.equals("")) {
            this.editor.putString(Constants.SHARED_PREFERENCE_NAME, username);
            this.editor.commit();
            return true;
        }else{
            return false;
        }
    }

    private boolean setPostcode(String postcode){
        ClimateZoneGetter czg = new ClimateZoneGetter();
        int zone = czg.getZone(postcode);
        if (zone != -1) {
            this.editor.putString(Constants.SHARED_PREFERENCE_POSTCODE, postcode);
            this.editor.commit();
            return true;
        }else{
            return false;
        }
    }
}
