package com.example.roombasic;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private Button buttonsumbit;
    private TextView editTextChinese,editTextEnglish;
    private WordViewModel wordViewModel;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();
        wordViewModel = ViewModelProviders.of(activity).get(WordViewModel.class);
        buttonsumbit = activity.findViewById(R.id.buttonsubmit);
        editTextEnglish = activity.findViewById(R.id.editTextEnglish);
        editTextChinese = activity.findViewById(R.id.editTextChinese);
        buttonsumbit.setEnabled(false);
        /*显示键盘*/
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(editTextEnglish,0);
        imm.showSoftInput(editTextChinese,0);
        TextWatcher textWatcher = new TextWatcher() {//文字监听器，当输入框中的文字改变时调用
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String english = editTextEnglish.getText().toString().trim();//获取输入框中的文字，trim是过滤前面的空格
                String chinese = editTextChinese.getText().toString().trim();
                buttonsumbit.setEnabled(!english.isEmpty() && !chinese.isEmpty());//输入框都不为空时才起作用
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextEnglish.addTextChangedListener(textWatcher);//输入框添加以上监听事件，不然不起作用
        editTextChinese.addTextChangedListener(textWatcher);
        buttonsumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = editTextEnglish.getText().toString().trim();
                String chinese = editTextChinese.getText().toString().trim();
                Word word = new Word(english,chinese);
                wordViewModel.insert_interface(word);
                NavController controller = Navigation.findNavController(v);
                controller.navigateUp();//更新
                /*关闭键盘*/
                InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });
    }
}
