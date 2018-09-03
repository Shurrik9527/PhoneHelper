package com.jerrywang.phonehelper.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jerrywang.phonehelper.R;


/**
 * Created by Shurrik on 2017/12/4.
 */

public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "AmountView";
    private int amount = 1; //购买数量
    //private int goods_storage = 20; //商品库存

    private OnAmountChangeListener mListener;

    private EditText etAmount;
    private Button bDecrease;
    private Button bIncrease;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.cart_amount_view, this);
        etAmount = (EditText) findViewById(R.id.et_amount);
        bDecrease = (Button) findViewById(R.id.b_decrease);
        bIncrease = (Button) findViewById(R.id.b_increase);
        bDecrease.setOnClickListener(this);
        bIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);

//        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
//        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
//        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
//        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
//        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);
//        obtainStyledAttributes.recycle();
//
//        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
//        bDecrease.setLayoutParams(btnParams);
//        bIncrease.setLayoutParams(btnParams);
//        if (btnTextSize != 0) {
//            bDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
//            bIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
//        }
//
//        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
//        etAmount.setLayoutParams(textParams);
//        if (tvTextSize != 0) {
//            etAmount.setTextSize(tvTextSize);
//        }
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

//    public void setGoods_storage(int goods_storage) {
//        this.goods_storage = goods_storage;
//    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.b_decrease) {
            if (amount > 1) {
                amount--;
                etAmount.setText(String.format("%d", amount));
            }
        } else if (i == R.id.b_increase) {
//            if (amount < goods_storage) {
                amount++;
                etAmount.setText(String.format("%d", amount));
//            }
        }

        etAmount.clearFocus();

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;
        amount = Integer.valueOf(s.toString());
//        if (amount > goods_storage) {
//            etAmount.setText(String.format("%d", goods_storage));
//            return;
//        }

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }


    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }

}