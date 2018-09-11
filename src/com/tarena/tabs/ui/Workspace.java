
package com.tarena.tabs.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class Workspace extends FrameLayout {
	private static final double TAN30 = Math.tan(Math.toRadians(30));
	private static final int INVALID_SCREEN = -1;

	private static final int SNAP_VELOCITY = 350;

	private int mDefaultScreen;

	private Paint mPaint;
	private Bitmap mWallpaper;

	private int mWallpaperWidth;
	private int mWallpaperHeight;
	private float mWallpaperOffset;
	private boolean mWallpaperLoaded;

	private boolean mFirstLayout = true;

	protected int mCurrentScreen;
	protected int mDestnationScreen;
	private int mNextScreen = INVALID_SCREEN;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private float mLastMotionX;
	private float mLastMotionY;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	private int mTouchState = TOUCH_STATE_REST;

	private OnLongClickListener mLongClickListener;

	private int[] mTempCell = new int[2];

	private boolean mAllowLongPress;

	private int mTouchSlop;
	private boolean mAlloweffect = true;

	final Rect mDrawerBounds = new Rect();
	final Rect mClipBounds = new Rect();
	int mDrawerContentHeight;
	int mDrawerContentWidth;
	
	
	private boolean isShowInput; 
	
	private Rect mTouchRect = new Rect();

	public Workspace(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	
	public Workspace(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDefaultScreen = 0;
		initWorkspace();
		setDrawingCacheEnabled(true);
		setAlwaysDrawnWithCacheEnabled(true);

	}

	private void initWorkspace() {
		mScroller = new Scroller(getContext());
		mCurrentScreen = mDefaultScreen;
		mPaint = new Paint();
		mPaint.setDither(false);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	void loadWallpaper(Bitmap bitmap) {
		mWallpaper = bitmap;
		mWallpaperLoaded = true;
		requestLayout();
		invalidate();
	}

	boolean isDefaultScreenShowing() {
		return mCurrentScreen == mDefaultScreen;
	}

	public void setAlloweffect(boolean alloweffect){
		mAlloweffect = alloweffect;
	}
	
	public int getCurrentScreen() {
		return mCurrentScreen;
	}
	public int getDesScreen(){
		return mDestnationScreen;
	}

	public void setCurrentScreen(int currentScreen) {
		mCurrentScreen = Math.max(0,
				Math.min(currentScreen, getChildCount() - 1));
		mDestnationScreen = mCurrentScreen;
		scrollTo(mCurrentScreen * getWidth(), 0);

	}

	
	void showDefaultScreen() {
		setCurrentScreen(mDefaultScreen);
	}

	
	public void setOnLongClickListener(OnLongClickListener l) {
		mLongClickListener = l;
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).setOnLongClickListener(l);
		}
	}


	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else if (mNextScreen != INVALID_SCREEN) {
			final int whichScreen = mCurrentScreen;
			mCurrentScreen = Math.max(0,
					Math.min(mNextScreen, getChildCount() - 1));
			mDestnationScreen = mCurrentScreen;
			mNextScreen = INVALID_SCREEN;
			clearChildrenCache();
			if ( onScreenChangeListener != null ) {
				onScreenChangeListener.onScrrenChangeEnd(mCurrentScreen);
			}
		}
	}


	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		final int wallpaperWidth = mWallpaperWidth;
		mWallpaperOffset = wallpaperWidth > width ? (count * width - wallpaperWidth)
				/ ((count - 1) * (float) width)
				: 1.0f;

		if (mFirstLayout) {
			scrollTo(mCurrentScreen * width, 0);
			mFirstLayout = false;
		}
	}
	

	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int childLeft = 0;

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
		
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				if (!isShowInput || i == mCurrentScreen) {
					child.layout(childLeft, 0, childLeft + childWidth,
							child.getMeasuredHeight());
				}
				childLeft += childWidth;
			}
		}
		isShowInput = false;
		
	}


	public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
			boolean immediate) {
		int screen = indexOfChild(child);
		if (screen != mCurrentScreen || !mScroller.isFinished()) {
			snapToScreen(screen);
			return true;
		}
		return false;
	}


	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {

		int focusableScreen;
		if (mNextScreen != INVALID_SCREEN) {
			focusableScreen = mNextScreen;
		} else {
			focusableScreen = mCurrentScreen;
		}
		if(focusableScreen>=getChildCount()) focusableScreen=getChildCount()-1;
		if(getChildAt(focusableScreen) != null)
		{
			getChildAt(focusableScreen).requestFocus(direction,
					previouslyFocusedRect);
		}
		
		return false;
	}


	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (direction == View.FOCUS_LEFT) {
			if (getCurrentScreen() > 0 ) {
				snapToScreen(getCurrentScreen() - 1);
				return true;
			}
		} else if (direction == View.FOCUS_RIGHT ) {
			if (getCurrentScreen() < getChildCount() - 1) {
				snapToScreen(getCurrentScreen() + 1);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}

	
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		
		Rect r = new Rect();
		focused.getDrawingRect(r);
		focused.requestRectangleOnScreen(r);
	}


	public void addFocusables(ArrayList<View> views, int direction) {

		getChildAt(mCurrentScreen).addFocusables(views, direction);
		if (direction == View.FOCUS_LEFT) {
			if (mCurrentScreen > 0) {
				getChildAt(mCurrentScreen - 1).addFocusables(views, direction);
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (mCurrentScreen < getChildCount() - 1) {
				getChildAt(mCurrentScreen + 1).addFocusables(views, direction);
			}
		}

	}

	private boolean find(View view, boolean isLeft, MotionEvent event) {
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			for (int i = 0; i < parent.getChildCount(); i++) {
				View child = parent.getChildAt(i);
			
				if (child instanceof Workspace) {
					Workspace ws = (Workspace) child;
					ws.getGlobalVisibleRect(mTouchRect);
					if (!ws.isShown() || !mTouchRect.contains((int)event.getRawX(), (int)event.getRawY())) {
						return false;
					}
					if (isLeft) {
						if (ws.getCurrentScreen() > 0
								|| getCurrentScreen() == 0) {
							return true;
						} else if (ws.mDestnationScreen != mCurrentScreen) {
							ws.setCurrentScreen(ws.mDestnationScreen);
						}
					} else {
						if (ws.getCurrentScreen() < ws.getChildCount() - 1
								|| getCurrentScreen() == getChildCount() - 1) {
							return true;
						} else if (ws.mDestnationScreen != mCurrentScreen) {
							ws.setCurrentScreen(ws.mDestnationScreen);
						}
					}
				}
				if (child instanceof ViewGroup) {
					ViewGroup cc = (ViewGroup) child;
					if (find(cc, isLeft, event)) {
						return true;
					}
				}
			}
		}
		return false;
	}


	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			boolean left = (ev.getX() - mLastMotionX) > 0;
			boolean find = find(getChildAt(mDestnationScreen), left, ev);

			if (find) {
				return false;
			}
		}
		
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);

			final int touchSlop = mTouchSlop;
			boolean xMoved = xDiff > touchSlop;
			boolean yMoved = yDiff > touchSlop;
			double tan = yDiff / (double) xDiff;
			if ((xMoved || yMoved) && tan < TAN30) {

				if (xMoved) {
					
					mTouchState = TOUCH_STATE_SCROLLING;
					enableChildrenCache();
				}
				
				if (mAllowLongPress) {
					mAllowLongPress = false;
					
					final View currentScreen = getChildAt(mCurrentScreen);
					currentScreen.cancelLongPress();
				}
			}
			break;

		case MotionEvent.ACTION_DOWN:
			
			mLastMotionX = x;
			mLastMotionY = y;
			mAllowLongPress = true;

			
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		
			clearChildrenCache();
			mTouchState = TOUCH_STATE_REST;
			mAllowLongPress = false;
			break;
		}

		
		View v = getChildAt(mDestnationScreen);
		return mTouchState != TOUCH_STATE_REST
				&& (!find(v, true, ev) && !find(v, false, ev));
	}

	void enableChildrenCache() {

	}

	void clearChildrenCache() {

	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		if (h < oldh && w == oldw) {
			isShowInput = true;
		} else {
			isShowInput = false;
		}
		if (!mScroller.isFinished()) {
			
			mScroller.forceFinished(true);
		}
		scrollTo(w * mDestnationScreen, 0);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
		
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				
				final int deltaX = (int) (mLastMotionX - x);
				if(deltaX > 0 &&!mAlloweffect && mCurrentScreen == getChildCount()-1){
					
					onScreenChangeListener.onScrrenChangeEnd(getChildCount());
					return true;
				}
				mLastMotionX = x;

				if (deltaX < 0) {
					if (getScrollX() > 0) {
						scrollBy(Math.max(-getScrollX(), deltaX), 0);
					}
					else{
						if(mAlloweffect){
							scrollBy(deltaX/3, 0);
						}
					}
				} else if (deltaX > 0) {
					final int availableToScroll = getChildAt(
							getChildCount() - 1).getRight()
							- getScrollX() - getWidth();
					if (availableToScroll > 0) {
						scrollBy(Math.min(availableToScroll, deltaX), 0);
					}
					else
					{
						if(mAlloweffect){
							scrollBy(deltaX/3, 0);
						}
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();

				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					
					snapToScreen(mCurrentScreen - 1);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurrentScreen < getChildCount() - 1) {
					
					snapToScreen(mCurrentScreen + 1);
				} else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
		}

		return true;
	}

	private void snapToDestination() {
		final int screenWidth = getWidth();
		final int whichScreen = (getScrollX() + (screenWidth / 2))
				/ screenWidth;

		snapToScreen(whichScreen);
	}
	
	public void snapToScreen(int whichScreen, int duration) {
		enableChildrenCache();

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		boolean changingScreens = whichScreen != mCurrentScreen;
		if (mDestnationScreen != whichScreen) {
			mDestnationScreen = whichScreen;
			if (onScreenChangeListener != null) {
				onScreenChangeListener.onScreenChangeStart(whichScreen);
			}
		}
		mNextScreen = whichScreen;
		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens
				&& focusedChild == getChildAt(mCurrentScreen)) {
			focusedChild.clearFocus();
		}
		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
		invalidate();
	}
	
	public void snapToScreen(int whichScreen) {
		enableChildrenCache();

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		boolean changingScreens = whichScreen != mCurrentScreen;
		if (mDestnationScreen != whichScreen) {
			mDestnationScreen = whichScreen;
			if (onScreenChangeListener != null) {
				onScreenChangeListener.onScreenChangeStart(whichScreen);
			}
		}
		mNextScreen = whichScreen;
		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens
				&& focusedChild == getChildAt(mCurrentScreen)) {
			focusedChild.clearFocus();
		}
		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) );
		invalidate();
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final SavedState state = new SavedState(super.onSaveInstanceState());
		state.currentScreen = mCurrentScreen;
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		if (savedState.currentScreen != -1) {
			mCurrentScreen = savedState.currentScreen;
		}
	}

	public void scrollLeft() {
		if (mNextScreen == INVALID_SCREEN && mCurrentScreen > 0
				&& mScroller.isFinished()) {
			snapToScreen(mCurrentScreen - 1);
		}
	}

	public void scrollRight() {
		if (mNextScreen == INVALID_SCREEN
				&& mCurrentScreen < getChildCount() - 1
				&& mScroller.isFinished()) {
			snapToScreen(mCurrentScreen + 1);
		}
	}

	public int getScreenForView(View v) {
		int result = -1;
		if (v != null) {
			ViewParent vp = v.getParent();
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (vp == getChildAt(i)) {
					return i;
				}
			}
		}
		return result;
	}

	
	public boolean allowLongPress() {
		return mAllowLongPress;
	}


	public void setAllowLongPress(boolean allowLongPress) {
		mAllowLongPress = allowLongPress;
	}

	void removeShortcutsForPackage(String packageName) {
	}

	void updateShortcutsForPackage(String packageName) {
	}

	void moveToDefaultScreen() {
		snapToScreen(mDefaultScreen);
		getChildAt(mDefaultScreen).requestFocus();
	}

	public static class SavedState extends BaseSavedState {
		int currentScreen = -1;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentScreen = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(currentScreen);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	private OnScreenChangeListener onScreenChangeListener;

	public interface OnScreenChangeListener {
		void onScreenChangeStart(int whichScreen);
		void onScrrenChangeEnd(int whichScreen);
	}

	public void setOnScreenChangeListener(OnScreenChangeListener listener) {
		onScreenChangeListener = listener;
	}
}
