package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;


/**
 * Service that provides the factory to be bound to the collection service.
 */
public class QuoteWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new QuoteWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

/**
 * This is the factory that will provide data to the collection widget.
 */
class QuoteWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = QuoteWidgetRemoteViewsFactory.class.getSimpleName();
    private Cursor mStockQuoteCursor;
    private static Context mContext;
    private int mQuoteWidgetId;

    public QuoteWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mQuoteWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

        public void onCreate() {
            Log.d(TAG, "onCreate");
            // do nothing since we load the data from the cursor in onDataSetChanged()
        }

        public void onDestroy() {
            Log.d(TAG, "onDestroy");
            // In onDestroy() you should tear down anything that was setup for your data source,
            // eg. cursors, connections, etc.
            if (mStockQuoteCursor != null) {
                mStockQuoteCursor.close();
            }
        }

        public int getCount() {
            Log.d(TAG, "getCount");
            if (mStockQuoteCursor != null) {
            return mStockQuoteCursor.getCount();
            } else {
                return 0;
            }
        }

        public RemoteViews getViewAt(int position) {
           // Log.d(TAG, "getViewAt");
            // get the data for this position from the content provider
            String symbol = "";
            String quote = "";
            String change = "";

            if (mStockQuoteCursor.moveToPosition(position)) {
                symbol = mStockQuoteCursor.getString(
                        mStockQuoteCursor.getColumnIndex(QuoteColumns.SYMBOL));
                quote = mStockQuoteCursor.getString(
                        mStockQuoteCursor.getColumnIndex(QuoteColumns.BIDPRICE));
                change = mStockQuoteCursor.getString(
                        mStockQuoteCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE));
            }

            // construct a remote views item based on the widget item xml file, and set the
            // text based on the position.
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_collection_item);


            rv.setTextViewText(R.id.stock_symbol, symbol);
           // rv.setTextViewTextSize(R.id.stock_symbol, TypedValue.COMPLEX_UNIT_SP,10);

            rv.setTextViewText(R.id.stock_quote, quote);

            if (mStockQuoteCursor.getInt(mStockQuoteCursor.getColumnIndex("is_up")) == 1) {
                rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }
            rv.setTextViewText(R.id.change, change);


            return rv;
        }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(TAG, "getLoadingView");
        return null;
    }


    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount");
            return 1;
        }

    public long getItemId(int position) {
     //   Log.d(TAG, "getItemId");
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged");
        if (mStockQuoteCursor != null) {
            mStockQuoteCursor.close();
        }

        final long token = Binder.clearCallingIdentity();
        try {
            mStockQuoteCursor = mContext.getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI, null, QuoteColumns.ISCURRENT + " = 1", null, null);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

}
