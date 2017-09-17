package tv.niuwa.live.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.androidutils.utils.StringUtils;
import tv.niuwa.live.R;
import tv.niuwa.live.intf.OnCustomClickListener;

/**
 * Created by Administrator on 2016/10/11.
 * Author: XuDeLong
 */
public class DialogEnsureUtiles {
    public static void showDialog(final Activity context){

    }
    public static void showInfo(final Activity context, final OnCustomClickListener listener,String val,String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_normal_layout, null);
        final AlertDialog dialog = builder.setView(inflate)
                .create();
        Button nagtive = (Button) inflate.findViewById(R.id.negativeButton);
        Button positive = (Button) inflate.findViewById(R.id.positiveButton);
        final TextView textViewTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        final TextView value = (TextView) inflate.findViewById(R.id.message);
        if(StringUtils.isNotEmpty(val)){
            value.setText(val);
        }
        textViewTitle.setText(title);
        nagtive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isEmpty(value.getText().toString())){
                    Toast.makeText(context,"请先输入内容",Toast.LENGTH_SHORT).show();
                }else{
                    listener.onClick(value.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public static void showConfirm(final Activity context,String msg,final OnCustomClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_normal_layout, null);
        final AlertDialog dialog = builder.setView(inflate)
                .create();
        Button nagtive = (Button) inflate.findViewById(R.id.negativeButton);
        Button positive = (Button) inflate.findViewById(R.id.positiveButton);
        final TextView textViewTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        final EditText value = (EditText) inflate.findViewById(R.id.message);
        value.setText(msg);
        value.setEnabled(false);

        nagtive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick("");
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
