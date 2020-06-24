package sugarrain.oss.danbi_oss5;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

class MemoAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    List<MemoData> mItems;
    DBHelper dbHelper;

    public MemoAdapter(List<MemoData> mItems) {
        this.mItems = mItems;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_memo, parent, false);
        dbHelper = new DBHelper(parent.getContext(), DBHelper.dbName, null, DBHelper.dbVersion);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일 a hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mItems.get(position).getTime());
        holder.content.setText(mItems.get(position).getMemo());
        holder.date.setText(sdf.format(calendar.getTime()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(MemoData data, DBHelper dbHelper) {
        dbHelper.addData(data);
        mItems.add(data);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        dbHelper.deleteData(mItems.get(position).getId());
        mItems.remove(position);
        notifyDataSetChanged();
    }

}

class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView content;
    TextView date;
    ImageButton delete;

    public ItemViewHolder(View itemView) {
        super(itemView);
        content = itemView.findViewById(R.id.memo_content);
        date = itemView.findViewById(R.id.memo_date);
        delete = itemView.findViewById(R.id.btn_delete);
    }
}
