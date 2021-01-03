package com.example.roombasic;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordFragment extends Fragment {

    private WordViewModel wordViewModel;
    private RecyclerView recyclerView;
    private MyAdapter adapter1,adapter2;
    private LiveData<List<Word>> filterWords;//过滤过后的单词(查找)
    private static final String VIEW_TYPE_SHP = "view_type_shp";//用在用户偏好上面
    private List<Word> allwords;//用于滑动删除时防止空指针

    public WordFragment() {//显示主菜单
        setHasOptionsMenu(true);//fragment默认不显示menu，所以要写这一句来显示
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word, container, false);
    }


        /*拖动移位，滑动删除*/
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.START | ItemTouchHelper.END){//两个参数，第一个参数为拖动，第二个参数为滑动
            @Override//拖动
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Word wordFrom = allwords.get(viewHolder.getAdapterPosition());
                Word wordTo = allwords.get(target.getAdapterPosition());
                int idFrom = wordFrom.getId();
                wordFrom.setId(wordTo.getId());
                wordTo.setId(idFrom);//交换两个词汇的id
                wordViewModel.update_interface(wordFrom,wordTo);//更新连着的数据
                adapter1.notifyDataSetChanged();//数据变化时监听改变
                adapter2.notifyDataSetChanged();
                adapter1.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());//交换位置
                adapter2.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }

            @Override//滑动
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordToDelete = allwords.get(viewHolder.getAdapterPosition());//获取删除的单词的位置，使用allWords而不是使用filterWords是为了防止空指针
                wordViewModel.delete_interface(wordToDelete);
                /*滑动删除的撤销,为了防止遮住增添的悬浮按钮，所以将wordFragmeng的Fragment改为CoordinatorLayout*/
                Snackbar.make(requireActivity().findViewById(R.id.Words),"删除了一个词汇",Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wordViewModel.insert_interface(wordToDelete);//把删除的单词加回来
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);//使得其与RecyclerView绑定，才能起作用

    }

    @Override
    public void onResume() {//重新打开此界面时，去掉键盘
        super.onResume();
        InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
    }

    /*菜单的一些内容*/
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);//设置宽度，以免挡住标题
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            /*查询框*/
            @Override
            public boolean onQueryTextChange(String newText) {//只要改变就监听
                String patten = newText.trim();//过滤空格
                filterWords.removeObservers(getViewLifecycleOwner());//因为CreateActivity方法中已经建立了监听，所以避免冲突，将那个先移除
                filterWords = wordViewModel.findWordWithPatten(patten);
                filterWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = adapter1.getItemCount();
                        allwords = words;
                        adapter1.setAllWords(words);
                        adapter2.setAllWords(words);
                        if(temp!=words.size()){
                            adapter1.notifyDataSetChanged();
                            adapter2.notifyDataSetChanged();
                        }
                    }
                });
                return false;//如果后面还要做些什么就返回true，不做就返回false
            }
        });
    }

    /*menu上的其它item*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear://清空数据
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());//参数为建立在那个地方
                builder.setTitle("清空数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.clear_interface();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
