package com.tarena.tabs.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tarena.tabs.ui.R;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FeedBackActivity extends Activity {

	private ListView mListView;//列表
	private FeedbackAgent mAgent;
	private Conversation mComversation;
	private Context mContext;
	private ReplyAdapter adapter;
	private Button sendBtn;//发送按钮
	private EditText inputEdit;//输入编辑框
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private final int VIEW_TYPE_COUNT = 2;
	private final int VIEW_TYPE_USER = 0;
	private final int VIEW_TYPE_DEV = 1;
	private final String TAG = FeedBackActivity.class.getName();

	private Handler mHandler = new Handler() {//创建一盒Handler对象
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();//notifyDataSetChanged()可以在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.umeng_activity);//加载布局文件
		mContext = this;//与下面一句合写为FeedbackAgent agent = new FeedbackAgent(context);
		initView();
		mAgent = new FeedbackAgent(this);//调用函数进入反馈界面：
         //FeedbackAgent类中提供了一个getDefaultConversation()方法来获取默认的Conversation()
		mComversation = new FeedbackAgent(this).getDefaultConversation();//获取Conversation对象，所有会话数据都在这里
		//创建Adapter并为ListView设置Adapter
		adapter = new ReplyAdapter();
		mListView.setAdapter(adapter);
		sync();// 数据同步

	}

	private void initView() {//界面
		mListView = (ListView) findViewById(R.id.fb_reply_list);
		sendBtn = (Button) findViewById(R.id.fb_send_btn);//发送按钮
		inputEdit = (EditText) findViewById(R.id.fb_send_content);
		// 下拉刷新组件
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fb_reply_refresh);
		//监听发送按钮的点击事件
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = inputEdit.getText().toString();
				inputEdit.getEditableText().clear();
				if (!TextUtils.isEmpty(content)) {
					// 将内容添加到会话列表
					mComversation.addUserReply(content);
					// 刷新新ListView
					mHandler.sendMessage(new Message());
					// 数据同步
					sync();
				}
			}
		});

		// 下拉刷新
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				sync();// 数据同步
			}
		});
	}

	// 数据同步
	private void sync() {
		
     //利用Comversation.sync方法来进行App端的数据与服务器端同步
		mComversation.sync(new SyncListener() {
			//Comversation.sync()方法该方法实现了数据的同步，即可以通过该方法发送用户反馈、回复也可以通过该方法获取来自开发者的回复。
			@Override
			//Conversation类提供了getReplyList()方法来获取整个会话的内容，返回的是List<Reply>，同时也提供了addUserReply()方法，将用反馈内容加入到Conversation
			public void onSendUserReply(List<Reply> replyList) {
			}

			@Override
			public void onReceiveDevReply(List<Reply> replyList) {
				// SwipeRefreshLayout停止刷新
				mSwipeRefreshLayout.setRefreshing(false);
				// 发送消息，刷新ListView
				mHandler.sendMessage(new Message());
				// 如果回复者没有新的回复数据，则返回
				if (replyList == null || replyList.size() < 1) {
					return;
				}
			}
		});
		// 更新adapter，刷新ListView
		adapter.notifyDataSetChanged();
	}

	// 为ListView创建Adapter
	class ReplyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mComversation.getReplyList().size();
		}

		@Override
		public Object getItem(int arg0) {
			return mComversation.getReplyList().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public int getViewTypeCount() {
			// 两种不同的Item布局
			return VIEW_TYPE_COUNT;
		}

		@Override
		public int getItemViewType(int position) {
			// 获取单条回复
			Reply reply = mComversation.getReplyList().get(position);
			if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// 回复者者回复Item布局
				return VIEW_TYPE_DEV;
			} else {
				// 用户反馈、回复Item布局
				return VIEW_TYPE_USER;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			// 获取单条回复
			Reply reply = mComversation.getReplyList().get(position);
			if (convertView == null) {
				// 根据Type的类型来加载不同的Item布局
				if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
					// 开发者的回复
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.umeng_fb_dev_reply, null);
				} else {
					// 用户的反馈、回复
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.umeng_fb_user_reply, null);
				}

				// 创建ViewHolder并获取各种View
				holder = new ViewHolder();
				holder.replyContent = (TextView) convertView
						.findViewById(R.id.fb_reply_content);
				holder.replyProgressBar = (ProgressBar) convertView
						.findViewById(R.id.fb_reply_progressBar);
				holder.replyStateFailed = (ImageView) convertView
						.findViewById(R.id.fb_reply_state_failed);
				holder.replyData = (TextView) convertView
						.findViewById(R.id.fb_reply_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 以下是填充数据
			// 设置Reply的内容?
			holder.replyContent.setText(reply.content);
			// 在App应用界面，对于开发者的Reply来讲status没有意义
			if (!Reply.TYPE_DEV_REPLY.equals(reply.type)) {
				// 根据Reply的状态来设置replyStateFailed的状态
				if (Reply.STATUS_NOT_SENT.equals(reply.status)) {
					holder.replyStateFailed.setVisibility(View.VISIBLE);
				} else {
					holder.replyStateFailed.setVisibility(View.GONE);
				}

				// 根据Reply的状态来设置replyProgressBar的状态
				if (Reply.STATUS_SENDING.equals(reply.status)) {
					holder.replyProgressBar.setVisibility(View.VISIBLE);
				} else {
					holder.replyProgressBar.setVisibility(View.GONE);
				}
			}

			// 回复的时间数据，这里仿照QQ两条Reply之间相差100000ms则展示时间
			if ((position + 1) < mComversation.getReplyList().size()) {
				Reply nextReply = mComversation.getReplyList()
						.get(position + 1);
				if (nextReply.created_at - reply.created_at > 100000 && holder.replyData!=null) {
					Date replyTime = new Date(reply.created_at);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					holder.replyData.setText(sdf.format(replyTime));
					holder.replyData.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}

		class ViewHolder {
			TextView replyContent;
			ProgressBar replyProgressBar;
			ImageView replyStateFailed;
			TextView replyData;
		}
	}

}
