package com.tarena.tabs.util;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
	//View的findViewById()方法也是比较耗时的，因此需要考虑只调用一次，之后就用View.getTag()方法来获得ViewHolder对象。 
	// I added a generic return type to reduce the casting noise in client code
	//ViewHolder将需要缓存的view封装好 

	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {//静态方法
// View的findViewById()方法也是比较耗时的，因此需要考虑只调用一次，之后就用View.getTag()方法来获得ViewHolder对象。
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

		if (viewHolder == null) {

			viewHolder = new SparseArray<View>();	
     //setTag才是将这些view缓存起来供下次调用。
			view.setTag(viewHolder);

		}

		View childView = viewHolder.get(id);

		if (childView == null) {

			childView = view.findViewById(id);

			viewHolder.put(id, childView);

		}

		return (T) childView;

	}

}