/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.data;

import java.util.List;

public class AlertItem {

    public int mNumbers;

    public int mNewNumber;

    public List<AlertMeta> mLAlertMetas;

    public AlertItem(List<AlertMeta> metas) {
        mLAlertMetas = metas;
        mNumbers = metas.size();
    }

    public void setNews(List<AlertMeta> meta) {
        if (meta != null) {
            mLAlertMetas.addAll(meta);
            mNewNumber = meta.size();
        }
    }
}
