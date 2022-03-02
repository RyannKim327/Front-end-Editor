package a;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.widget.TextView;
import android.widget.Toast;
public class toast {
    public static Toast makeText(Context c, String str, int i){
        Toast t = new Toast(c);
        TextView v = new TextView(c);
        float r = 10;
        ShapeDrawable d = new ShapeDrawable(new RoundRectShape(new float[]{r, r, r, r, r, r, r, r}, null, null));
        d.getPaint().setColor(Color.parseColor("#1d7fe8"));
        v.setText(str);
        v.setPadding(5, 10, 5, 10);
        v.setBackgroundDrawable(d);
        v.setTextColor(Color.WHITE);
        v.setTypeface(Typeface.SERIF, Typeface.ITALIC);
        t.setView(v);
        t.setDuration(i);
        t.show();
        return t;
    }
}
