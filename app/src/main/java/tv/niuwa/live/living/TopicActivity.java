package tv.niuwa.live.living;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.activity.BaseActivity;
import com.smart.androidutils.utils.DensityUtils;
import tv.niuwa.live.R;
import tv.niuwa.live.intf.OnRequestDataListener;
import tv.niuwa.live.utils.Api;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/12/7.
 * Author: XuDeLong
 */
public class TopicActivity extends BaseActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.radiogroup_term)
    RadioGroup mRadiogroupTerm;


    private ArrayList<TermModel> terms;
    private TermModel mCurrntTerm ;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("话题列表");
        terms = new ArrayList<>();
        initData();
    }

    private void initData() {
        JSONObject params = new JSONObject();
        Api.getTerm(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                terms.clear();
                JSONArray temp = data.getJSONArray("data");
                for (int i =0 ;i<temp.size();i++){
                    TermModel model = new TermModel();
                    model.setTerm_id(temp.getJSONObject(i).getString("term_id"));
                    model.setName(temp.getJSONObject(i).getString("name"));
                    terms.add(model);
                }
                for(int i=0; i<terms.size(); i++)
                {
                    int dp = DensityUtils.dip2px(TopicActivity.this,10);
                    RadioButton tempButton = new RadioButton(TopicActivity.this);
                    tempButton.setBackgroundResource(R.drawable.btn_topic_selector);   // 设置RadioButton的背景图片
                    tempButton.setButtonDrawable(null);           // 设置按钮的样式
                    tempButton.setPadding(dp,dp/3,dp,dp/3);
                    tempButton.setId(i);
                    tempButton.setText(terms.get(i).getName());
                    tempButton.setTag(terms.get(i).getTerm_id());
                    tempButton.setTextColor(getResources().getColor(R.color.colorGrayFont1));

                    mRadiogroupTerm.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tempButton.getLayoutParams();
                    params.setMargins(dp,dp,dp,dp);
                    tempButton.setLayoutParams(params);
                }

            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
        mRadiogroupTerm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                mCurrntTerm = terms.get(id);
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_topic;
    }

    @OnClick(R.id.btn_start_living)
    public void btnStartLiving(){
        if(null == mCurrntTerm){
            toast("您还没选择话题");
            return;
        }
        Intent data = new Intent();
        data.putExtra("term",mCurrntTerm);
        setResult(1,data);
        finish();
    }
}
