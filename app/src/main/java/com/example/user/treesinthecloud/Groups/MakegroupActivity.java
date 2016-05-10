package com.example.user.treesinthecloud.Groups;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakegroupActivity extends AppCompatActivity {

    private ListView lView;
    private List list;
    private AutoCompleteTextView  addUser;
    private MyCustomAdapter adapter;
    private TextView userError;
    private EditText groupnameTextfield;
    private ArrayList<String> users;
    private PopupWindow popupMessage;
    private PopupWindow popupMessageChooseGroupname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_make_group);
        addUser = (AutoCompleteTextView  )findViewById(R.id.addUserText);
        userError = (TextView) findViewById(R.id.user_does_not_exist);


        //Toast.makeText(getApplicationContext(), "list", Toast.LENGTH_SHORT).show();

        users = new ArrayList<String>(Arrays.asList("henk", "jos", "jaak", "louis", "geert", "nina", "amy", "frank", "user1", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9"));

        //generate list
        list = new ArrayList<String>();

        //instantiate custom adapter
        adapter = new MyCustomAdapter(getApplicationContext(),list, this);
        ArrayAdapter<String> autoCompleteAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, users);
        addUser.setAdapter(autoCompleteAdapter);

        //handle listview and assign adapter
        lView = (ListView)findViewById(R.id.listViewUsers);
        if(adapter!=null){
            lView.setAdapter(adapter);
        }

        UpdateViewOfList(lView);

    }

    public boolean addUserToList(View view){

        String text = addUser.getText().toString();

        if(addUser.getText().length() == 0 || addUser.getText().toString().equals("")){
            return false;
        }
        else{
            if(users.contains(text)) {

                list.add(addUser.getText().toString());
                addUser.setText("");
                userError.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                UpdateViewOfList(view);
                return true;
            }
            else{
                userError.setVisibility(View.VISIBLE);
                return false;

            }

        }
    }

    public void UpdateViewOfList(View view){

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(lView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = lView.getLayoutParams();
        params.height = totalHeight + (lView.getDividerHeight() * (adapter.getCount() - 1));
        lView.setLayoutParams(params);
        lView.requestLayout();

    }

    public void ContinueButton(View view){
        if(addUser.getText().length() == 0 || addUser.getText().toString().equals("")){
            popupInitChooseGroupname();
            popupMessageChooseGroupname.showAtLocation(view, Gravity.TOP, 0, 120);
        }
        else {
            popupInit(addUser.getText().toString());
            popupMessage.showAtLocation(view, Gravity.TOP, 0, 120);
        }
    }

    public void YesPopup(View view){
        if(addUserToList(view)){
            popupMessage.dismiss();
            popupInitChooseGroupname();
            popupMessageChooseGroupname.showAtLocation(view, Gravity.TOP, 0, 120);
        }
        else{
            popupMessage.dismiss();
        }
    }

    public void NoPopup (View view){
        addUser.setText("");
        popupMessage.dismiss();
        popupInitChooseGroupname();
        popupMessageChooseGroupname.showAtLocation(view, Gravity.TOP, 0, 120);
    }

    public void popupInit(String username) {
        Context context = getBaseContext();
        LinearLayout viewG = (LinearLayout) findViewById(R.id.layout_popup_makegroup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_popup_makegroup, viewG);
        popupMessage = new PopupWindow(findViewById(R.id.layout_popup_makegroup),ViewGroup.LayoutParams.WRAP_CONTENT ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupMessage.setContentView(layout);
        popupMessage.setFocusable(true);
        View view =  popupMessage.getContentView();
        TextView user = (TextView) view.findViewById(R.id.popup_text_user);
        user.setText(username);
    }

    public void cancelPopup(View view){
        popupMessageChooseGroupname.dismiss();
    }

    public void confirmPopup(View view){
        boolean checkGroupName = false;
        View contentview =  popupMessageChooseGroupname.getContentView();
        TextView groupnameError = (TextView) contentview.findViewById(R.id.groupnameError);
        groupnameTextfield = (EditText) contentview.findViewById(R.id.groupnameTextfield);
        if(checkGroupName){
            groupnameError.setVisibility(View.INVISIBLE);
            popupMessageChooseGroupname.dismiss();
            String groupname = groupnameTextfield.getText().toString();
            Toast.makeText(getApplicationContext(), "Succesfully made " + groupname , Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            groupnameError.setVisibility(View.VISIBLE);
        }

    }

    public void popupInitChooseGroupname() {
        Context context = getBaseContext();
        LinearLayout viewG = (LinearLayout) findViewById(R.id.layout_popup_choose_groupname);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_choose_groupname, viewG);
        popupMessageChooseGroupname = new PopupWindow(findViewById(R.id.layout_popup_choose_groupname),ViewGroup.LayoutParams.WRAP_CONTENT ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupMessageChooseGroupname.setContentView(layout);
        popupMessageChooseGroupname.setFocusable(true);

    }
}
