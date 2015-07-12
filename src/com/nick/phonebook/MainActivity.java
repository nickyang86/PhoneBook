package com.nick.phonebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
//	private ImageView mImage;
//	private TextView mName;
//	private TextView mNumber;
//	private ImageButton mMessage;
//	private ImageButton mCall;
	
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	private static final String SORT_KEY = "sort_key";
	
	private List<ContentValues> listData;  
	private AsyncQueryHandler asyncQuery;    
	private ListAdapter adapter;
	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lv = (ListView) findViewById(R.id.lv);
//		initView();
		
		//实例化一个AsyncQueryHandler对象
		asyncQuery = new MyAsyncQueryHandler(getContentResolver()); 
		asyncQueryContact();
		
//		listData = getData();
	}

	private void asyncQueryContact() {  
        // TODO Auto-generated method stub  
        Uri uri = Uri.parse("content://com.android.contacts/data/phones");    
        String[] projection = { "_id", "display_name", "data1", "sort_key" };    
        asyncQuery.startQuery(0, null, uri, projection, null, null,"sort_key COLLATE LOCALIZED asc");  
    }
	
	private List<ContentValues> getData(){
		List<ContentValues> data = new ArrayList<ContentValues>();
		
		ContentValues cv = new ContentValues();
		cv.put(NAME, "nick");
		cv.put(NUMBER, "18512345678");
		data.add(cv);
		
		cv = new ContentValues();
		cv.put(NAME, "jenny");
		cv.put(NUMBER, "15012345678");
		data.add(cv);
		
		return data;
	}

//	private void initView() {
//		// TODO Auto-generated method stub
//		mImage = (ImageView) findViewById(R.id.iv_image);
//		mName = (TextView) findViewById(R.id.tv_name);
//		mNumber = (TextView) findViewById(R.id.tv_number);
//		mMessage = (ImageButton) findViewById(R.id.ib_msg);
//		mCall = (ImageButton) findViewById(R.id.ib_call);
//	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler{

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				listData = new ArrayList<ContentValues>();
				
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					ContentValues cv = new ContentValues();
					String name = cursor.getString(1);
					String num = cursor.getString(2);
					String sort = cursor.getString(3);
					
					cv.put(NAME, name);
					cv.put(NUMBER, num);
					cv.put(SORT_KEY, sort);
					listData.add(cv);
				}
				
				adapter = new ListAdapter(MainActivity.this, listData);
				lv.setAdapter(adapter);
			}
		}
		
	}
	
	
	private class ListAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private List<ContentValues> listData;
		
		public ListAdapter(Context context, List<ContentValues> listData) {
			this.inflater = LayoutInflater.from(context);
			this.listData = listData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//convertView就是列表中的每一项的ViewGroup
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_list, null);
				holder.name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.num = (TextView) convertView.findViewById(R.id.tv_number);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			ContentValues cv = listData.get(position);
			holder.name.setText(cv.getAsString(NAME));
			holder.num.setText(cv.getAsString(NUMBER));
			return convertView;
		}
		
		private class ViewHolder{
			TextView name;
			TextView num;
		}
		
	}
}
