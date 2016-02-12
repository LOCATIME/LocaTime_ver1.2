package kr.co.edttext;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Joonha on 2015-12-02.
 */
public class MyEditText extends EditText{

    Paint paint;

    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){

        paint=new Paint();
        paint.setColor(R.color.colorAccent);

    }
}
