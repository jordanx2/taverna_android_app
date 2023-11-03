import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;


public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    public CustomerSpinnerAdapter(Context context, int resource, String[] spinnerItems) {
        super(context, resource, spinnerItems);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ((TextView) view).setTextColor(getContext().getResources().getColor(android.R.color.white));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        ((TextView) view).setTextColor(getContext().getResources().getColor(android.R.color.white));
        return view;
    }

    private void customizeView(View view) {
        ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.white));
        Drawable border = ContextCompat.getDrawable(context, R.drawable.spinner_border); // Custom border drawable
        view.setBackground(border);
    }

}
